import { Component, computed, effect, inject, signal } from '@angular/core';
import { NgClass } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import type { Trip } from '../trips/trips';
import { TripService, TripResponse } from '../../services/trip.service';
import { CityService, City } from '../../services/city.service';
import { ToastService } from '../../services/toast.service';
import { StepperComponent, Step } from '../../components/stepper/stepper';
import { environment } from '../../../environments/environment';

const BOOKING_CONTACT_STORAGE_KEY = 'easybus_booking_contact';
const BOOKING_SEATS_STORAGE_KEY = 'easybus_booking_seats';

export interface BookingState {
  trip: Trip;
  fromCityName: string;
  toCityName: string;
}

/** Map seat id "1A", "2B" etc to backend seat number 1-40. */
function seatIdToNumber(seatId: string): number {
  const col = seatId.slice(-1);
  const row = parseInt(seatId.slice(0, -1), 10);
  const colIndex = ['A', 'B', 'C', 'D'].indexOf(col);
  return (row - 1) * 4 + colIndex + 1;
}

@Component({
  selector: 'app-booking',
  standalone: true,
  imports: [FormsModule, NgClass, StepperComponent],
  templateUrl: './booking.html',
})
export class Booking {
  private readonly router = inject(Router);
  private readonly route = inject(ActivatedRoute);
  private readonly http = inject(HttpClient);
  private readonly tripService = inject(TripService);
  private readonly cityService = inject(CityService);
  private readonly toast = inject(ToastService);

  protected readonly trip = signal<Trip | null>(null);
  protected readonly fromCityName = signal<string>('');
  protected readonly toCityName = signal<string>('');
  protected readonly date = signal<string>('');
  protected readonly passengers = signal<number>(1);
  protected readonly contactName = signal<string>('');
  protected readonly contactEmail = signal<string>('');
  protected readonly contactPhone = signal<string>('');
  protected readonly selectedSeats = signal<string[]>([]);
  protected readonly confirmed = signal(false);
  protected readonly paymentLoading = signal(false);
  protected readonly paymentError = signal<string | null>(null);
  protected readonly paypalCancel = signal(false);
  protected readonly paypalReturnMode = signal(false);
  protected readonly tripLoading = signal(false);
  protected readonly bookingIds = signal<number[]>([]);

  // Stepper
  protected readonly currentStep = signal(0);
  protected readonly steps: Step[] = [
    { label: 'Seats', icon: 'seat' },
    { label: 'Contact', icon: 'user' },
    { label: 'Payment', icon: 'card' },
    { label: 'Confirmed', icon: 'check' },
  ];

  // Form validation
  protected readonly contactAttempted = signal(false);

  private static readonly SEAT_ROWS = 10;
  private static readonly SEAT_COLS = ['A', 'B', 'C', 'D'];

  protected readonly seatLayout = computed(() => {
    const t = this.trip();
    if (!t) return { seats: [] as { id: string; row: number; col: string; available: boolean }[], occupiedCount: 0 };
    const seats: { id: string; row: number; col: string; available: boolean }[] = [];
    const total = Booking.SEAT_ROWS * Booking.SEAT_COLS.length;
    const availableSet = t.availableSeatsList ? new Set(t.availableSeatsList) : null;
    const occupiedCount = availableSet
      ? total - availableSet.size
      : Math.min(total, Math.max(0, total - t.availableSeats));
    for (let row = 1; row <= Booking.SEAT_ROWS; row++) {
      for (const col of Booking.SEAT_COLS) {
        const id = `${row}${col}`;
        const available = availableSet ? availableSet.has(id) : row <= Booking.SEAT_ROWS - Math.ceil(occupiedCount / 4);
        seats.push({ id, row, col, available });
      }
    }
    return { seats, occupiedCount };
  });

  protected readonly seatsByRow = computed(() => {
    const { seats } = this.seatLayout();
    const rows: { row: number; seats: { id: string; row: number; col: string; available: boolean }[] }[] = [];
    for (let r = 1; r <= Booking.SEAT_ROWS; r++) {
      rows.push({ row: r, seats: seats.filter((s) => s.row === r) });
    }
    return rows;
  });

  protected readonly canConfirm = computed(() => {
    const selected = this.selectedSeats();
    const p = this.passengers();
    return selected.length === p && p > 0;
  });

  protected readonly isContactValid = computed(() => {
    return this.contactName().trim().length > 0 && this.isValidEmail(this.contactEmail());
  });

  protected readonly formattedDate = computed(() => {
    const d = this.date();
    if (!d) return '';
    const [y, m, day] = d.split('-').map(Number);
    const date = new Date(y, m - 1, day);
    return date.toLocaleDateString(undefined, {
      weekday: 'long', month: 'long', day: 'numeric', year: 'numeric',
    });
  });

  protected readonly totalPrice = computed(() => {
    const t = this.trip();
    const p = this.passengers();
    return t ? t.pricePerSeat * p : 0;
  });

  protected readonly bookingReference = computed(() => {
    const ids = this.bookingIds();
    return ids.length > 0 ? `EB-${ids[0]}` : `EB-${Date.now().toString(36).toUpperCase()}`;
  });

  protected readonly formatDuration = (minutes: number): string => {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    return m > 0 ? `${h}h ${m}m` : `${h}h`;
  };

  constructor() {
    this.loadFromStateAndParams();
    this.restoreContactFromStorage();
    this.handlePaypalReturn();
    effect(() => {
      this.contactName();
      this.contactEmail();
      this.contactPhone();
      this.persistContactToStorage();
    });
  }

  private isValidEmail(email: string): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email.trim());
  }

  private loadFromStateAndParams(): void {
    const state = history.state as BookingState | undefined;
    const q = this.route.snapshot.queryParams;
    if (q['date']) this.date.set(q['date']);
    if (q['passengers']) this.passengers.set(Number(q['passengers']) || 1);
    if (q['paypal'] === 'cancel') this.paypalCancel.set(true);

    if (state?.trip) {
      this.trip.set(state.trip);
      this.fromCityName.set(state.fromCityName ?? '');
      this.toCityName.set(state.toCityName ?? '');
    } else if (q['tripId'] && q['paypal'] !== 'return') {
      this.tripLoading.set(true);
      const tripId = Number(q['tripId']);
      this.tripService.getTripById(tripId).subscribe({
        next: (r: TripResponse) => {
          const dep = r.departure;
          const arr = r.arrival;
          const depMinutes = this.timeToMinutes(dep);
          const arrMinutes = this.timeToMinutes(arr);
          let duration = arrMinutes - depMinutes;
          if (duration < 0) duration += 24 * 60;
          const trip: Trip = {
            id: r.id, operator: r.operator,
            departureTime: dep.substring(0, 5), arrivalTime: arr.substring(0, 5),
            durationMinutes: duration, pricePerSeat: r.price,
            availableSeats: r.availableSeats.length, availableSeatsList: r.availableSeats,
            equipments: r.equipments ?? [],
          };
          this.trip.set(trip);
          this.cityService.getCities().subscribe((cities) => {
            this.fromCityName.set(cities.find((c: City) => c.id === (q['from'] ?? r.fromCity))?.name ?? '');
            this.toCityName.set(cities.find((c: City) => c.id === (q['to'] ?? r.toCity))?.name ?? '');
            this.tripLoading.set(false);
          });
          this.restoreSeatsFromStorage();
        },
        error: () => { this.tripLoading.set(false); },
      });
    }

    this.route.queryParams.subscribe((params) => {
      if (params['date']) this.date.set(params['date']);
      if (params['passengers']) this.passengers.set(Number(params['passengers']) || 1);
    });
  }

  private timeToMinutes(time: string): number {
    const parts = time.split(':').map(Number);
    return parts[0] * 60 + (parts[1] || 0);
  }

  private restoreContactFromStorage(): void {
    try {
      const raw = sessionStorage.getItem(BOOKING_CONTACT_STORAGE_KEY);
      if (!raw) return;
      const data = JSON.parse(raw) as { name?: string; email?: string; phone?: string };
      if (data.name != null) this.contactName.set(data.name);
      if (data.email != null) this.contactEmail.set(data.email);
      if (data.phone != null) this.contactPhone.set(data.phone);
    } catch { /* ignore */ }
  }

  private persistContactToStorage(): void {
    try {
      sessionStorage.setItem(BOOKING_CONTACT_STORAGE_KEY, JSON.stringify({
        name: this.contactName(), email: this.contactEmail(), phone: this.contactPhone(),
      }));
    } catch { /* ignore */ }
  }

  private handlePaypalReturn(): void {
    const q = this.route.snapshot.queryParams;
    const token = q['token'] as string | undefined;
    if (q['paypal'] !== 'return' || !token) return;
    this.paypalReturnMode.set(true);
    this.paymentLoading.set(true);
    this.paymentError.set(null);
    this.http
      .post<{ success: boolean; bookingIds?: number[]; reservationIds?: number[]; message?: string }>(
        `${environment.apiUrl}/paypal/capture`, { orderId: token })
      .subscribe({
        next: (res) => {
          this.paymentLoading.set(false);
          if (res.success) {
            this.bookingIds.set(res.bookingIds ?? res.reservationIds ?? []);
            this.confirmed.set(true);
            this.currentStep.set(3);
            this.toast.success('Booking confirmed! Check your email for details.');
          } else {
            this.paymentError.set(res.message ?? 'Payment could not be completed.');
          }
        },
        error: (err) => {
          this.paymentLoading.set(false);
          this.paymentError.set(err.error?.message ?? err.message ?? 'Payment failed.');
        },
      });
  }

  // Stepper navigation
  protected goToStep(step: number): void {
    if (step <= this.currentStep()) {
      this.currentStep.set(step);
    }
  }

  protected nextStep(): void {
    const step = this.currentStep();
    if (step === 0 && !this.canConfirm()) return;
    if (step === 1) {
      this.contactAttempted.set(true);
      if (!this.isContactValid()) return;
    }
    this.currentStep.set(step + 1);
  }

  protected prevStep(): void {
    const step = this.currentStep();
    if (step > 0) this.currentStep.set(step - 1);
  }

  protected goBack(): void {
    this.router.navigate(['/trips'], { queryParamsHandling: 'preserve' });
  }

  protected toggleSeat(seatId: string): void {
    const layout = this.seatLayout();
    const seat = layout.seats.find((s) => s.id === seatId);
    if (!seat || !seat.available) return;
    const current = this.selectedSeats();
    const idx = current.indexOf(seatId);
    let next: string[];
    if (idx >= 0) {
      next = current.filter((_, i) => i !== idx);
    } else if (current.length < this.passengers()) {
      next = [...current, seatId].sort();
    } else {
      return;
    }
    this.selectedSeats.set(next);
    this.persistSeatsToStorage(next);
  }

  private persistSeatsToStorage(seats: string[]): void {
    try { sessionStorage.setItem(BOOKING_SEATS_STORAGE_KEY, JSON.stringify(seats)); } catch { /* ignore */ }
  }

  private restoreSeatsFromStorage(): void {
    try {
      const raw = sessionStorage.getItem(BOOKING_SEATS_STORAGE_KEY);
      if (!raw) return;
      const seats = JSON.parse(raw) as string[];
      if (Array.isArray(seats) && seats.length > 0) this.selectedSeats.set(seats);
    } catch { /* ignore */ }
  }

  protected isSeatSelected(seatId: string): boolean {
    return this.selectedSeats().includes(seatId);
  }

  protected getSeatButtonClasses(seat: { id: string; available: boolean }): string {
    const base = 'w-10 h-10 rounded-lg border text-sm font-medium transition-all flex items-center justify-center disabled:pointer-events-none';
    if (!seat.available) {
      return `${base} opacity-40 cursor-not-allowed border-gray-200 dark:border-gray-600 bg-gray-100 dark:bg-gray-800 text-gray-400 dark:text-gray-500`;
    }
    if (this.isSeatSelected(seat.id)) {
      return `${base} border-indigo-500 bg-indigo-600 hover:bg-indigo-700 text-white shadow-md ring-2 ring-indigo-400 ring-offset-2 dark:ring-offset-gray-800`;
    }
    return `${base} border-gray-200 dark:border-gray-600 bg-white dark:bg-gray-700 text-gray-900 dark:text-white hover:bg-indigo-50 dark:hover:bg-indigo-900/30 hover:border-indigo-300`;
  }

  protected payWithPayPal(): void {
    const t = this.trip();
    if (!t || !this.canConfirm()) return;
    const name = this.contactName().trim();
    const email = this.contactEmail().trim();
    const phone = this.contactPhone().trim();
    if (!name || !email) {
      this.paymentError.set('Please enter your name and email.');
      return;
    }
    const tripId = t.id;
    const seatNumbers = this.selectedSeats().map(seatIdToNumber);
    const amount = this.totalPrice();
    const baseUrl = typeof window !== 'undefined' ? window.location.origin : '';
    const returnUrl = `${baseUrl}/booking?paypal=return`;
    const cancelUrl = `${baseUrl}/booking?paypal=cancel`;

    this.paymentLoading.set(true);
    this.paymentError.set(null);
    this.http
      .post<{ orderId: string; approvalUrl: string }>(`${environment.apiUrl}/paypal/create-order`, {
        tripId, seats: seatNumbers.map(String), fullName: name, email, phone,
        amount, currency: 'MAD', returnUrl, cancelUrl,
      })
      .subscribe({
        next: (res) => {
          if (res.approvalUrl) {
            window.location.href = res.approvalUrl;
          } else {
            this.paymentLoading.set(false);
            this.paymentError.set('No approval URL received.');
          }
        },
        error: (err) => {
          this.paymentLoading.set(false);
          this.paymentError.set(err.error?.message ?? err.message ?? 'Could not start payment.');
        },
      });
  }

  protected printTicket(): void {
    window.print();
  }

  protected goHome(): void {
    this.router.navigate(['/']);
  }
}

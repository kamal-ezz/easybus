import { Component, computed, inject, signal } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { CityService, City } from '../../services/city.service';
import { TripService, TripResponse } from '../../services/trip.service';

export interface Trip {
  id: number;
  operator: string;
  departureTime: string;
  arrivalTime: string;
  durationMinutes: number;
  pricePerSeat: number;
  availableSeats: number;
  availableSeatsList: string[];
  equipments: string[];
}

@Component({
  selector: 'app-trips',
  imports: [],
  templateUrl: './trips.html',
})
export class Trips {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly titleService = inject(Title);
  private readonly cityService = inject(CityService);
  private readonly tripService = inject(TripService);

  protected readonly fromId = signal<string>('');
  protected readonly toId = signal<string>('');
  protected readonly date = signal<string>('');
  protected readonly passengers = signal<number>(1);
  protected readonly cities = signal<City[]>([]);
  protected readonly trips = signal<Trip[]>([]);
  protected readonly loading = signal(true);

  protected readonly fromCity = computed(() => {
    const id = this.fromId();
    return this.cities().find((c) => c.id === id)?.name ?? id;
  });
  protected readonly toCity = computed(() => {
    const id = this.toId();
    return this.cities().find((c) => c.id === id)?.name ?? id;
  });
  protected readonly formattedDate = computed(() => {
    const d = this.date();
    if (!d) return '';
    const [y, m, day] = d.split('-').map(Number);
    const date = new Date(y, m - 1, day);
    return date.toLocaleDateString(undefined, {
      weekday: 'long',
      month: 'long',
      day: 'numeric',
      year: 'numeric',
    });
  });
  protected readonly totalPassengers = computed(() => this.passengers());

  constructor() {
    this.cityService.getCities().subscribe((cities) => {
      this.cities.set(cities);
      this.updateTitle();
    });

    this.route.queryParams.subscribe((params) => {
      this.fromId.set(params['from'] ?? '');
      this.toId.set(params['to'] ?? '');
      this.date.set(params['date'] ?? '');
      this.passengers.set(Number(params['passengers']) || 1);
      this.loadTrips();
    });
  }

  private loadTrips(): void {
    this.loading.set(true);
    const from = this.fromId();
    const to = this.toId();
    const date = this.date();
    if (!from || !to || !date) {
      this.trips.set([]);
      this.loading.set(false);
      return;
    }
    this.tripService.searchTrips(from, to, date).subscribe({
      next: (responses) => {
        const trips = responses.map((r) => this.mapTripResponse(r));
        trips.sort((a, b) => a.departureTime.localeCompare(b.departureTime));
        this.trips.set(trips);
        this.loading.set(false);
      },
      error: () => {
        this.trips.set([]);
        this.loading.set(false);
      },
    });
  }

  private mapTripResponse(r: TripResponse): Trip {
    const dep = r.departure; // "HH:mm:ss" or "HH:mm"
    const arr = r.arrival;
    const depMinutes = this.timeToMinutes(dep);
    const arrMinutes = this.timeToMinutes(arr);
    let duration = arrMinutes - depMinutes;
    if (duration < 0) duration += 24 * 60; // overnight trip
    return {
      id: r.id,
      operator: r.operator,
      departureTime: dep.substring(0, 5),
      arrivalTime: arr.substring(0, 5),
      durationMinutes: duration,
      pricePerSeat: r.price,
      availableSeats: r.availableSeats.length,
      availableSeatsList: r.availableSeats,
      equipments: r.equipments ?? [],
    };
  }

  private timeToMinutes(time: string): number {
    const parts = time.split(':').map(Number);
    return parts[0] * 60 + (parts[1] || 0);
  }

  protected formatDuration(minutes: number): string {
    const h = Math.floor(minutes / 60);
    const m = minutes % 60;
    return m > 0 ? `${h}h ${m}m` : `${h}h`;
  }

  protected modifySearch(): void {
    this.router.navigate(['/'], {
      queryParams: {
        from: this.fromId(),
        to: this.toId(),
        date: this.date(),
        passengers: this.passengers(),
      },
    });
  }

  protected selectTrip(trip: Trip): void {
    this.router.navigate(['/booking'], {
      queryParams: {
        tripId: trip.id,
        from: this.fromId(),
        to: this.toId(),
        date: this.date(),
        passengers: this.passengers(),
      },
      state: {
        trip,
        fromCityName: this.fromCity(),
        toCityName: this.toCity(),
      },
    });
  }

  protected totalPrice(trip: Trip): number {
    return trip.pricePerSeat * this.passengers();
  }

  private updateTitle(): void {
    const from = this.fromCity();
    const to = this.toCity();
    if (from && to) {
      this.titleService.setTitle(`${from} to ${to} Buses - EasyBus`);
    }
  }

  protected readonly equipmentMeta: Record<string, { icon: string; label: string }> = {
    WIFI: { icon: 'M8.111 16.404a5.5 5.5 0 017.778 0M12 20h.01m-7.08-7.071c3.904-3.905 10.236-3.905 14.141 0M1.394 9.393c5.857-5.858 15.355-5.858 21.213 0', label: 'WiFi' },
    AIR_CONDITIONER: { icon: 'M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17h14a2 2 0 002-2V5a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z', label: 'Air Conditioning' },
    USB_CHARGER: { icon: 'M13 10V3L4 14h7v7l9-11h-7z', label: 'USB Charging' },
    TV: { icon: 'M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z', label: 'TV' },
    HIGHWAY: { icon: 'M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l5.447 2.724A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7', label: 'Highway' },
    LUGGAGE_SERVICES: { icon: 'M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z', label: 'Luggage' },
    PHARMACY_BOX: { icon: 'M12 9v3m0 0v3m0-3h3m-3 0H9m12 0a9 9 0 11-18 0 9 9 0 0118 0z', label: 'First Aid' },
    LAMP: { icon: 'M9.663 17h4.673M12 3v1m6.364 1.636l-.707.707M21 12h-1M4 12H3m3.343-5.657l-.707-.707m2.828 9.9a5 5 0 117.072 0l-.548.547A3.374 3.374 0 0014 18.469V19a2 2 0 11-4 0v-.531c0-.895-.356-1.754-.988-2.386l-.548-.547z', label: 'Reading Light' },
    WAITING_ROOM: { icon: 'M19 21V5a2 2 0 00-2-2H7a2 2 0 00-2 2v16m14 0h2m-2 0h-5m-9 0H3m2 0h5M9 7h1m-1 4h1m4-4h1m-1 4h1m-5 10v-5a1 1 0 011-1h2a1 1 0 011 1v5m-4 0h4', label: 'Waiting Room' },
  };
}

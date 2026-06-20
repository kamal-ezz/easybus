import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Booking } from './booking';

describe('Booking', () => {
  let component: Booking;
  let fixture: ComponentFixture<Booking>;
  let httpMock: HttpTestingController;

  const mockQueryParams = {
    tripId: '1',
    from: 'casablanca',
    to: 'rabat',
    date: '2026-02-15',
    passengers: '2',
  };

  const mockTrip = {
    id: 1,
    operator: 'CTM',
    departureTime: '08:00',
    arrivalTime: '10:30',
    durationMinutes: 150,
    pricePerSeat: 120,
    availableSeats: 10,
    availableSeatsList: ['1A', '1B', '2A', '2B', '3A', '3B', '4A', '4B', '5A', '5B'],
  };

  beforeEach(async () => {
    // Mock history.state
    Object.defineProperty(window, 'history', {
      value: {
        state: {
          trip: mockTrip,
          fromCityName: 'Casablanca',
          toCityName: 'Rabat',
        },
      },
      writable: true,
    });

    await TestBed.configureTestingModule({
      imports: [Booking],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        provideRouter([]),
        {
          provide: ActivatedRoute,
          useValue: {
            queryParams: of(mockQueryParams),
            snapshot: { queryParams: mockQueryParams },
          },
        },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(Booking);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load trip from state', () => {
    expect((component as any).trip()).toEqual(mockTrip);
    expect((component as any).fromCityName()).toBe('Casablanca');
    expect((component as any).toCityName()).toBe('Rabat');
  });

  it('should display seat selection grid', () => {
    const seatLayout = (component as any).seatLayout();
    expect(seatLayout.seats.length).toBeGreaterThan(0);
  });

  it('should allow selecting seats up to passenger count', () => {
    // Select first seat
    (component as any).toggleSeat('1A');
    expect((component as any).selectedSeats().length).toBe(1);

    // Select second seat (passengers = 2)
    (component as any).toggleSeat('1B');
    expect((component as any).selectedSeats().length).toBe(2);

    // Try to select third seat - should not be allowed
    (component as any).toggleSeat('2A');
    expect((component as any).selectedSeats().length).toBe(2);
  });

  it('should toggle seat selection', () => {
    (component as any).toggleSeat('1A');
    expect((component as any).isSeatSelected('1A')).toBe(true);

    (component as any).toggleSeat('1A');
    expect((component as any).isSeatSelected('1A')).toBe(false);
  });

  it('should enable pay button when correct seats selected', () => {
    expect((component as any).canConfirm()).toBe(false);

    (component as any).toggleSeat('1A');
    expect((component as any).canConfirm()).toBe(false);

    (component as any).toggleSeat('1B');
    expect((component as any).canConfirm()).toBe(true);
  });

  it('should calculate total price correctly', () => {
    expect((component as any).totalPrice()).toBe(240); // 2 passengers * 120
  });

  it('should format duration correctly', () => {
    expect((component as any).formatDuration(150)).toBe('2h 30m');
    expect((component as any).formatDuration(60)).toBe('1h');
  });

  it('should format date correctly', () => {
    const formattedDate = (component as any).formattedDate();
    expect(formattedDate).toContain('2026');
  });

  it('should not allow selecting unavailable seats', () => {
    // Create a trip with limited available seats
    (component as any).trip.set({
      ...mockTrip,
      availableSeatsList: ['1A'],
    });
    fixture.detectChanges();

    // Try to select unavailable seat
    (component as any).toggleSeat('1B');
    expect((component as any).selectedSeats().length).toBe(0);
  });

  it('should get correct seat button classes', () => {
    const availableSeat = { id: '1A', available: true };
    const unavailableSeat = { id: '10D', available: false };

    const availableClasses = (component as any).getSeatButtonClasses(availableSeat);
    const unavailableClasses = (component as any).getSeatButtonClasses(unavailableSeat);

    expect(availableClasses).toContain('hover:bg-indigo-50');
    expect(unavailableClasses).toContain('cursor-not-allowed');
  });
});

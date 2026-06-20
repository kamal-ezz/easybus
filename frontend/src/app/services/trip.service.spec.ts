import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { TripService, TripResponse } from './trip.service';
import { environment } from '../../environments/environment';

describe('TripService', () => {
  let service: TripService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [TripService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(TripService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('searchTrips should call correct endpoint with query params', () => {
    const mockTrips: TripResponse[] = [
      {
        id: 1,
        operator: 'CTM',
        equipments: ['AIR_CONDITIONER', 'WIFI'],
        fromCity: 'casablanca',
        toCity: 'rabat',
        date: '2026-02-15',
        departure: '08:00:00',
        arrival: '10:30:00',
        price: 120,
        availableSeats: ['1A', '1B', '2A', '2B'],
      },
    ];

    service.searchTrips('casablanca', 'rabat', '2026-02-15').subscribe((trips) => {
      expect(trips).toEqual(mockTrips);
      expect(trips.length).toBe(1);
    });

    const req = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=casablanca&destinationCity=rabat&date=2026-02-15`
    );
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('departureCity')).toBe('casablanca');
    expect(req.request.params.get('destinationCity')).toBe('rabat');
    expect(req.request.params.get('date')).toBe('2026-02-15');
    req.flush(mockTrips);
  });

  it('searchTrips should return empty array when no trips found', () => {
    service.searchTrips('agadir', 'fes', '2026-02-15').subscribe((trips) => {
      expect(trips).toEqual([]);
      expect(trips.length).toBe(0);
    });

    const req = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=agadir&destinationCity=fes&date=2026-02-15`
    );
    req.flush([]);
  });

  it('getTripById should call correct endpoint', () => {
    const mockTrip: TripResponse = {
      id: 1,
      operator: 'CTM',
      equipments: ['AIR_CONDITIONER'],
      fromCity: 'casablanca',
      toCity: 'rabat',
      date: '2026-02-15',
      departure: '08:00:00',
      arrival: '10:30:00',
      price: 120,
      availableSeats: ['1A', '1B'],
    };

    service.getTripById(1).subscribe((trip) => {
      expect(trip).toEqual(mockTrip);
      expect(trip.id).toBe(1);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/trips/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTrip);
  });

  it('getAvailableTrips should call correct endpoint', () => {
    const mockTrips: TripResponse[] = [
      {
        id: 1,
        operator: 'CTM',
        equipments: [],
        fromCity: 'casablanca',
        toCity: 'rabat',
        date: '2026-02-15',
        departure: '08:00:00',
        arrival: '10:30:00',
        price: 120,
        availableSeats: ['1A', '1B'],
      },
    ];

    service.getAvailableTrips().subscribe((trips) => {
      expect(trips.length).toBe(1);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/trips`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTrips);
  });
});

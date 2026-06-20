import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter, ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { Trips } from './trips';
import { environment } from '../../../environments/environment';

describe('Trips', () => {
  let component: Trips;
  let fixture: ComponentFixture<Trips>;
  let httpMock: HttpTestingController;

  const mockQueryParams = {
    from: 'casablanca',
    to: 'rabat',
    date: '2026-02-15',
    passengers: '2',
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Trips],
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

    fixture = TestBed.createComponent(Trips);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    // Flush the expected requests
    const citiesReq = httpMock.expectOne(`${environment.apiUrl}/cities`);
    citiesReq.flush([]);
    const tripsReq = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=casablanca&destinationCity=rabat&date=2026-02-15`
    );
    tripsReq.flush([]);

    expect(component).toBeTruthy();
  });

  it('should load trip results from API', () => {
    const mockTrips = [
      {
        id: 1,
        operator: 'CTM',
        equipments: ['AIR_CONDITIONER'],
        fromCity: 'casablanca',
        toCity: 'rabat',
        date: '2026-02-15',
        departure: '08:00:00',
        arrival: '10:30:00',
        price: 120,
        availableSeats: ['1A', '1B', '2A'],
      },
    ];

    const citiesReq = httpMock.expectOne(`${environment.apiUrl}/cities`);
    citiesReq.flush([
      { id: 'casablanca', name: 'Casablanca' },
      { id: 'rabat', name: 'Rabat' },
    ]);

    const tripsReq = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=casablanca&destinationCity=rabat&date=2026-02-15`
    );
    tripsReq.flush(mockTrips);

    fixture.detectChanges();

    const trips = (component as any).trips();
    expect(trips.length).toBe(1);
    expect(trips[0].operator).toBe('CTM');
    expect(trips[0].availableSeats).toBe(3);
  });

  it('should display loading state initially', () => {
    expect((component as any).loading()).toBe(true);

    const citiesReq = httpMock.expectOne(`${environment.apiUrl}/cities`);
    citiesReq.flush([]);
    const tripsReq = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=casablanca&destinationCity=rabat&date=2026-02-15`
    );
    tripsReq.flush([]);

    expect((component as any).loading()).toBe(false);
  });

  it('should format duration correctly', () => {
    const citiesReq = httpMock.expectOne(`${environment.apiUrl}/cities`);
    citiesReq.flush([]);
    const tripsReq = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=casablanca&destinationCity=rabat&date=2026-02-15`
    );
    tripsReq.flush([]);

    expect((component as any).formatDuration(90)).toBe('1h 30m');
    expect((component as any).formatDuration(60)).toBe('1h');
    expect((component as any).formatDuration(150)).toBe('2h 30m');
  });

  it('should calculate total price correctly', () => {
    const citiesReq = httpMock.expectOne(`${environment.apiUrl}/cities`);
    citiesReq.flush([]);
    const tripsReq = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=casablanca&destinationCity=rabat&date=2026-02-15`
    );
    tripsReq.flush([]);

    const trip = { pricePerSeat: 100 } as any;
    expect((component as any).totalPrice(trip)).toBe(200); // 2 passengers
  });

  it('should resolve city names from IDs', () => {
    const citiesReq = httpMock.expectOne(`${environment.apiUrl}/cities`);
    citiesReq.flush([
      { id: 'casablanca', name: 'Casablanca' },
      { id: 'rabat', name: 'Rabat' },
    ]);
    const tripsReq = httpMock.expectOne(
      `${environment.apiUrl}/trips/search?departureCity=casablanca&destinationCity=rabat&date=2026-02-15`
    );
    tripsReq.flush([]);

    fixture.detectChanges();

    expect((component as any).fromCity()).toBe('Casablanca');
    expect((component as any).toCity()).toBe('Rabat');
  });
});

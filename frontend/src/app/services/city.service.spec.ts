import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { CityService, City } from './city.service';
import { environment } from '../../environments/environment';

describe('CityService', () => {
  let service: CityService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CityService, provideHttpClient(), provideHttpClientTesting()],
    });
    service = TestBed.inject(CityService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('getCities should call correct endpoint', () => {
    const mockCities: City[] = [
      { id: 'casablanca', name: 'Casablanca' },
      { id: 'rabat', name: 'Rabat' },
    ];

    service.getCities().subscribe((cities) => {
      expect(cities).toEqual(mockCities);
      expect(cities.length).toBe(2);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/cities`);
    expect(req.request.method).toBe('GET');
    req.flush(mockCities);
  });

  it('getCities should cache the result', () => {
    const mockCities: City[] = [{ id: 'casablanca', name: 'Casablanca' }];

    // First call
    service.getCities().subscribe();
    const req1 = httpMock.expectOne(`${environment.apiUrl}/cities`);
    req1.flush(mockCities);

    // Second call - should use cache
    service.getCities().subscribe((cities) => {
      expect(cities).toEqual(mockCities);
    });

    // No additional request should be made
    httpMock.expectNone(`${environment.apiUrl}/cities`);
  });

  it('getCities should return cities array', () => {
    const mockCities: City[] = [
      { id: 'casablanca', name: 'Casablanca' },
      { id: 'rabat', name: 'Rabat' },
      { id: 'marrakech', name: 'Marrakech' },
    ];

    service.getCities().subscribe((cities) => {
      expect(Array.isArray(cities)).toBe(true);
      expect(cities[0].id).toBe('casablanca');
      expect(cities[0].name).toBe('Casablanca');
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/cities`);
    req.flush(mockCities);
  });
});

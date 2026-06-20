import { TestBed, ComponentFixture } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { Home } from './home';
import { environment } from '../../../environments/environment';

describe('Home', () => {
  let component: Home;
  let fixture: ComponentFixture<Home>;
  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Home],
      providers: [provideHttpClient(), provideHttpClientTesting(), provideRouter([])],
    }).compileComponents();

    fixture = TestBed.createComponent(Home);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create', () => {
    // Flush the cities request made in constructor
    const req = httpMock.expectOne(`${environment.apiUrl}/cities`);
    req.flush([]);
    expect(component).toBeTruthy();
  });

  it('should load cities on init', () => {
    const mockCities = [
      { id: 'casablanca', name: 'Casablanca' },
      { id: 'rabat', name: 'Rabat' },
    ];

    const req = httpMock.expectOne(`${environment.apiUrl}/cities`);
    req.flush(mockCities);

    fixture.detectChanges();
    expect((component as any).cities()).toEqual(mockCities);
  });

  it('should display search form elements', () => {
    const req = httpMock.expectOne(`${environment.apiUrl}/cities`);
    req.flush([{ id: 'casablanca', name: 'Casablanca' }]);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    // Check for form elements presence
    expect(compiled.querySelector('form') || compiled.querySelector('button')).toBeTruthy();
  });

  it('should filter available from cities when to city is selected', () => {
    const mockCities = [
      { id: 'casablanca', name: 'Casablanca' },
      { id: 'rabat', name: 'Rabat' },
      { id: 'marrakech', name: 'Marrakech' },
    ];

    const req = httpMock.expectOne(`${environment.apiUrl}/cities`);
    req.flush(mockCities);
    fixture.detectChanges();

    // Simulate selecting toCity
    (component as any).toCity.set({ id: 'rabat', name: 'Rabat' });
    fixture.detectChanges();

    const availableFrom = (component as any).availableFromCities();
    expect(availableFrom.length).toBe(2);
    expect(availableFrom.find((c: any) => c.id === 'rabat')).toBeUndefined();
  });

  it('should swap cities when swap is called', () => {
    const req = httpMock.expectOne(`${environment.apiUrl}/cities`);
    req.flush([]);
    fixture.detectChanges();

    const from = { id: 'casablanca', name: 'Casablanca' };
    const to = { id: 'rabat', name: 'Rabat' };

    (component as any).fromCity.set(from);
    (component as any).toCity.set(to);

    (component as any).swapCities();

    expect((component as any).fromCity()).toEqual(to);
    expect((component as any).toCity()).toEqual(from);
  });
});

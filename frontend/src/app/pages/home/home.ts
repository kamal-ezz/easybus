import { Component, computed, effect, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { DatePickerComponent } from '../../components/date-picker/date-picker.component';
import { AppearDirective } from '../../directives/appear.directive';
import { CityService, City } from '../../services/city.service';

const SEARCH_FORM_STORAGE_KEY = 'easybus_search_form';

@Component({
  selector: 'app-home',
  imports: [FormsModule, DatePickerComponent, AppearDirective],
  templateUrl: './home.html',
})
export class Home {
  private readonly cityService = inject(CityService);
  private readonly router = inject(Router);

  protected readonly cities = signal<City[]>([]);
  protected readonly fromCity = signal<City | null>(null);
  protected readonly toCity = signal<City | null>(null);
  protected readonly travelDate = signal('');
  protected readonly passengers = signal(1);

  protected readonly availableFromCities = computed(() =>
    this.cities().filter((city) => city.id !== this.toCity()?.id)
  );

  protected readonly availableToCities = computed(() =>
    this.cities().filter((city) => city.id !== this.fromCity()?.id)
  );

  protected readonly minDate = new Date().toISOString().split('T')[0];
  protected readonly searchAttempted = signal(false);

  protected readonly popularRoutes = [
    { from: 'Casablanca', to: 'Rabat', emoji: '🏙️' },
    { from: 'Marrakech', to: 'Fes', emoji: '🕌' },
    { from: 'Tangier', to: 'Casablanca', emoji: '⛴️' },
    { from: 'Agadir', to: 'Marrakech', emoji: '🏖️' },
    { from: 'Rabat', to: 'Meknes', emoji: '🏛️' },
    { from: 'Fes', to: 'Oujda', emoji: '🌄' },
  ];

  private searchFormRestored = false;

  constructor() {
    this.cityService.getCities().subscribe((cities) => {
      this.cities.set(cities);
      this.restoreSearchForm();
      this.searchFormRestored = true;
    });
    effect(() => {
      this.fromCity();
      this.toCity();
      this.travelDate();
      this.passengers();
      if (this.searchFormRestored) this.persistSearchForm();
    });
  }

  private restoreSearchForm(): void {
    if (typeof window === 'undefined' || !window.sessionStorage) return;
    try {
      const raw = window.sessionStorage.getItem(SEARCH_FORM_STORAGE_KEY);
      if (!raw) return;
      const data = JSON.parse(raw) as {
        fromId?: string;
        toId?: string;
        date?: string;
        passengers?: number;
      };
      const cities = this.cities();
      // Select uses [value]="city.id", so we restore id string so the option matches
      if (data.fromId && cities.some((c) => c.id === data.fromId)) {
        this.fromCity.set((data.fromId ?? null) as unknown as City | null);
      }
      if (data.toId && cities.some((c) => c.id === data.toId)) {
        this.toCity.set((data.toId ?? null) as unknown as City | null);
      }
      if (data.date != null) this.travelDate.set(data.date);
      if (data.passengers != null && data.passengers >= 1 && data.passengers <= 6) {
        this.passengers.set(data.passengers);
      }
    } catch {
      // ignore invalid stored data
    }
  }

  private persistSearchForm(): void {
    if (typeof window === 'undefined' || !window.sessionStorage) return;
    try {
      const from = this.fromCity();
      const to = this.toCity();
      const fromId = (from && typeof from === 'object' && 'id' in from ? (from as City).id : from) as string | null;
      const toId = (to && typeof to === 'object' && 'id' in to ? (to as City).id : to) as string | null;
      window.sessionStorage.setItem(
        SEARCH_FORM_STORAGE_KEY,
        JSON.stringify({
          fromId,
          toId,
          date: this.travelDate(),
          passengers: this.passengers(),
        })
      );
    } catch {
      // ignore quota / private mode
    }
  }

  protected swapCities() {
    const from = this.fromCity();
    this.fromCity.set(this.toCity());
    this.toCity.set(from);
  }

  protected searchTrips() {
    this.searchAttempted.set(true);
    if (!this.fromCity() || !this.toCity() || !this.travelDate()) {
      return;
    }

    this.router.navigate(['/trips'], {
      queryParams: {
        from: this.fromCity(),
        to: this.toCity(),
        date: this.travelDate(),
        passengers: this.passengers(),
      },
    });
  }

  protected selectPopularRoute(route: { from: string; to: string }): void {
    const cities = this.cities();
    const fromCity = cities.find((c) => c.name === route.from);
    const toCity = cities.find((c) => c.name === route.to);
    if (fromCity) this.fromCity.set(fromCity.id as unknown as City | null);
    if (toCity) this.toCity.set(toCity.id as unknown as City | null);
    // Scroll to search form
    document.querySelector('.search-form-card')?.scrollIntoView({ behavior: 'smooth' });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, shareReplay } from 'rxjs';
import { environment } from '../../environments/environment';

export interface City {
  id: string;
  name: string;
}

@Injectable({ providedIn: 'root' })
export class CityService {
  private citiesCache$: Observable<City[]> | null = null;

  constructor(private http: HttpClient) {}

  getCities(): Observable<City[]> {
    if (!this.citiesCache$) {
      this.citiesCache$ = this.http
        .get<City[]>(`${environment.apiUrl}/cities`)
        .pipe(shareReplay(1));
    }
    return this.citiesCache$;
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Trip } from '../models/trip.model';
import { environment } from 'src/environments/environment';
import { City } from '../models/city.model';

@Injectable({
  providedIn: 'root',
})
export class TripService {
  constructor(private http: HttpClient) {}

  getTrips() {
    return this.http.get<Trip[]>(`${environment.apiUrl}/api/v1/trips`);
  }

  searchTrips(departure: string, destination: string, date: string) {
    return this.http.get<Trip[]>(
      `${environment.apiUrl}/api/v1/trips/search?departureCity=${departure}&destinationCity=${destination}&date=${date}`
    );
  }

  getTripById(id: number) {
    return this.http.get<Trip>(`${environment.apiUrl}/api/v1/trips/${id}`);
  }

  getCities() {
    return this.http.get<City[]>(
      `${environment.apiUrl}/api/v1/trips/cities/all`
    );
  }
}

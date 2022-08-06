import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Trip } from '../models/trip.model';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TripService {
  constructor(private http: HttpClient) {}

  getTrips(): Observable<Trip[]> {
    return this.http.get<Trip[]>(`${environment.apiUrl}/api/v1/trips`);
  }

  searchTrips(departure: string, destination: string, date: string) {
    return this.http.get<Trip[]>(
      `${environment.apiUrl}/api/v1/trips/search?departureCity=${departure}&destinationCity=${destination}&date=${date}`
    );
  }

  getTripById(id: number): Observable<Trip> {
    return this.http.get<Trip>(`${environment.apiUrl}/api/v1/trips/${id}`);
  }
}

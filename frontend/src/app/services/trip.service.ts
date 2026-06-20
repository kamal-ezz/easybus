import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface TripResponse {
  id: number;
  operator: string;
  equipments: string[];
  fromCity: string;
  toCity: string;
  date: string;
  departure: string;
  arrival: string;
  price: number;
  availableSeats: string[];
}

@Injectable({ providedIn: 'root' })
export class TripService {
  constructor(private http: HttpClient) {}

  searchTrips(
    departureCity: string,
    destinationCity: string,
    date: string
  ): Observable<TripResponse[]> {
    const params = new HttpParams()
      .set('departureCity', departureCity)
      .set('destinationCity', destinationCity)
      .set('date', date);

    return this.http.get<TripResponse[]>(
      `${environment.apiUrl}/trips/search`,
      { params }
    );
  }

  getTripById(id: number): Observable<TripResponse> {
    return this.http.get<TripResponse>(`${environment.apiUrl}/trips/${id}`);
  }

  getAvailableTrips(): Observable<TripResponse[]> {
    return this.http.get<TripResponse[]>(`${environment.apiUrl}/trips`);
  }
}

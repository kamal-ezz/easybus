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
    return this.http.get<Trip[]>(`${environment.url}/api/v1/trips`);
  }
}

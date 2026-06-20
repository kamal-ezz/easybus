import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface BookingResponse {
  id: number;
  tripId: number | null;
  seat: string;
  fullName: string;
  email: string;
  phone: string;
  amount: number;
  currency: string;
  paypalOrderId: string;
  paymentMethod: string;
  bookingStatus: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class BookingService {
  constructor(private http: HttpClient) {}

  getMyBookings(): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(`${environment.apiUrl}/bookings/my`);
  }
}

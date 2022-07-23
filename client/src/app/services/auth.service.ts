import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../models/user.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<User> {
    return this.http.post<any>(
      environment.url + '/api/v1/auth/login',
      {
        email,
        password,
      },
      httpOptions
    );
  }

  register(
    firstName: string,
    lastName: string,
    email: string,
    password: string,
    phone: string
  ): Observable<any> {
    return this.http.post(
      environment.url + '/auth/signup',
      {
        firstName,
        lastName,
        email,
        password,
        phone,
      },
      httpOptions
    );
  }

  logout() {}

  isLoggedIn() {}

  isLoggedOut() {}
}

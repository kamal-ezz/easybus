import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../models/user.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
};

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private token!: string | null;
  //private loggedInUsername!: string | null;

  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
    return this.http.post(
      environment.url + '/api/v1/auth/login',
      {
        email,
        password,
      },
      httpOptions
    );
  }

  register(user: User) {
    return this.http.post(
      environment.url + '/api/v1/auth/signup',
      user,
      httpOptions
    );
  }

  logout() {
    this.token = null;
    //this.loggedInUsername = null;
    localStorage.removeItem('user');
    localStorage.removeItem('token');
    localStorage.removeItem('users');
  }

  saveToken(token: string) {
    this.token = token;
    localStorage.setItem('token', token);
  }

  addUserToLocalCache(user: User) {
    localStorage.setItem('user', JSON.stringify(user));
  }

  getUserFromLocalCache() {
    return JSON.parse(localStorage.getItem('user') || '');
  }

  loadToken() {
    this.token = localStorage.getItem('token');
  }

  getToken() {
    return this.token;
  }
}

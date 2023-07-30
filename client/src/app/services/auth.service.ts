import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, Observable } from 'rxjs';
import { User } from '../models/user.model';
import { environment } from 'src/environments/environment';
import { JwtHelperService } from '@auth0/angular-jwt';

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
      environment.apiUrl + '/api/v1/auth/login',
      {
        email,
        password,
      },
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
    const user = localStorage.getItem('user');
    if (user) return JSON.parse(user);
  }

  /*loadToken() {
    this.token = localStorage.getItem('token');
  }

  getToken() {
    return this.token;
  }*/

  isLoggedIn() {
    const token = localStorage.getItem('token');
    if (!token) return false;
    const isExpired = new JwtHelperService().isTokenExpired(token);
    if (!isExpired) return true;

    return false;
  }
}

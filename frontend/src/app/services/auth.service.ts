import { Injectable, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';

export interface User {
  fullName: string;
  email: string;
  username: string;
  avatarUrl: string | null;
  locale: string | null;
  emailVerified: boolean;
  role: string;
}

interface LoginResponse extends User {
  token: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly user = signal<User | null>(null);
  private readonly token = signal<string | null>(null);
  private readonly loading = signal(false);

  readonly currentUser = this.user.asReadonly();
  readonly isAuthenticated = computed(() => !!this.user());
  readonly isLoading = this.loading.asReadonly();

  constructor(private http: HttpClient) {
    this.loadUserFromStorage();
  }

  signInWithGoogle(): void {
    // Redirect to backend OAuth endpoint
    window.location.href = `${environment.apiUrl}/auth/google/redirect`;
  }

  // Called from the callback route after OAuth redirect
  handleAuthCallback(token: string, userJson: string): boolean {
    try {
      const user = JSON.parse(userJson) as LoginResponse;
      this.setSession(token, user);
      return true;
    } catch (e) {
      console.error('Failed to parse auth callback data:', e);
      return false;
    }
  }

  private setSession(token: string, response: LoginResponse): void {
    const { token: _, ...user } = response;
    this.user.set(user);
    this.token.set(token);
    localStorage.setItem('auth_token', token);
    localStorage.setItem('auth_user', JSON.stringify(user));
  }

  private loadUserFromStorage(): void {
    const token = localStorage.getItem('auth_token');
    const userJson = localStorage.getItem('auth_user');
    if (token && userJson) {
      try {
        this.token.set(token);
        this.user.set(JSON.parse(userJson));
      } catch {
        this.logout();
      }
    }
  }

  getToken(): string | null {
    return this.token();
  }

  logout(): void {
    this.user.set(null);
    this.token.set(null);
    localStorage.removeItem('auth_token');
    localStorage.removeItem('auth_user');
  }
}

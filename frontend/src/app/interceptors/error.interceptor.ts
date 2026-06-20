import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';
import { ToastService } from '../services/toast.service';
import { Router } from '@angular/router';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const toast = inject(ToastService);
  const router = inject(Router);

  return next(req).pipe(
    catchError((error) => {
      if (error.status === 401) {
        authService.logout();
        router.navigate(['/']);
        toast.error('Session expired. Please sign in again.');
      } else if (error.status >= 500) {
        toast.error('Server error. Please try again later.');
      } else if (error.status === 0) {
        toast.error('Network error. Please check your connection.');
      }
      return throwError(() => error);
    })
  );
};

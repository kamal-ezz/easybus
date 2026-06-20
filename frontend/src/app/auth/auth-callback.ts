import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-auth-callback',
  template: `
    <div class="min-h-screen flex items-center justify-center bg-white dark:bg-gray-900">
      <div class="text-center">
        @if (error) {
          <div class="text-red-600 dark:text-red-400 mb-4">
            <svg class="w-16 h-16 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
            <p class="text-lg font-medium">Authentication failed</p>
            <p class="text-sm text-gray-500 dark:text-gray-400 mt-2">{{ error }}</p>
          </div>
          <a href="/" class="text-indigo-600 dark:text-indigo-400 hover:text-indigo-800 dark:hover:text-indigo-300">Return to home</a>
        } @else {
          <div class="animate-spin w-12 h-12 border-4 border-indigo-600 dark:border-indigo-400 border-t-transparent rounded-full mx-auto mb-4"></div>
          <p class="text-gray-600 dark:text-gray-300">Signing you in...</p>
        }
      </div>
    </div>
  `,
})
export class AuthCallback implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private authService = inject(AuthService);

  error: string | null = null;

  ngOnInit(): void {
    const params = this.route.snapshot.queryParams;
    const token = params['token'];
    const user = params['user'];
    const errorMsg = params['message'];

    if (errorMsg) {
      this.error = errorMsg;
      return;
    }

    if (token && user) {
      const success = this.authService.handleAuthCallback(token, user);
      if (success) {
        this.router.navigate(['/']);
      } else {
        this.error = 'Failed to process authentication data';
      }
    } else {
      this.error = 'Missing authentication data';
    }
  }
}

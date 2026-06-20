import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    title: 'EasyBus - Bus Tickets Across Morocco',
    loadComponent: () => import('./pages/home/home').then((m) => m.Home),
  },
  {
    path: 'trips',
    title: 'Available Trips - EasyBus',
    loadComponent: () => import('./pages/trips/trips').then((m) => m.Trips),
  },
  {
    path: 'booking',
    title: 'Complete Your Booking - EasyBus',
    loadComponent: () => import('./pages/booking/booking').then((m) => m.Booking),
  },
  {
    path: 'about',
    title: 'About Us - EasyBus',
    loadComponent: () => import('./pages/about/about').then((m) => m.About),
  },
  {
    path: 'contact',
    title: 'Contact Us - EasyBus',
    loadComponent: () => import('./pages/contact/contact').then((m) => m.Contact),
  },
  {
    path: 'profile',
    title: 'My Profile - EasyBus',
    loadComponent: () => import('./pages/profile/profile').then((m) => m.Profile),
  },
  {
    path: 'bookings',
    title: 'My Bookings - EasyBus',
    loadComponent: () => import('./pages/bookings/bookings').then((m) => m.Bookings),
  },
  {
    path: 'auth/callback',
    title: 'Signing In - EasyBus',
    loadComponent: () => import('./auth/auth-callback').then((m) => m.AuthCallback),
  },
  {
    path: 'auth/error',
    title: 'Authentication Error - EasyBus',
    loadComponent: () => import('./auth/auth-callback').then((m) => m.AuthCallback),
  },
];

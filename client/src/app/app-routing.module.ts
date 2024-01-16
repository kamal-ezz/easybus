import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/admin/login/login.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { TripsComponent } from './components/trips/trips.component';
import { AdminHomeComponent } from './components/admin/admin-home/admin-home.component';
import { DashboardTripsComponent } from './components/admin/dashboard-trips/dashboard-trips.component';
import { DashboardBusesComponent } from './components/admin/dashboard-buses/dashboard-buses.component';
import { DashboardReservationsComponent } from './components/admin/dashboard-reservations/dashboard-reservations.component';
import { AddTripComponent } from './components/admin/add-trip/add-trip.component';
import { ContactInfoComponent } from './components/contact-info/contact-info.component';
import { ReservationSummaryComponent } from './components/reservation-summary/reservation-summary.component';

const routes: Routes = [
  {
    path: 'admin',
    component: AdminHomeComponent,
    children: [
      {
        path: 'trips',
        component: DashboardTripsComponent,
      },
      {
        path: 'buses',
        component: DashboardBusesComponent,
      },
      {
        path: 'reservations',
        component: DashboardReservationsComponent,
      },
      {
        path: 'add-trip',
        component: AddTripComponent,
      },
    ],
  },
  { path: 'login', component: LoginComponent },
  {
    path: 'trips',
    component: TripsComponent,
  },
  { path: 'contact-info', component: ContactInfoComponent },
  { path: 'reservation-summary', component: ReservationSummaryComponent },
  { path: '', component: HomeComponent },
  { path: '**', component: NotFoundComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}

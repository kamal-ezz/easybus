import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { TripsComponent } from './components/trips/trips.component';
import { TripComponent } from './components/trip/trip.component';
import { HeaderComponent } from './components/header/header.component';
import { LoginComponent } from './components/admin/login/login.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './components/home/home.component';
import { NotFoundComponent } from './components/not-found/not-found.component';
import { FooterComponent } from './components/footer/footer.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ChooseSeatComponent } from './components/choose-seat/choose-seat.component';
import { ContactInfoComponent } from './components/contact-info/contact-info.component';
import { ReservationSummaryComponent } from './components/reservation-summary/reservation-summary.component';
import { TicketComponent } from './components/ticket/ticket.component';
import { AddTripComponent } from './components/admin/add-trip/add-trip.component';
import { AddBusComponent } from './components/admin/add-bus/add-bus.component';
import { ReservationsComponent } from './components/admin/reservations/reservations.component';
import { SidebarComponent } from './components/admin/sidebar/sidebar.component';

@NgModule({
  declarations: [
    AppComponent,
    TripsComponent,
    TripComponent,
    HeaderComponent,
    LoginComponent,
    HomeComponent,
    NotFoundComponent,
    FooterComponent,
    ProfileComponent,
    ChooseSeatComponent,
    ContactInfoComponent,
    ReservationSummaryComponent,
    TicketComponent,
    AddTripComponent,
    AddBusComponent,
    ReservationsComponent,
    SidebarComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    FormsModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}

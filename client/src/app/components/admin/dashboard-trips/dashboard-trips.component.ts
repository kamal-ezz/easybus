import { Component, OnInit } from '@angular/core';
import { Trip } from 'src/app/models/trip.model';
import { TripService } from 'src/app/services/trip.service';

@Component({
  selector: 'app-dashboard-trips',
  templateUrl: './dashboard-trips.component.html',
  styleUrls: ['./dashboard-trips.component.css'],
})
export class DashboardTripsComponent implements OnInit {
  trips: Trip[] = [];

  constructor(private tripService: TripService) {}

  ngOnInit(): void {
    this.tripService.getTrips().subscribe((data) => (this.trips = data));
  }
}

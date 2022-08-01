import { Component, OnInit } from '@angular/core';
import { Trip } from 'src/app/models/trip.model';
import { TripService } from 'src/app/services/trip.service';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.css'],
})
export class TripsComponent implements OnInit {
  trips: Trip[] = [];

  constructor(private tripService: TripService) {}

  ngOnInit() {
    this.tripService.getTrips().subscribe({
      next: (data) => {
        this.trips = data;
        console.log(this.trips);
        this.trips.map(
          (trip) =>
            (trip.duration = this.calculateDuration(
              trip.departureTime,
              trip.destinationTime
            ))
        );
      },
    });
  }

  calculateDuration(departureTime: string, destinationTime: string) {
    const departureTimeInMins =
      parseInt(departureTime.substring(0, 2)) * 60 +
      parseInt(departureTime.substring(3, 5));
    const destinationTimeInMins =
      parseInt(destinationTime.substring(0, 2)) * 60 +
      parseInt(destinationTime.substring(3, 5));
    const durationInMins = destinationTimeInMins - departureTimeInMins;
    const duration =
      Math.floor(durationInMins / 60).toString() +
      ':' +
      (durationInMins % 60).toString();
    return duration;
  }
}

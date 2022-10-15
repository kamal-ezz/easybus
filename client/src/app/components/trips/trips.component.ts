import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Trip } from 'src/app/models/trip.model';
import { TripService } from 'src/app/services/trip.service';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.css'],
})
export class TripsComponent implements OnInit {
  trips: Trip[] = [];
  price: number = 20;
  departureTimes: string[] = ['morning', 'afternoon', 'evening'];
  equipments: string[] = [];

  constructor(
    private tripService: TripService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const departure = this.route.snapshot.queryParamMap.get('departureCity');
    const destination =
      this.route.snapshot.queryParamMap.get('destinationCity');
    const date = this.route.snapshot.queryParamMap.get('date');

    if (departure && destination && date) {
      this.tripService.searchTrips(departure, destination, date).subscribe({
        next: (data) => {
          this.trips = (data as any).content;
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

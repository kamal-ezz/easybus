import { Component, Input, OnInit } from '@angular/core';
import { Trip } from 'src/app/models/trip.model';

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.css'],
})
export class TripComponent implements OnInit {
  @Input()
  trip!: Trip;

  showSeats = true;

  constructor() {}

  ngOnInit() {
    console.log(this.trip.bus.company);
  }

  toggleSeats() {
    this.showSeats = !this.showSeats;
  }
}

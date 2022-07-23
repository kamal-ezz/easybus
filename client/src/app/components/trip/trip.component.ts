import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.css']
})
export class TripComponent implements OnInit {

  @Input()
  trip: any

  showSeats = true 

  constructor() { }

  ngOnInit(): void {
  }

  toggleSeats(){
    this.showSeats = !this.showSeats;
  }



}

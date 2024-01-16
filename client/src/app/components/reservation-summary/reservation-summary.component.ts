import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { TripService } from 'src/app/services/trip.service';
import { Ticket } from 'src/app/models/ticket.model';

@Component({
  selector: 'app-reservation-summary',
  templateUrl: './reservation-summary.component.html',
  styleUrls: ['./reservation-summary.component.css'],
})
export class ReservationSummaryComponent implements OnInit {
  constructor(
    private tripService: TripService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {}

  get departure() {
    return this.route.snapshot.paramMap.get('departure');
  }

  get destination() {
    return this.route.snapshot.paramMap.get('destination');
  }

  get price() {
    return this.route.snapshot.paramMap.get('totalPrice');
  }

  get seats() {
    return this.route.snapshot.paramMap.get('seats');
  }

  get firstName() {
    return this.route.snapshot.paramMap.get('firstName');
  }

  get lastName() {
    return this.route.snapshot.paramMap.get('lastName');
  }

  get email() {
    return this.route.snapshot.paramMap.get('email');
  }

  generateTicket() {
    const ticketData: Ticket = {
      lastName: this.lastName,
      firstName: this.firstName,
      price: this.price,
      seatNumber: this.seats,
      departure: this.departure,
      destination: this.destination,
    };

    this.tripService.generateTicket(ticketData).subscribe();
  }
}

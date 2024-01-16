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

  generateTicket() {
    let departure = this.route.snapshot.paramMap.get('departure');
    let destination = this.route.snapshot.paramMap.get('destination');
    let price = this.route.snapshot.paramMap.get('totalPrice');
    let seats = this.route.snapshot.paramMap.get('seats');
    let firstName = this.route.snapshot.paramMap.get('firstName');
    let lastName = this.route.snapshot.paramMap.get('lastName');
    let email = this.route.snapshot.paramMap.get('email');

    const ticketData: Ticket = {
      lastName: lastName,
      firstName: firstName,
      price: price,
      seatNumber: seats,
      departure: departure,
      destination: destination,
    };

    this.tripService.generateTicket(ticketData).subscribe();
  }
}

import { Component, OnInit } from '@angular/core';
import { Trip } from 'src/app/models/trip.model';
import { TripService } from 'src/app/services/trip.service';

@Component({
  selector: 'app-trips',
  templateUrl: './trips.component.html',
  styleUrls: ['./trips.component.css'],
})
export class TripsComponent implements OnInit {
  ghazala = {
    id: 1,
    company: 'trans ghazala',
    logo: '',
    equipments: [],
  };

  trips = [
    {
      id: 1,
      bus: this.ghazala,
      departureCity: 'Marrakech',
      destinationCity: 'Rabat',
      date: new Date(),
      departureTime: '14:00',
      destinationTime: '18:00',
      price: 90,
      isAvailable: true,
    },
    {
      id: 2,
      bus: this.ghazala,
      departureCity: 'Casablanca',
      destinationCity: 'Rabat',
      date: new Date(),
      departureTime: '15:00',
      destinationTime: '16:00',
      price: 50,
      isAvailable: true,
    },
    {
      id: 3,
      bus: this.ghazala,
      departureCity: 'Safi',
      destinationCity: 'Kelaa de Sraghna',
      date: new Date(),
      departureTime: '17:00',
      destinationTime: '20:00',
      price: 100,
      isAvailable: false,
    },
    {
      id: 4,
      bus: this.ghazala,
      departureCity: 'Tanger',
      destinationCity: 'Fes',
      date: new Date(),
      departureTime: '14:00',
      destinationTime: '19:00',
      price: 150,
      isAvailable: true,
    },
  ];

  constructor(private tripService: TripService) {}

  ngOnInit(): void {}
}

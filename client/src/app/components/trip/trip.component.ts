import { Component, Input, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Trip } from 'src/app/models/trip.model';
import { AuthService } from 'src/app/services/auth.service';
import { TripService } from 'src/app/services/trip.service';

@Component({
  selector: 'app-trip',
  templateUrl: './trip.component.html',
  styleUrls: ['./trip.component.css'],
})
export class TripComponent implements OnInit {
  @Input()
  trip!: Trip;
  seatStatus: boolean[] = new Array(36).fill(false);
  form!: FormGroup;
  selectedSeats: number[] = [];
  submitted: boolean = false;
  //showSeats = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tripService: TripService,
    private authService: AuthService
  ) {}

  ngOnInit() {
    //console.log(this.trip.bus.company);
    /*if (this.route.snapshot.paramMap.get('id')) {
      this.tripId = this.route.snapshot.paramMap.get('id');
    }*/

    this.form = new FormGroup({
      seats: new FormArray([]),
    });

    this.tripService.getTripById(+this.trip.id).subscribe({
      next: (data) => {
        this.trip = data;
        console.log(this.trip);
      },
    });

    this.seatStatus.forEach(() => {
      this.seats.push(new FormControl(false));
    });

    if (this.seats) {
      this.seats.valueChanges.subscribe((selectedValue) => {
        this.selectedSeats = [];
        for (var i = 0; i < 36; i++) {
          if (selectedValue[i]) this.selectedSeats.push(i);
        }
      });
    }
  }

  /*toggleSeats() {
    this.showSeats = !this.showSeats;
  }*/

  get seats() {
    return this.form.get('seats') as FormArray;
  }

  logged() {
    return this.authService.isLoggedIn();
  }

  submit() {
    console.log(this.form);
    this.submitted = true;
    if (this.selectedSeats.length) {
      let price: number = this.trip.price * this.selectedSeats.length;
      this.router.navigate(['passengerInfo']);
    } else return;
  }
}

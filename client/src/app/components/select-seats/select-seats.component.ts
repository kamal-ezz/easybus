import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';
import { Trip } from 'src/app/models/trip.model';
import { TripService } from 'src/app/services/trip.service';

@Component({
  selector: 'app-select-seats',
  templateUrl: './select-seats.component.html',
  styleUrls: ['./select-seats.component.css'],
})
export class SelectSeatsComponent implements OnInit {
  tripId!: any;
  trip!: Trip;
  seatStatus: boolean[] = new Array(36).fill(false);
  form!: FormGroup;
  selectedSeats: number[] = [];
  submitted: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tripService: TripService
  ) {}

  ngOnInit(): void {
    if (this.route.snapshot.paramMap.get('id')) {
      this.tripId = this.route.snapshot.paramMap.get('id');
    }

    this.form = new FormGroup({
      seats: new FormArray([]),
    });

    this.tripService.getTripById(+this.tripId).subscribe({
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

  get seats() {
    return this.form.get('seats') as FormArray;
  }

  submit() {
    console.log(this.form);
    this.submitted = true;
    if (this.selectedSeats.length) {
      //@ts-ignore
      let price = this.trip.price * this.selectedSeats.length;
      this.router.navigate(['passengerInfo']);
    } else return;
  }
}

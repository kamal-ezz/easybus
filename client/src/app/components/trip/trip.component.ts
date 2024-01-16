import { Component, Input, OnInit } from '@angular/core';
import {
  UntypedFormArray,
  UntypedFormControl,
  UntypedFormGroup,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
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
  form!: UntypedFormGroup;
  selectedSeats: number[] = [];
  submitted: boolean = false;
  isLoggedIn: boolean = false;
  //showSeats = true;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private tripService: TripService,
    private authService: AuthService,
    private modalService: NgbModal
  ) {}

  ngOnInit() {
    //console.log(this.trip.bus.company);
    /*if (this.route.snapshot.paramMap.get('id')) {
      this.tripId = this.route.snapshot.paramMap.get('id');
    }*/

    this.form = new UntypedFormGroup({
      seats: new UntypedFormArray([]),
    });

    this.tripService.getTripById(this.trip.id).subscribe({
      next: (data) => {
        this.trip = data;
        console.log(this.trip);
      },
    });

    this.seatStatus.forEach(() => {
      this.seats.push(new UntypedFormControl(false));
    });

    if (this.seats) {
      this.seats.valueChanges.subscribe((selectedValue) => {
        this.selectedSeats = [];
        for (var i = 0; i < 36; i++) {
          if (selectedValue[i]) this.selectedSeats.push(i);
        }
      });
    }

    this.isLoggedIn = this.authService.isLoggedIn();
  }

  /*toggleSeats() {
    this.showSeats = !this.showSeats;
  }*/

  get seats() {
    return this.form.get('seats') as UntypedFormArray;
  }

  submit() {
    console.log(this.form);
    this.submitted = true;

    if (this.selectedSeats.length === 0) {
      return; // Don't proceed if no seats are selected
    }

    const totalPrice = this.trip.price * this.selectedSeats.length;
    this.modalService.dismissAll();
    this.router.navigate(['contact-info'], {
      queryParams: {
        totalPrice: totalPrice,
        departure: this.trip.departureCity,
        destination: this.trip.destinationCity,
        seats: '',
      },
    });
  }

  openModal(content: any) {
    this.modalService.open(content, {
      ariaLabelledBy: 'modal-basic-title',
      size: 'lg',
    });
  }
}

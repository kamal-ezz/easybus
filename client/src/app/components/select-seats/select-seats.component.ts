import { Component, OnInit } from '@angular/core';
import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router } from '@angular/router';

@Component({
  selector: 'app-select-seats',
  templateUrl: './select-seats.component.html',
  styleUrls: ['./select-seats.component.css'],
})
export class SelectSeatsComponent implements OnInit {
  busId: any;
  busDetails: any;
  trip: any;
  seatStatus: boolean[] = new Array(36).fill(false);
  form: FormGroup = new FormGroup({
    seats: new FormArray([]),
  });
  selectedSeats: number[] = [];
  submitted: boolean = false;

  constructor(private route: ActivatedRoute, private router: Router) {}

  get seats() {
    return this.form.get('seats') as FormArray;
  }

  submit() {
    console.log(this.form);
    this.submitted = true;
    if (this.selectedSeats.length) {
      //@ts-ignore
      let price = this.busDetails['Price'] * this.selectedSeats.length;
      this.router.navigate(['passengerInfo']);
    } else return;
  }

  ngOnInit(): void {}
}

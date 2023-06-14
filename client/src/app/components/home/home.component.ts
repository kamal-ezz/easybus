import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { City } from 'src/app/models/city.model';
import { TripService } from 'src/app/services/trip.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  tripInfo!: FormGroup;
  submitted: boolean = false;
  cities: City[] = [];
  todayDate = new Date().toJSON().slice(0, 10);

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private tripService: TripService
  ) {}

  ngOnInit(): void {
    this.tripInfo = this.formBuilder.group({
      departure: ['', Validators.required],
      destination: ['', Validators.required],
      date: ['', Validators.required],
    });

    this.tripService.getCities().subscribe((data) => (this.cities = data));
  }

  get departure() {
    return this.tripInfo.get('departure');
  }

  get destination() {
    return this.tripInfo.get('destination');
  }

  get date() {
    return this.tripInfo.get('date');
  }

  onSubmit() {
    this.submitted = true;
    console.log(this.tripInfo.value);
    if (this.tripInfo.invalid) return;
    this.router.navigateByUrl(
      `trips?departureCity=${this.departure?.value}&destinationCity=${this.destination?.value}&date=${this.date?.value}`
    );
  }
}

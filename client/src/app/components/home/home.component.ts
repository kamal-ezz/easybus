import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  tripInfo!: FormGroup;
  submitted: boolean = false;
  departureCities: string[] = ['Marrakech', 'Casablanca', 'Rabat'];
  destinationCities: string[] = ['Marrakech', 'Casablanca', 'Rabat'];
  todayDate = new Date().toJSON().slice(0, 10);

  constructor(private formBuilder: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    this.tripInfo = this.formBuilder.group({
      departure: ['', Validators.required],
      destination: ['', Validators.required],
      date: ['', Validators.required],
    });
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

  /*excludeDepartureCity(departureCities: string[]) {
    return departureCities.filter((i) => i != this.departure?.value);
  }*/

  onSubmit() {
    this.submitted = true;
    console.log(this.tripInfo.value);
    if (this.tripInfo.invalid) return;
    this.router.navigateByUrl(
      `trips?departureCity=${this.departure?.value}&destinationCity=${this.destination?.value}&date=${this.date?.value}`
    );
  }
}

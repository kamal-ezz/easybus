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
  sourceCities: string[] = ['Marrakech', 'Casablanca', 'Rabat'];
  destinationCities: string[] = ['Marrakech', 'Casablanca', 'Rabat'];

  constructor(private formBuilder: FormBuilder, private router: Router) {}

  ngOnInit(): void {
    this.tripInfo = this.formBuilder.group({
      departureCity: ['', Validators.required],
      destinationCity: ['', Validators.required],
      departureDate: ['', Validators.required],
    });
  }

  get formControl() {
    return this.tripInfo.controls;
  }

  onSubmit() {
    this.submitted = true;
    console.log(this.tripInfo.value);
    if (this.tripInfo.invalid) return;
    this.router.navigate(['trips']);
  }
}

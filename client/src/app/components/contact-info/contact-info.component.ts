import { Component, OnInit } from '@angular/core';
import {
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-contact-info',
  templateUrl: './contact-info.component.html',
  styleUrls: ['./contact-info.component.css'],
})
export class ContactInfoComponent implements OnInit {
  form!: UntypedFormGroup;
  error = '';

  constructor(
    private fb: UntypedFormBuilder,
    private activatedRoute: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required, Validators.email],
      phone: ['', Validators.required],
    });
  }

  submitContactInfo() {}

  get firstName() {
    return this.form.get('firstName');
  }

  get lastName() {
    return this.form.get('lastName');
  }

  get email() {
    return this.form.get('email');
  }

  get phone() {
    return this.form.get('phone');
  }

  checkout() {
    let departure = this.activatedRoute.snapshot.paramMap.get('departure');
    let destination = this.activatedRoute.snapshot.paramMap.get('destination');
    let price = this.activatedRoute.snapshot.paramMap.get('totalPrice');
    let seats = this.activatedRoute.snapshot.paramMap.get('seats');

    this.router.navigate(['reservation-summary'], {
      queryParams: {
        price: price,
        departure: departure,
        destination: destination,
        seats: seats,
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
      },
    });
  }
}

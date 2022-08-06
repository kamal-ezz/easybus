import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent implements OnInit {
  form!: FormGroup;

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', Validators.required],
      password: ['', Validators.required],
      phone: ['', Validators.required],
    });

    if (this.authService.isLoggedIn()) this.router.navigateByUrl('/');
  }

  get email() {
    return this.form.get('email');
  }

  get password() {
    return this.form.get('password');
  }

  get firstName() {
    return this.form.get('firstName');
  }

  get lastName() {
    return this.form.get('lastName');
  }

  get phone() {
    return this.form.get('phone');
  }

  handleRegister() {
    this.authService
      .register(
        this.firstName?.value,
        this.lastName?.value,
        this.email?.value,
        this.password?.value,
        this.phone?.value
      )
      .subscribe({
        next: (data) => {
          const user = data as User;
          //this.authService.login(user.email, user.password);
          this.router.navigateByUrl('/login');
        },
        error: (err) => {
          this.form.setErrors({
            invalidRegister: true,
          });

          //this.errors.push(err.error.error);
        },
      });
  }
}

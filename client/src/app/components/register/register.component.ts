import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
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
  }

  handleRegister() {
    const val = this.form.value;

    if (val.any) {
      this.authService
        .register(
          val.firstName,
          val.lastName,
          val.email,
          val.password,
          val.phone
        )
        .subscribe((data) => {
          if (data.token) localStorage.setItem('token', data.token);
          this.router.navigateByUrl('/');
        });
    }
  }
}

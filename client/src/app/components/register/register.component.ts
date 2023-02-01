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
  user!: User;

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



  handleRegister() {
    this.authService.register({
        firstName: this.form.get('firstName')?.value,
        lastName: this.form.get('lastName')?.value,
        email: this.form.get('email')?.value,
        password: this.form.get('password')?.value,
        phone: this.form.get('phone')?.value,
      })
                    .subscribe({
                      next: (data) => {
                        const user = data as User
                        this.authService.saveToken(user.token || '');
                        this.authService.addUserToLocalCache(user);
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

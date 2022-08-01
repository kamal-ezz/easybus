import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { User } from 'src/app/models/user.model';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  form!: FormGroup;
  errors: any = [];

  constructor(
    private authService: AuthService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.form = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required],
    });
  }

  get email() {
    return this.form.get('email');
  }

  get password() {
    return this.form.get('password');
  }

  handleLogin() {
    this.authService.login(this.email?.value, this.password?.value).subscribe({
      next: (data) => {
        const user = data as User;
        this.authService.saveToken(user.token || '');
        this.authService.addUserToLocalCache(user);
        this.router.navigateByUrl('/');
      },
      error: (err) => {
        this.form.setErrors({
          invalidLogin: true,
        });

        //this.errors.push(err.error.error);
      },
    });
  }

  reloadPage() {
    window.location.reload();
  }
}

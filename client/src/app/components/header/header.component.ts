import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  isCollapsed = false;
  userName = '';

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    if (this.isLoggedIn())
      this.userName =
        this.authService.getUserFromLocalCache().firstName +
        ' ' +
        this.authService.getUserFromLocalCache().lastName;
  }

  isLoggedIn() {
    return this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout();
  }
}

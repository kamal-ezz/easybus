import { Component, inject, signal } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-navbar',
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
})
export class Navbar {
  protected readonly authService = inject(AuthService);
  protected readonly themeService = inject(ThemeService);
  protected readonly languages = [
    { code: 'en', label: 'English' },
    { code: 'fr', label: 'Fran\u00e7ais' },
  ];
  protected readonly currentLang = signal('en');
  protected readonly isLangMenuOpen = signal(false);
  protected readonly isUserMenuOpen = signal(false);
  protected readonly isMobileMenuOpen = signal(false);

  protected toggleLangMenu() {
    this.isLangMenuOpen.update((v) => !v);
    this.isUserMenuOpen.set(false);
  }

  protected toggleUserMenu() {
    this.isUserMenuOpen.update((v) => !v);
    this.isLangMenuOpen.set(false);
  }

  protected toggleMobileMenu() {
    this.isMobileMenuOpen.update((v) => !v);
  }

  protected toggleTheme() {
    this.themeService.toggleTheme();
  }

  protected selectLanguage(code: string) {
    this.currentLang.set(code);
    this.isLangMenuOpen.set(false);
  }

  protected getCurrentLangLabel() {
    return this.languages.find((l) => l.code === this.currentLang())?.label ?? 'English';
  }

  protected signInWithGoogle() {
    this.authService.signInWithGoogle();
  }

  protected logout() {
    this.authService.logout();
    this.isUserMenuOpen.set(false);
    this.isMobileMenuOpen.set(false);
  }
}

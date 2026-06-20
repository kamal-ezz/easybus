import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './contact.html',
})
export class Contact {
  protected readonly name = signal('');
  protected readonly email = signal('');
  protected readonly message = signal('');
  protected readonly submitted = signal(false);

  protected submitForm(): void {
    if (!this.name().trim() || !this.email().trim() || !this.message().trim()) return;
    // In a real app this would call an API
    this.submitted.set(true);
  }
}

import { Component, input, output } from '@angular/core';

export interface Step {
  label: string;
  icon: string;
}

@Component({
  selector: 'app-stepper',
  standalone: true,
  template: `
    <nav class="flex items-center justify-between mb-8" aria-label="Booking progress">
      @for (step of steps(); track step.label; let i = $index) {
        <div class="flex items-center" [class.flex-1]="i < steps().length - 1">
          <button
            type="button"
            (click)="stepClick.emit(i)"
            [disabled]="i > currentStep()"
            class="flex items-center gap-2 shrink-0"
            [attr.aria-current]="i === currentStep() ? 'step' : null"
          >
            <div class="w-9 h-9 rounded-full flex items-center justify-center text-sm font-medium transition-colors"
              [class]="i < currentStep() ? 'bg-green-500 text-white' : i === currentStep() ? 'bg-indigo-600 text-white' : 'bg-gray-200 dark:bg-gray-700 text-gray-500 dark:text-gray-400'">
              @if (i < currentStep()) {
                <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 13l4 4L19 7" />
                </svg>
              } @else {
                {{ i + 1 }}
              }
            </div>
            <span class="text-sm font-medium hidden sm:inline"
              [class]="i <= currentStep() ? 'text-gray-900 dark:text-white' : 'text-gray-400 dark:text-gray-500'">
              {{ step.label }}
            </span>
          </button>
          @if (i < steps().length - 1) {
            <div class="flex-1 h-px mx-3 transition-colors"
              [class]="i < currentStep() ? 'bg-green-400' : 'bg-gray-200 dark:bg-gray-700'"></div>
          }
        </div>
      }
    </nav>
  `,
})
export class StepperComponent {
  readonly steps = input.required<Step[]>();
  readonly currentStep = input.required<number>();
  readonly stepClick = output<number>();
}

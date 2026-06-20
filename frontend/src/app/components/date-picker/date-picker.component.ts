import {
  Component,
  computed,
  signal,
  input,
  output,
  viewChild,
  ElementRef,
  effect,
  HostListener,
} from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR, FormsModule } from '@angular/forms';
import { computePosition, offset, flip, shift, autoUpdate, type Placement } from '@floating-ui/dom';

export type DateCell = {
  date: string | null;
  day: number;
  isCurrentMonth: boolean;
  isToday: boolean;
  isSelected: boolean;
  isDisabled: boolean;
};

const MONTH_NAMES = [
  'January', 'February', 'March', 'April', 'May', 'June',
  'July', 'August', 'September', 'October', 'November', 'December',
];

function toYmd(d: Date): string {
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, '0');
  const day = String(d.getDate()).padStart(2, '0');
  return `${y}-${m}-${day}`;
}

function parseYmd(str: string): { y: number; m: number; d: number } | null {
  if (!str || !/^\d{4}-\d{2}-\d{2}$/.test(str)) return null;
  const [y, m, d] = str.split('-').map(Number);
  if (m < 1 || m > 12 || d < 1 || d > 31) return null;
  return { y, m: m - 1, d };
}

@Component({
  selector: 'app-date-picker',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './date-picker.component.html',
  providers: [
    { provide: NG_VALUE_ACCESSOR, useExisting: DatePickerComponent, multi: true },
  ],
})
export class DatePickerComponent implements ControlValueAccessor {
  private readonly el = viewChild<ElementRef<HTMLElement>>('pickerRef');
  private readonly triggerRef = viewChild<ElementRef<HTMLElement>>('triggerRef');
  private readonly calendarPanelRef = viewChild<ElementRef<HTMLElement>>('calendarPanel');

  private cleanupFloating: (() => void) | null = null;

  minDate = input<string>(''); // Y-m-d
  placeholder = input<string>('Select date');

  readonly valueChange = output<string>();

  protected readonly open = signal(false);
  protected readonly floatingPosition = signal<{ x: number; y: number }>({ x: 0, y: 0 });
  protected value = signal<string>('');
  protected disabled = signal(false);

  // View month/year (0-indexed month)
  protected viewDate = signal<{ year: number; month: number }>({ year: 0, month: 0 });

  constructor() {
    const t = new Date();
    this.viewDate.set({ year: t.getFullYear(), month: t.getMonth() });
    effect(() => {
      const v = this.value();
      if (v) {
        const p = parseYmd(v);
        if (p) this.viewDate.set({ year: p.y, month: p.m });
      }
    });

    effect(() => {
      const isOpen = this.open();
      if (!isOpen) {
        if (this.cleanupFloating) {
          this.cleanupFloating();
          this.cleanupFloating = null;
        }
        return;
      }
      // Defer so the calendar panel is in the DOM after @if (open()) renders
      setTimeout(() => {
        const trigger = this.triggerRef()?.nativeElement;
        const panel = this.calendarPanelRef()?.nativeElement;
        if (!trigger || !panel) return;

        const updatePosition = (): void => {
          computePosition(trigger, panel, {
            placement: 'bottom-start' as Placement,
            middleware: [offset(8), flip({ padding: 8 }), shift({ padding: 8 })],
          }).then(({ x, y }) => {
            this.floatingPosition.set({ x, y });
          });
        };

        updatePosition();
        this.cleanupFloating = autoUpdate(trigger, panel, updatePosition);
      }, 0);
    });
  }

  protected get monthName(): string {
    const { month } = this.viewDate();
    return MONTH_NAMES[month];
  }

  protected get viewYear(): number {
    return this.viewDate().year;
  }

  protected readonly weekDays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

  protected readonly cells = computed<DateCell[]>(() => {
    const { year, month } = this.viewDate();
    const min = this.minDate();
    const selected = this.value();
    const today = toYmd(new Date());

    const first = new Date(year, month, 1);
    // Monday = 0 (getDay() 1 -> 0, Sunday 0 -> 6)
    const startOffset = (first.getDay() + 6) % 7;
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const total = 42; // 6 rows
    const cells: DateCell[] = [];

    for (let i = 0; i < total; i++) {
      if (i < startOffset) {
        cells.push({
          date: null,
          day: 0,
          isCurrentMonth: false,
          isToday: false,
          isSelected: false,
          isDisabled: true,
        });
        continue;
      }
      const dayIndex = i - startOffset;
      if (dayIndex >= daysInMonth) {
        cells.push({
          date: null,
          day: 0,
          isCurrentMonth: false,
          isToday: false,
          isSelected: false,
          isDisabled: true,
        });
        continue;
      }
      const day = dayIndex + 1;
      const date = new Date(year, month, day);
      const dateStr = toYmd(date);
      const isDisabled = min ? dateStr < min : false;
      cells.push({
        date: dateStr,
        day,
        isCurrentMonth: true,
        isToday: dateStr === today,
        isSelected: dateStr === selected,
        isDisabled,
      });
    }
    return cells;
  });

  protected openCalendar(): void {
    if (this.disabled()) return;
    this.open.set(true);
  }

  protected closeCalendar(): void {
    this.open.set(false);
  }

  protected prevMonth(): void {
    const { year, month } = this.viewDate();
    if (month === 0) {
      this.viewDate.set({ year: year - 1, month: 11 });
    } else {
      this.viewDate.set({ year, month: month - 1 });
    }
  }

  protected nextMonth(): void {
    const { year, month } = this.viewDate();
    if (month === 11) {
      this.viewDate.set({ year: year + 1, month: 0 });
    } else {
      this.viewDate.set({ year, month: month + 1 });
    }
  }

  protected selectCell(cell: DateCell): void {
    if (!cell.date || cell.isDisabled) return;
    this.value.set(cell.date);
    this.onChange(cell.date);
    this.valueChange.emit(cell.date);
    this.closeCalendar();
  }

  @HostListener('document:click', ['$event'])
  onDocumentClick(event: MouseEvent): void {
    const host = this.el()?.nativeElement;
    if (host?.contains(event.target as Node)) return;
    this.closeCalendar();
  }

  protected displayValue(): string {
    const v = this.value();
    if (!v) return '';
    const p = parseYmd(v);
    if (!p) return v;
    const d = new Date(p.y, p.m, p.d);
    return d.toLocaleDateString(undefined, {
      weekday: 'short',
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  }

  // ControlValueAccessor
  private onChange: (value: string) => void = () => {};
  private _onTouched: () => void = () => {};
  protected onTouched(): void {
    this._onTouched();
  }

  writeValue(value: string | null): void {
    this.value.set(value ?? '');
  }

  registerOnChange(fn: (value: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this._onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled.set(isDisabled);
  }
}

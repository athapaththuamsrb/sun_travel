import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { DashboardComponent } from './dashboard.component';

describe('DashboardComponent', () => {
  let component: DashboardComponent;
  let fixture: ComponentFixture<DashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatDialogModule, HttpClientModule, MatFormFieldModule, MatNativeDateModule, MatFormFieldModule,
        MatInputModule, BrowserAnimationsModule,
        MatDatepickerModule, MatSelectModule, ReactiveFormsModule, FormsModule, MatIconModule, MatTableModule,],
      declarations: [DashboardComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should display the header is "DASHBOARD"', async () => {
    fixture.detectChanges();
    let compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('DASHBOARD');
  });
  it('Test form group input element count', async () => {
    fixture.detectChanges();
    const formElement = fixture.debugElement.nativeElement.querySelector("#searchForm");
    const inputElements = formElement.querySelectorAll("input");
    const selectElements = formElement.querySelectorAll("mat-select");
    expect(inputElements.length).toEqual(4);
    expect(selectElements.length).toEqual(1);
  });
  it('check initial form value', async () => {
    const formGroup = component.roomsSearchForm;
    const formValue = {
      start: null, end: null,
      location: ""
    }
    expect(formGroup.value).toEqual(formValue);
  });
  it("check the validation in start date input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#searchForm").querySelectorAll("input")[0];
    const start = component.roomsSearchForm.get("start");
    expect(form.value).toEqual("");
    expect(start?.errors).not.toBeNull();
    expect(start?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in loaction input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#searchForm").querySelectorAll("mat-select")[0];
    const location = component.roomsSearchForm.get("location");
    expect(form.value).toBeUndefined();
    expect(location?.errors).not.toBeNull();
    expect(location?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in end date input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#searchForm").querySelectorAll("input")[1];
    const end = component.roomsSearchForm.get("end");
    expect(form.value).toEqual("");
    expect(end?.errors).not.toBeNull();
    expect(end?.errors?.['required']).toBeTruthy();
  })
});

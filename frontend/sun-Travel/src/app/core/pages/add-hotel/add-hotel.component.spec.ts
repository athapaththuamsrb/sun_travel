import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { AddHotelComponent } from './add-hotel.component';

describe('AddHotelComponent', () => {
  let component: AddHotelComponent;
  let fixture: ComponentFixture<AddHotelComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatDialogModule, HttpClientModule, MatFormFieldModule, MatSelectModule, ReactiveFormsModule, MatFormFieldModule,
        MatInputModule, BrowserAnimationsModule],
      declarations: [AddHotelComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddHotelComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should display the header is "ADD HOTEL"', async () => {
    fixture.detectChanges();
    let compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('ADD HOTEL');
  });
  it('Test form group input element count', async () => {
    fixture.detectChanges();
    const formElement = fixture.debugElement.nativeElement.querySelector("#addHotelForm");
    const inputElements = formElement.querySelectorAll("input");
    const selectElements = formElement.querySelectorAll("mat-select");
    expect(inputElements.length).toEqual(2);
    expect(selectElements.length).toEqual(1);
  });
  it("check the validation in hotel name input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addHotelForm").querySelectorAll("input")[0];
    const hotelName = component.hotelAddForm.get("name")!;
    expect(hotelName.value).toEqual(form.value);
    expect(hotelName?.errors).not.toBeNull();
    expect(hotelName?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in loaction input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addHotelForm").querySelectorAll("mat-select")[0];
    const location = component.hotelAddForm.get("location");
    expect(form.value).toBeUndefined();
    expect(location?.errors).not.toBeNull();
    expect(location?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in contact input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addHotelForm").querySelectorAll("input")[1];
    const contactNumber = component.hotelAddForm.get("contactNumber");
    expect(contactNumber?.value).toEqual(form.value);
    expect(contactNumber?.errors).not.toBeNull();
    expect(contactNumber?.errors?.['required']).toBeTruthy();
  })
});

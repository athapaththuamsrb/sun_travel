import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatTabsModule } from '@angular/material/tabs';
import { MatSelectModule } from '@angular/material/select';
import { AddContractComponent } from './add-contract.component';
import { MatNativeDateModule } from '@angular/material/core';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatInputModule } from '@angular/material/input';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

describe('AddContractComponent', () => {
  let component: AddContractComponent;
  let fixture: ComponentFixture<AddContractComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatDialogModule, HttpClientModule, MatFormFieldModule, MatTabsModule, MatSelectModule, MatNativeDateModule,
        MatDatepickerModule, MatFormFieldModule,
        MatInputModule, ReactiveFormsModule, BrowserAnimationsModule],
      declarations: [AddContractComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(AddContractComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should display the header is "ADD CONTRACT"', async () => {
    fixture.detectChanges();
    let compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('ADD CONTRACT');
  });
  it('Test new room type add form group input element count', async () => {
    fixture.detectChanges();
    const formElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm");
    const inputElements = formElement.querySelectorAll("input");
    const selectElements = formElement.querySelectorAll("mat-select");
    const textAreaElements = formElement.querySelectorAll("textarea");
    expect(inputElements.length).toEqual(7);
    expect(selectElements.length).toEqual(1);
    expect(textAreaElements.length).toEqual(1);
  });
  it('check initial new room type form value', async () => {
    const formGroup = component.newTypeAddContractForm;
    const formValue = {
      name: "", type: null, start: "", end: "",
      price: null,
      maxAdults: null,
      numRoom: null,
      markup: null,
      description: ""
    }
    expect(formGroup.value).toEqual(formValue);
  });
  it('check initial exist room type form value', async () => {
    const formGroup = component.existTypeAddContractForm;
    const formValue = {
      name: "", type: null, start: "", end: "",
      price: null,
      numRoom: null,
      markup: null,
      description: ""
    }
    expect(formGroup.value).toEqual(formValue);
  });
  it("check the validation in hotel name input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("mat-select")[0];
    const hotelName = component.newTypeAddContractForm.get("name");
    expect(form.value).toBeUndefined();
    expect(hotelName?.errors).not.toBeNull();
    expect(hotelName?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in type input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("input")[0];
    const type = component.newTypeAddContractForm.get("type");
    expect(form.value).toEqual("");
    expect(type?.errors).not.toBeNull();
    expect(type?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in start input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("input")[1];
    const start = component.newTypeAddContractForm.get("start");
    expect(form.value).toEqual(start?.value!);
    expect(start?.errors).not.toBeNull();
    expect(start?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in end input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("input")[2];
    const end = component.newTypeAddContractForm.get("end");
    expect(form.value).toEqual(end?.value!);
    expect(end?.errors).not.toBeNull();
    expect(end?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in price input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("input")[3];
    const price = component.newTypeAddContractForm.get("price");
    expect(form.value).toEqual("");
    expect(price?.errors).not.toBeNull();
    expect(price?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in maxAdults input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("input")[4];
    const maxAdults = component.newTypeAddContractForm.get("maxAdults");
    expect(form.value).toEqual("");
    expect(maxAdults?.errors).not.toBeNull();
    expect(maxAdults?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in numRoom input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("input")[5];
    const numRoom = component.newTypeAddContractForm.get("numRoom");
    expect(form.value).toEqual("");
    expect(numRoom?.errors).not.toBeNull();
    expect(numRoom?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in markup input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("input")[6];
    const markup = component.newTypeAddContractForm.get("markup")
    expect(form.value).toEqual("");
    expect(markup?.errors).not.toBeNull();
    expect(markup?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in description input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#addNewTypeForm").querySelectorAll("textarea")[0];
    const description = component.newTypeAddContractForm.get("description");
    expect(form.value).toEqual(description?.value!);
    expect(description?.errors).not.toBeNull();
    expect(description?.errors?.['required']).toBeTruthy();
  })
});

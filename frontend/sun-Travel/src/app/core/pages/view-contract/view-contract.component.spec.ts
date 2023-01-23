import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ViewContractComponent } from './view-contract.component';

describe('ViewContractComponent', () => {
  let component: ViewContractComponent;
  let fixture: ComponentFixture<ViewContractComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MatDialogModule, HttpClientModule, MatFormFieldModule, MatSelectModule, MatFormFieldModule,
        MatInputModule, ReactiveFormsModule, MatTableModule, BrowserAnimationsModule],
      declarations: [ViewContractComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ViewContractComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('should display the header is "VIEW CONTRACT"', async () => {
    fixture.detectChanges();
    let compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('VIEW CONTRACT');
  });
  it('Test form group input element count', async () => {
    fixture.detectChanges();
    const formElement = fixture.debugElement.nativeElement.querySelector("#searchContractForm");
    const selectElements = formElement.querySelectorAll("mat-select");
    expect(selectElements.length).toEqual(2);
  });
  it('check initial form value', async () => {
    const formGroup = component.viewContractForm;
    const formValue = { name: "", type: null }
    expect(formGroup.value).toEqual(formValue);
  });
  it("check the validation in hotel name input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#searchContractForm").querySelectorAll("mat-select")[0];
    const hotelName = component.viewContractForm.get("name");
    expect(form.value).toBeUndefined();
    expect(hotelName?.errors).not.toBeNull();
    expect(hotelName?.errors?.['required']).toBeTruthy();
  })
  it("check the validation in room type input", async () => {
    const form: HTMLInputElement = fixture.debugElement.nativeElement.querySelector("#searchContractForm").querySelectorAll("mat-select")[1];
    const type = component.viewContractForm.get("type");
    expect(form.value).toBeUndefined();
    expect(type?.errors).not.toBeNull();
    expect(type?.errors?.['required']).toBeTruthy();
  })

});

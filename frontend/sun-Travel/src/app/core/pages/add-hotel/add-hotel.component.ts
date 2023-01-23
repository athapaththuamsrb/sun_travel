import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { ModalComponent } from '../../modal/modal.component';
import { HotelDto } from '../../models/HotelDto';
import { HotelService } from '../../services/hotel/hotel.service';
import { ShareService } from '../../services/share/share.service';

@Component({
  selector: 'app-add-hotel',
  templateUrl: './add-hotel.component.html',
  styleUrls: ['./add-hotel.component.css']
})
export class AddHotelComponent {
  isSpinne: boolean = false;
  isPendding: boolean = false;
  /*Form controler for new hotel*/
  hotelAddForm = new FormGroup({
    name: new FormControl("", [Validators.required, Validators.maxLength(100)]),
    location: new FormControl(null, [Validators.required]),
    contactNumber: new FormControl("", [Validators.required, Validators.pattern("[0-9]{9}")])
  })
  constructor(public dialog: MatDialog, private service: HotelService, private shareService: ShareService) { }

  /*open module to show msg*/
  public openModule(msg: string, status: string): void {
    const dialogRef = this.dialog.open(ModalComponent, {
      data: { msg, status, id: "" },
    });
    dialogRef.afterClosed().subscribe(result => {

    })
  }
  public save(): void {
    /*Validate the form field*/
    if (this.hotelAddForm.get('name')?.value == null || this.hotelAddForm.get('contactNumber')?.value == null || this.hotelAddForm.get('location')?.value == null) {
      this.openModule("Attempted is fales.Try Again!!", "bad");/*show error massage case data are not valid*/
      return
    } else {
      const data: HotelDto = { name: this.hotelAddForm.get('name')?.value!, contact: this.hotelAddForm.get('contactNumber')?.value!, location: this.hotelAddForm.get('location')?.value! }
      this.hotelAddForm.disable();
      this.isPendding = true;
      setTimeout(() => {
        if (this.isPendding)
          this.isSpinne = true;
        else
          this.isSpinne = false;
      }, 200)
      this.service.addHotel(data).subscribe(
        response => {
          this.openModule("Attempted is Successfully!!", "good");/*show massage case successfully added*/
          this.shareService.updateHotelList();
          this.hotelAddForm.enable();
          this.hotelAddForm.reset();
          this.isPendding = false;
          this.isSpinne = false;
        },
        error => {
          this.openModule("Attempted is fales.Try Again!!", "bad");/*show error massage case data are not valid*/
          this.hotelAddForm.enable();
          this.isPendding = false;
          this.isSpinne = false;
        }
      );

      return
    }

  }
}

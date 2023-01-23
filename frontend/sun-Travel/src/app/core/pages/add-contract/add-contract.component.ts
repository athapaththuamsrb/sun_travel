import { Component, OnInit } from '@angular/core';
import { DatePipe, KeyValue } from '@angular/common';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ModalComponent } from '../../modal/modal.component';
import { ContractService } from '../../services/contract/contract.service';
import { MatDialog } from '@angular/material/dialog';
import { NewTypeContractDto } from '../../models/NewTypeContractDto';
import { ExistTypeContractDto } from '../../models/ExistTypeContractDto';
import { HotelService } from '../../services/hotel/hotel.service';
import { ShareService } from '../../services/share/share.service';

@Component({
  selector: 'app-add-contract',
  templateUrl: './add-contract.component.html',
  styleUrls: ['./add-contract.component.css']
})
export class AddContractComponent implements OnInit {
  isPendding: boolean = false;
  isSpinne: boolean = false;
  hotelList: any = this.shareService.hotelList;
  date = new Date().toLocaleDateString('en-ca');
  newStartDate = new Date().toLocaleDateString('en-ca');
  existStartDate = new Date().toLocaleDateString('en-ca');

  /*Form controler add for new roon type and contrcat*/
  newTypeAddContractForm = new FormGroup({
    name: new FormControl("", Validators.required),
    type: new FormControl(null, [Validators.required, Validators.maxLength(100)]),
    start: new FormControl("", Validators.required),
    end: new FormControl("", Validators.required),
    price: new FormControl(null, [Validators.required, Validators.min(1)]),
    maxAdults: new FormControl(null, [Validators.required, Validators.min(1)]),
    numRoom: new FormControl(null, [Validators.required, Validators.min(1)]),
    markup: new FormControl(null, [Validators.required, Validators.max(100), Validators.min(1)]),
    description: new FormControl("", [Validators.required, Validators.maxLength(2000)]),
  });

  /*Form controler for exist type to new add contrcat*/
  existTypeAddContractForm = new FormGroup({
    name: new FormControl("", Validators.required),
    type: new FormControl(null, [Validators.required, Validators.maxLength(100)]),
    start: new FormControl("", Validators.required),
    end: new FormControl("", Validators.required),
    price: new FormControl(null, [Validators.required, Validators.min(1)]),
    numRoom: new FormControl(null, [Validators.required, Validators.min(1)]),
    markup: new FormControl(null, [Validators.required, Validators.max(100), Validators.min(1)]),
    description: new FormControl("", [Validators.required, Validators.maxLength(2000)]),
  })
  typeList: { type: string; }[] = [{ type: "" }];
  constructor(public dialog: MatDialog, private service: ContractService, private hotelService: HotelService, private shareService: ShareService) { }

  /*reloading and in the start this funtion run and load the data*/
  ngOnInit(): void {
    this.hotelService.getHotelWithTheirRoomType().subscribe(
      response => {
        this.hotelList = [{
          "id": "",
          "name": "",
          "location": "",
          "contact": "",
          "roomTypeList": [""]
        }]
        this.hotelList.push(...response.data);
      })
    const nowDate = new Date();
    nowDate.setDate(nowDate.getDate() + 1)
    this.date = nowDate.toLocaleDateString('en-ca');
    this.newStartDate = nowDate.toLocaleDateString('en-ca');
    this.existStartDate = nowDate.toLocaleDateString('en-ca');

  }
  private onCompare(_left: KeyValue<any, any>, _right: KeyValue<any, any>): number {
    return -1;
  }
  /*After select the hotel name then update to drop down for room type  */
  public update(): void {
    this.hotelList.forEach((hotel: { name: string | null | undefined; roomTypeList: { type: string; }[]; }) => {
      if (hotel.name == this.existTypeAddContractForm.get('name')?.value) {
        this.typeList = hotel.roomTypeList.length == 0 ? [{ type: "" }] : hotel.roomTypeList;
      }
    })
  }
  /*After select start date update the end date min value*/
  public updateStartDate(state: string): void {
    if (state == "existType") {
      const newDate = new Date(this.existTypeAddContractForm.get('start')?.value!);
      newDate.setDate(newDate.getDate() + 30)
      this.existStartDate = newDate.toLocaleDateString('en-ca');
    }
    else {
      const newDate = new Date(this.newTypeAddContractForm.get('start')?.value!);
      newDate.setDate(newDate.getDate() + 30)
      this.newStartDate = newDate.toLocaleDateString('en-ca');
    }
  }
  /*Open module to show massage to user*/
  public openModule(msg: string, status: string): void {
    const dialogRef = this.dialog.open(ModalComponent, {
      data: { msg, status, id: "" },
    });
    dialogRef.afterClosed().subscribe(result => { });
  }

  /*Save the new type and contrcat*/
  public saveNewType(): void {
    /*Validate the form field*/
    if (this.newTypeAddContractForm.get('name')?.value == null || this.newTypeAddContractForm.get('type')?.value == null || this.newTypeAddContractForm.get('start')?.value == null || this.newTypeAddContractForm.get('end')?.value == null || this.newTypeAddContractForm.get('price')?.value == null || this.newTypeAddContractForm.get('maxAdults')?.value == null || this.newTypeAddContractForm.get('numRoom')?.value == null || this.newTypeAddContractForm.get('markup')?.value == null || this.newTypeAddContractForm.get('description')?.value == null) {
      this.openModule("Attempted is fales.Try Again!!", "bad");/*show error massage case data are not valid*/
      return
    } else {
      const data: NewTypeContractDto = {
        hotelDto: { name: this.newTypeAddContractForm.get('name')?.value! }, type: this.newTypeAddContractForm.get('type')?.value!, max_adults: this.newTypeAddContractForm.get('maxAdults')?.value!, contractList: [{
          start_contract: this.newTypeAddContractForm.get('start')?.value!, end_contract: this.newTypeAddContractForm.get('end')?.value!, price: this.newTypeAddContractForm.get('price')?.value!, markup: this.newTypeAddContractForm.get('markup')?.value!
          , count: this.newTypeAddContractForm.get('numRoom')?.value!, isExpired: false,
          description: this.newTypeAddContractForm.get('description')?.value!
        }]
      }
      this.newTypeAddContractForm.disable();
      this.isPendding = true;
      setTimeout(() => {
        if (this.isPendding)
          this.isSpinne = true;
        else
          this.isSpinne = false;
      }, 200)
      this.service.addNewRoomTypeContract(data).subscribe(
        response => {
          this.openModule("Attempted is Successfully!!", "good");/*show massage case successfully added*/
          this.newTypeAddContractForm.enable();
          this.isPendding = false;
          this.isSpinne = false;
          this.newTypeAddContractForm.reset();//reset the form
          this.shareService.updateHotelList();//update the hotel list
          this.hotelList.forEach((hotel: { "name": String; "roomTypeList": {}[] }, index: number) => {
            if (data.hotelDto.name == hotel.name) {
              this.hotelList[index].roomTypeList.push({ id: '', hotelDto: null, type: data.type, max_adults: data.max_adults, contractList: [] });
            }
          })
        },
        error => {
          this.openModule("Attempted is fales.Try Again!!", "bad");/*show error massage case data are not valid*/
          this.newTypeAddContractForm.enable();
          this.isPendding = false;
          this.isSpinne = false;
        }
      );
      return
    }
  }
  /*Save the exist type to new contrcat*/
  public saveExistType(): void {
    /*Validate the form field*/
    if (this.existTypeAddContractForm.get('name')?.value == null || this.existTypeAddContractForm.get('type')?.value == null || this.existTypeAddContractForm.get('start')?.value == null || this.existTypeAddContractForm.get('end')?.value == null || this.existTypeAddContractForm.get('price')?.value == null || this.existTypeAddContractForm.get('numRoom')?.value == null || this.existTypeAddContractForm.get('markup')?.value == null || this.existTypeAddContractForm.get('description')?.value == null) {
      this.openModule("Attempted is fales.Try Again!!", "bad");/*show error massage case data are not valid*/
      return
    } else {

      const data: ExistTypeContractDto = {
        room_type_dto: { type: this.existTypeAddContractForm.get('type')?.value!, hotelDto: { name: this.existTypeAddContractForm.get('name')?.value! } },
        start_contract: this.existTypeAddContractForm.get('start')?.value!,
        end_contract: this.existTypeAddContractForm.get('end')?.value!,
        price: this.existTypeAddContractForm.get('price')?.value!,
        count: this.existTypeAddContractForm.get('numRoom')?.value!,
        markup: this.existTypeAddContractForm.get('markup')?.value!,
        description: this.existTypeAddContractForm.get('description')?.value!,
        isExpired: false
      }
      this.existTypeAddContractForm.disable();
      this.isPendding = true;
      setTimeout(() => {
        if (this.isPendding)
          this.isSpinne = true;
        else
          this.isSpinne = false;
      }, 200)
      this.service.addExistRoomTypeContract(data).subscribe(
        response => {
          this.openModule("Attempted is Successfully!!", "good");/*show massage case successfully added*/
          this.existTypeAddContractForm.enable();
          this.isPendding = false;
          this.isSpinne = false;
          this.existTypeAddContractForm.reset();//reset the form
          this.shareService.updateHotelList();//update the hotel list
          this.hotelList = this.shareService.hotelList;
        },
        error => {
          this.openModule("Attempted is fales.Try Again!!", "bad");/*show error massage case data are not valid*/
          this.existTypeAddContractForm.enable();
          this.isPendding = false;
          this.isSpinne = false;
        }
      );
      return
    }
  }
}

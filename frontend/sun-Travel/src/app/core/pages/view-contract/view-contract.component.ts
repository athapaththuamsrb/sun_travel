import { Component, OnInit } from '@angular/core';
import { KeyValue } from '@angular/common';
import { MatTableDataSource } from '@angular/material/table';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { ContractService } from '../../services/contract/contract.service';
import { ModalComponent } from '../../modal/modal.component';
import { ViewContractDto } from '../../models/ViewContractDto';
import { ShareService } from '../../services/share/share.service';
import { HotelService } from '../../services/hotel/hotel.service';
import { faSearch, faSun } from '@fortawesome/free-solid-svg-icons';

export interface PeriodicElement {
  id: string;
  start: string;
  end: string;
  price: number;
  count: number;
  maxAdules: number;
  markup: string;
  status: string;
  isExpired: boolean;
}

@Component({
  selector: 'app-view-contract',
  templateUrl: './view-contract.component.html',
  styleUrls: ['./view-contract.component.css'],
})
export class ViewContractComponent implements OnInit {
  searchIcon = faSearch;
  isSpinne: boolean = false;
  isPendding: boolean = false;
  hotelList: any = this.shareService.hotelList;
  displayedColumns: string[] = ['id', 'start', 'end', 'price', 'count', 'maxAdules', 'markup', 'status', 'delete'];
  //dataSource = new MatTableDataSource(ELEMENT_DATA);
  data: PeriodicElement[] = []
  dataSource = new MatTableDataSource(this.data);
  typeList: { type: string; }[] = [{ type: "" }];

  viewContractForm = new FormGroup({
    name: new FormControl("", Validators.required),
    type: new FormControl(null, Validators.required)
  });
  properties: any;
  constructor(public dialog: MatDialog, private service: ContractService, private shareService: ShareService, private hotelService: HotelService) { }

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
  }
  public applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }
  private onCompare(_left: KeyValue<any, any>, _right: KeyValue<any, any>): number {
    return -1;
  }
  /*After select the hotel name then update to drop down for room type  */
  public update(): void {
    this.hotelList.forEach((hotel: { name: string | null | undefined; roomTypeList: { type: string; }[]; }) => {
      if (hotel.name == this.viewContractForm.get('name')?.value) {
        this.typeList = hotel.roomTypeList.length == 0 ? [{ type: "" }] : hotel.roomTypeList;
      }
    })
  }
  /*open module to show msg*/
  public openModule(msg: string, status: string, id: string): void {
    const dialogRef = this.dialog.open(ModalComponent, {
      data: { msg, status, id },
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result != "" && id.length == 36 && id == result) {
        this.isPendding = true;
        this.service.deleteContract({ id }).subscribe(
          response => {
            setTimeout(() => { this.dialog.open(ModalComponent, { data: { msg: "Attempted is Successfull!!", status: "good", id: "" } }); }, 0);
            this.search()
            this.isPendding = false;
          }, error => {
            setTimeout(() => { this.dialog.open(ModalComponent, { data: { msg: "Attempted is fales.Try Again!!", status: "bad", id: "" } }); }, 0);
            this.isPendding = false;
          });
      }
    });
  }
  /*Search the room type that satisfy the condition*/
  public search(): void {
    /*validate the form*/
    if (this.viewContractForm.get('name')?.value == null || this.viewContractForm.get('type')?.value == null) {
      this.openModule("Attempted is fales.Try Again!!", "bad", "");/*show error massage case data are not valid*/
      return
    }
    else {
      const data: ViewContractDto = { e1: this.viewContractForm.get('name')?.value!, e2: this.viewContractForm.get('type')?.value!, e3: "", e4: "" }
      this.viewContractForm.disable();
      this.isPendding = true;
      setTimeout(() => {
        if (this.isPendding)
          this.isSpinne = true;
        else
          this.isSpinne = false;
      }, 200)
      this.service.searchContracts(data).subscribe(
        response => {
          let data: PeriodicElement[] = response.data.contractList.map((contract: {
            markup: string; id: string; start_contract: string; end_contract: string; price: string; count: string; isExpired: string
          }) => {
            return { id: contract.id, start: contract.start_contract, end: contract.end_contract, price: contract.price, count: contract.count, maxAdules: response.data.max_adults, markup: contract.markup + " %", status: contract.isExpired ? "EXPIRED" : "PROCESSING", isExpired: contract.isExpired }
          })
          this.viewContractForm.enable();
          this.dataSource = new MatTableDataSource(data);
          this.isPendding = false;
          this.isSpinne = false;
        },
        error => {
          this.openModule("Attempted is fales.Try Again!!", "bad", "");/*show error massage case data are not valid*/
          this.viewContractForm.enable();
          this.isPendding = false;
          this.isSpinne = false;
        }
      );
      return
    }
  }
  /*Ask and  verify theuser want to delete or not */
  delete(id: string): void {
    const dialogRef = this.openModule("Are sure you want to delete this contract?", "normal", id);
  }
}
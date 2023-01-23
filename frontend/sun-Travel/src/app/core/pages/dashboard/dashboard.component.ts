import { Component, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { ModalComponent } from '../../modal/modal.component';
import { SearchRoomType } from '../../models/SearchRoomType';
import { RoomService } from '../../services/room/room.service';
import { DialogComponent } from './components/dialog/dialog.component';

export interface PeriodicElement {
  name: string;
  type: string;
  price: number;
  count: number
  maxAdults: number;
  startContract: string;
  endContrcat: string;
}
export interface pair {
  numberRoom: number;
  maxAdults: number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  isPendding: boolean = false;
  isSpinne: boolean = false;
  displayedColumns: string[] = ['id', 'name', 'type', 'price', 'count', 'maxAdults', 'viewMore'];
  pairTableColumns: string[] = ["numberRoom", "maxAdults", "delete"];
  //dataSource = ELEMENT_DATA;
  dataSource: PeriodicElement[] = []
  pairs: pair[] = [];
  pairSource: MatTableDataSource<any> = new MatTableDataSource<any>([]);
  date = new Date().toLocaleDateString('en-ca');
  startDate = new Date().toLocaleDateString('en-ca');

  /*Form controler for geting hotel name and room type*/
  roomsSearchForm = new FormGroup({
    start: new FormControl(null, Validators.required),
    end: new FormControl(null, Validators.required),
    location: new FormControl("", Validators.required),
  })
  constructor(public dialog: MatDialog, private service: RoomService) { }

  /*for open diloag for show me details about row*/
  public openDialog(hotel: string, type: string, price: Number, count: Number, maxAdults: Number, startContract: string, endContract: string, description: string): void {
    const dialogRef = this.dialog.open(DialogComponent, {
      data: { hotel, type, price, count, maxAdults, startContract, endContract, description },
    });
  }
  /*Add max adults and room number to the table and list*/
  public addCard(data: any): void {
    if (data.numberRoom > 0 && data.maxAdults > 0) {
      for (let i = 0; this.pairs.length > i; i++) {
        if (this.pairs[i].maxAdults == data.maxAdults) {
          this.pairs[i].numberRoom += data.numberRoom;
          this.updateDataSource();
          return;
        }
      }
      this.pairs.push(data);
      this.updateDataSource();
    }

  }
  /*remove the existed row */
  public removeCart(index: number): void {
    this.pairs.splice(index, 1);
    this.updateDataSource();
  }
  /*update pairs table*/
  public updateDataSource(): void {
    this.pairSource.data = this.pairs;
  }
  /*Update the end date min value select start date*/
  public updateStartDate(): void {
    const newDate = new Date(this.roomsSearchForm.get('start')?.value!);
    newDate.setDate(newDate.getDate() + 1)
    this.startDate = newDate.toLocaleDateString('en-ca');
  }
  /*open module to show msg*/
  private openModule(msg: string, status: string): void {
    const dialogRef = this.dialog.open(ModalComponent, {
      data: { msg, status, id: "" },
    });
    dialogRef.afterClosed().subscribe(result => { });
  }
  /*Search room type under condition*/
  public searchRooms(): void {
    if (this.roomsSearchForm.get('start')?.value == null || this.roomsSearchForm.get('end')?.value == null || this.roomsSearchForm.get('location')?.value == null || this.pairs.length == 0) {
      this.openModule("Attempted is fales.Try Again!!", "bad");
      return
    } else {
      const data: SearchRoomType = { start: this.roomsSearchForm.get('start')?.value!, end: this.roomsSearchForm.get('end')?.value!, location: this.roomsSearchForm.get('location')?.value!, pairs: this.pairs }
      this.roomsSearchForm.disable();
      this.isPendding = true;
      setTimeout(() => {
        if (this.isPendding)
          this.isSpinne = true;
        else
          this.isSpinne = false;
      }, 200)
      this.service.searchRoomType(data).subscribe(
        response => {
          if (response.data == null || response.data.length == 0) {
            this.dataSource = [];
            this.openModule("No availble rooms!!", "bad");
            this.roomsSearchForm.enable();
            this.isPendding = false;
            this.isSpinne = false;
            return;
          }
          else {
            this.roomsSearchForm.enable();
            this.isPendding = false;
            this.isSpinne = false;
            this.dataSource = response.data.map(((roomtype: {
              hotelDto: { name: string; }; type: string; contractList: {
                end_contract: any;
                start_contract: any;
                description: string;
                markup: number;
                price: number; count: number;
              }[]; max_adults: number;
            }) => {
              return {
                name: roomtype.hotelDto.name, type: roomtype.type, price: Math.round(roomtype.contractList[0].price * (1 + roomtype.contractList[0].markup / 100)), count: roomtype.contractList[0].count, maxAdults: roomtype.max_adults, startContract: roomtype.contractList[0].start_contract,
                endContract: roomtype.contractList[0].end_contract, description: roomtype.contractList[0].description
              }
            }));
          }
        },
        error => {
          this.dataSource = [];
          this.openModule("No availble rooms!!", "bad");
          this.roomsSearchForm.enable();
          this.isSpinne = false;
          this.isPendding = false;
        }
      );
      return
    }
  }
}

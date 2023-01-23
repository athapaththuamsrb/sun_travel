import { Component, Inject, OnInit, TemplateRef, ViewChild } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';


@Component({
  selector: 'app-modal',
  templateUrl: './modal.component.html',
  styleUrls: ['./modal.component.css']
})
export class ModalComponent implements OnInit {
  @ViewChild(TemplateRef) ref: any;
  color = "success"
  constructor(public dialogRef: MatDialogRef<ModalComponent>, @Inject(MAT_DIALOG_DATA) public data: { msg: string, status: string, id: string }) { }

  /*reloading and in the start this funtion run and load the data*/
  ngOnInit(): void {
    switch (this.data.status) {
      case "bad":
        this.color = "bad";
        break
      case "good":
        this.color = "success";
        break
      default:
        this.color = "normal"
        break
    }
  }
  /*Model close When click ok button*/
  onNoClick(): void {
    this.dialogRef.close();
  }

}

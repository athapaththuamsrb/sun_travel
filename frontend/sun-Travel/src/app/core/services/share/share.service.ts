import { Injectable } from '@angular/core';
import { environment } from 'environment';
import { HotelService } from '../hotel/hotel.service';

@Injectable({
  providedIn: 'root'
})
export class ShareService {
  hotelList: any = [{
    "id": "",
    "name": "",
    "location": "",
    "contact": "",
    "roomTypeList": [""]
  }];
  constructor(private hotelService: HotelService) { }
  url = environment.baseUrl;
  /*Update the shared hotel list*/
  public updateHotelList(): void {
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
        return this.hotelList;
      })

  }
}
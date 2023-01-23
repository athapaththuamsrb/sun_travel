import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environment';
import { Observable } from 'rxjs';
import { HotelDto } from '../../models/HotelDto';

@Injectable({
  providedIn: 'root'
})
export class HotelService {
  url = environment.baseUrl
  constructor(private http: HttpClient) { }
  /*Add hotel*/
  public addHotel(data: HotelDto): Observable<any> {
    return this.http.post(this.url + "hotel/add", data)
  }
  /*Get hotel list and their room type*/
  public getHotelWithTheirRoomType(): Observable<any> {
    return this.http.get(this.url + "hotel/get")
  }
}

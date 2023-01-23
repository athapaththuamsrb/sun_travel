import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environment';
import { Observable } from 'rxjs';
import { SearchRoomType } from '../../models/SearchRoomType';

@Injectable({
  providedIn: 'root'
})
export class RoomService {
  url = environment.baseUrl
  constructor(private http: HttpClient) { }
  /*Search room type that under filter option*/
  public searchRoomType(data: SearchRoomType): Observable<any> {
    return this.http.post(this.url + "roomType/search", data)//TODO HAVE TO MAKE API
  }
}

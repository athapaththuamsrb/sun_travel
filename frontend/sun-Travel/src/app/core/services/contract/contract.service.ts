import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'environment';
import { Observable } from 'rxjs';
import { ExistTypeContractDto } from '../../models/ExistTypeContractDto';
import { NewTypeContractDto } from '../../models/NewTypeContractDto';
import { ViewContractDto } from '../../models/ViewContractDto';

@Injectable({
  providedIn: 'root'
})
export class ContractService {
  url = environment.baseUrl
  constructor(private http: HttpClient) { }
  /*Search list of contract under given hotel name and room type*/
  public searchContracts(data: ViewContractDto): Observable<any> {
    return this.http.post(this.url + "contract/search", data);
  }
  /*Add exist room type to new contract*/
  public addExistRoomTypeContract(data: ExistTypeContractDto): Observable<any> {
    return this.http.post(this.url + "contract/add", data);
  }
  /*Add new room type and new contract for it*/
  public addNewRoomTypeContract(data: NewTypeContractDto): Observable<any> {
    return this.http.post(this.url + "roomType/add", data);
  }
  /*Delete the contract*/
  public deleteContract(data: { id: string }): Observable<any> {
    return this.http.post(this.url + "contract/delete", data);
  }
}

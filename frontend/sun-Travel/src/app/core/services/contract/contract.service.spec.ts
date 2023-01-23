import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { FactoryTarget } from '@angular/compiler';
import { getTestBed, TestBed } from '@angular/core/testing';
import { ExistTypeContractDto } from '../../models/ExistTypeContractDto';
import { NewTypeContractDto } from '../../models/NewTypeContractDto';
import { ViewContractDto } from '../../models/ViewContractDto';

import { ContractService } from './contract.service';

describe('ContractService', () => {
  let service: ContractService;
  let injector: TestBed;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ContractService]
    });
    // service = TestBed.inject(ContractService);
    injector = getTestBed();
    service = injector.get(ContractService);
    httpMock = injector.get(HttpTestingController);
  });
  afterEach(() => {
    httpMock.verify();
  });
  it('should be created', () => {
    expect(service).toBeTruthy();
  });
  it('searchContracts(data: ViewContractDto): Observable<any>', () => {
    const dummySearchContractData: ViewContractDto = { e1: "Hilton", e2: "single bed", e3: "", e4: "" }
    const response = {
      code: "00",
      isSuccess: true,
      description: null,
      data: {
        "id": "c0a81814-85d0-1973-8185-d03e59450001",
        "hotelDto": null,
        "type": "single bed",
        "max_adults": 2,
        "contractList": [
          {
            "id": "c0a81814-85d0-1973-8185-d03e59480002",
            "room_type_dto": null,
            "start_contract": "2023-01-21",
            "end_contract": "2023-02-22",
            "price": 35000.00,
            "count": 100,
            "isExpired": true,
            "markup": 5,
            "description": "we gave full board package for this.",
            "roomBookingDtoList": null
          },
          {
            "id": "c0a81814-85d0-1973-8185-d040f7ec0003",
            "room_type_dto": null,
            "start_contract": "2023-01-20",
            "end_contract": "2023-02-23",
            "price": 35000.00,
            "count": 100,
            "isExpired": false,
            "markup": 5,
            "description": "we offer full board package.",
            "roomBookingDtoList": null
          }
        ]
      }
    }
    service.searchContracts(dummySearchContractData).subscribe(response => {
      expect(response.code).toBe("00");
      expect(response.isSuccess).toBe(true);
    });

    const req = httpMock.expectOne(`${service.url}contract/search`);
    expect(req.request.method).toBe("POST");
    req.flush(response);

  });
  it('addExistRoomTypeContract(data: ExistTypeContractDto): Observable<any>', () => {
    const existTypeContractData: ExistTypeContractDto = {
      room_type_dto: { type: "single bed", hotelDto: { name: "Cinomon Red" } },
      start_contract: "2023-01-20",
      end_contract: "2023-01-22",
      price: 25000,
      count: 100,
      isExpired: false,
      markup: 5,
      description: "This is full board package.This one night."
    }
    const response = {
      code: "00",
      isSuccess: true,
      description: null,
      data: null
    }
    service.addExistRoomTypeContract(existTypeContractData).subscribe(response => {
      expect(response.code).toBe("00");
      expect(response.isSuccess).toBe(true);
    });

    const req = httpMock.expectOne(`${service.url}contract/add`);
    expect(req.request.method).toBe("POST");
    req.flush(response);

  });
  it('addNewRoomTypeContract(data: NewTypeContractDto): Observable<any>', () => {
    const newTypeContractData: NewTypeContractDto = {
      hotelDto: { name: "Hilton" },
      type: "single bed",
      max_adults: 2,
      contractList: [{
        start_contract: "2023-01-20",
        end_contract: "2023-02-28",
        price: 25000,
        count: 100,
        isExpired: false,
        markup: 5,
        description: "This 1 night package"
      }]
    }
    const response = {
      code: "00",
      isSuccess: true,
      description: null,
      data: null
    }
    service.addNewRoomTypeContract(newTypeContractData).subscribe(response => {
      expect(response.code).toBe("00");
      expect(response.isSuccess).toBe(true);
    });

    const req = httpMock.expectOne(`${service.url}roomType/add`);
    expect(req.request.method).toBe("POST");
    req.flush(response);

  });
  it('deleteContract(data: { id: string }): Observable<any>', () => {
    const contractData = { id: "912eu2h91dh3ud" }
    const response = {
      code: "00",
      isSuccess: true,
      description: null,
      data: null
    }
    service.deleteContract(contractData).subscribe(response => {
      expect(response.code).toBe("00");
      expect(response.isSuccess).toBe(true);
    });

    const req = httpMock.expectOne(`${service.url}contract/delete`);
    expect(req.request.method).toBe("POST");
    req.flush(response);

  });


});

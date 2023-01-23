import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { getTestBed, TestBed } from '@angular/core/testing';

import { ShareService } from './share.service';

describe('ShareService', () => {
  let service: ShareService;
  let injector: TestBed;
  let httpMock: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ShareService]
    });
    // service = TestBed.inject(ShareService);
    injector = getTestBed();
    service = injector.get(ShareService);
    httpMock = injector.get(HttpTestingController);
  });
  afterEach(() => {
    httpMock.verify();
  });
  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('updateHotelList():void', () => {
    service.updateHotelList();
    const response = {
      code: "00",
      isSuccess: true,
      description: null,
      data: [
        {
          "id": "c0a81814-85d0-1973-8185-d03d4f860000",
          "name": "Cinomon Red",
          "location": "Colombo",
          "contact": "123456789",
          "roomTypeList": [
            {
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
          ]
        },
        {
          "id": "c0a85314-85ce-16a3-8185-cebaa45b0003",
          "name": "Hilton",
          "location": "Colombo",
          "contact": "768294279",
          "roomTypeList": [
            {
              "id": "c0a85314-85ce-16a3-8185-cebbd12f0004",
              "hotelDto": null,
              "type": "single bed room",
              "max_adults": 2,
              "contractList": [
                {
                  "id": "c0a85314-85ce-16a3-8185-cebbd1310005",
                  "room_type_dto": null,
                  "start_contract": "2023-01-21",
                  "end_contract": "2023-02-21",
                  "price": 35000.00,
                  "count": 100,
                  "isExpired": true,
                  "markup": 5,
                  "description": "this one night room. we are not providing foods.",
                  "roomBookingDtoList": null
                },
                {
                  "id": "c0a85314-85ce-16a3-8185-cebe90480006",
                  "room_type_dto": null,
                  "start_contract": "2023-01-20",
                  "end_contract": "2023-02-25",
                  "price": 10000.00,
                  "count": 100,
                  "isExpired": false,
                  "markup": 10,
                  "description": "we providing dinner. not lunch",
                  "roomBookingDtoList": null
                }
              ]
            }
          ]
        },
      ]
    }
    const req = httpMock.expectOne(`${service.url}hotel/get`);
    expect(req.request.method).toBe("GET");
    req.flush(response);
  });
});

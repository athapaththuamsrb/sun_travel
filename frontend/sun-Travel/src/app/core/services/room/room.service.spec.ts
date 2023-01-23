import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { getTestBed, TestBed } from '@angular/core/testing';
import { SearchRoomType } from '../../models/SearchRoomType';
import { RoomService } from './room.service';

describe('RoomService', () => {
  let service: RoomService;
  let injector: TestBed;
  let httpMock: HttpTestingController;
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [RoomService]
    });
    //service = TestBed.inject(RoomService);
    injector = getTestBed();
    service = injector.get(RoomService);
    httpMock = injector.get(HttpTestingController);
  });
  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('searchRoomType(data: SearchRoomType): Observable<any>', () => {
    const dummySearchData: SearchRoomType = {
      start: "2023-01-20", end: "2023-02-28", location: "Colombo",
      pairs: [{ numberRoom: 2, maxAdults: 1 }]
    }
    const response = { code: "00", isSuccess: true, data: [], description: null }
    service.searchRoomType(dummySearchData).subscribe(response => {
      expect(response.code).toBe("00");
      expect(response.isSuccess).toBe(true);
    });

    const req = httpMock.expectOne(`${service.url}roomType/search`);
    expect(req.request.method).toBe("POST");
    req.flush(response);

  });
});

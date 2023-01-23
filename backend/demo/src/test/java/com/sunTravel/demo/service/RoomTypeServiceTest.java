package com.sunTravel.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sunTravel.demo.dto.ContractDto;
import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.dto.SearchDto;
import com.sunTravel.demo.entity.Contract;
import com.sunTravel.demo.entity.Hotel;
import com.sunTravel.demo.entity.RoomType;
import com.sunTravel.demo.repository.ContractRepository;
import com.sunTravel.demo.repository.HotelRepository;
import com.sunTravel.demo.repository.RoomTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith( MockitoExtension.class )
class RoomTypeServiceTest
{
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private RoomTypeRepository roomTypeRepository ;
    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private RoomTypeService roomTypeService;
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();
    @Test
    void insert_fail_EmptyOrNullContractList()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",null );
        RoomTypeDto roomTypeDto1=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,null);
        RoomTypeDto roomTypeDto2=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>());

        assertEquals("07",roomTypeService.insert( roomTypeDto1 ));
        assertEquals("07",roomTypeService.insert( roomTypeDto2 ));
    }
    @Test
    void insert_fail_NullOrZeroLengthDescription()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",null );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,null,null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));
        ContractDto contractDto1=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"",null);
        RoomTypeDto roomTypeDto1=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto1 )));

        assertEquals("07",roomTypeService.insert( roomTypeDto ));
        assertEquals("07",roomTypeService.insert( roomTypeDto1 ));
    }
    @Test
    void insert_fail_NotExistHotel()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );

        List<Contract> contractList1=new ArrayList<>( Arrays.asList( new Contract() ));
        List<Hotel> hotelList=new ArrayList<>();

        BDDMockito.given( hotelRepository.findByName( roomTypeDto.getHotelDto().getName())).willReturn(hotelList);
        assertEquals("07",roomTypeService.insert( roomTypeDto ));
    }


    @Test
    void insert_fail_DuplicateRoomTypeUnderHotel()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());

        List<Contract> contractList1=new ArrayList<>( Arrays.asList( new Contract() ));
        List<Hotel> hotelList=new ArrayList<>(Arrays.asList( hotel ));
        List<RoomType> roomTypeList=new ArrayList<>(Arrays.asList( roomType ));

        BDDMockito.given( hotelRepository.findByName( roomTypeDto.getHotelDto().getName())).willReturn(hotelList);
        BDDMockito.given( modelMapper.map(hotelList.get(0),HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( modelMapper.map(roomTypeDto, RoomType.class )).willReturn(roomType);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId(roomType.getType(),roomType.getHotel().getId())).willReturn(roomTypeList);
        assertEquals("06",roomTypeService.insert( roomTypeDto ));
    }
    @Test
    void insert_fail_pastStartDate()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/20"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());

        List<Contract> contractList1=new ArrayList<>( Arrays.asList( new Contract() ));
        List<Hotel> hotelList=new ArrayList<>(Arrays.asList( hotel ));
        List<RoomType> roomTypeList=new ArrayList<>();

        BDDMockito.given( hotelRepository.findByName( roomTypeDto.getHotelDto().getName())).willReturn(hotelList);
        BDDMockito.given( modelMapper.map(hotelList.get(0),HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( modelMapper.map(roomTypeDto, RoomType.class )).willReturn(roomType);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId(roomType.getType(),roomType.getHotel().getId())).willReturn(roomTypeList);
        assertEquals("06",roomTypeService.insert( roomTypeDto ));
    }
    @Test
    void insert_fail_pastEndDate()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2022/12/10"),new Date("2023/01/10"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());

        List<Contract> contractList1=new ArrayList<>( Arrays.asList( new Contract() ));
        List<Hotel> hotelList=new ArrayList<>(Arrays.asList( hotel ));
        List<RoomType> roomTypeList=new ArrayList<>();

        BDDMockito.given( hotelRepository.findByName( roomTypeDto.getHotelDto().getName())).willReturn(hotelList);
        BDDMockito.given( modelMapper.map(hotelList.get(0),HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( modelMapper.map(roomTypeDto, RoomType.class )).willReturn(roomType);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId(roomType.getType(),roomType.getHotel().getId())).willReturn(roomTypeList);
        assertEquals("06",roomTypeService.insert( roomTypeDto ));
    }
    @Test
    void insert_fail_InvalidDataCombination()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/02/10"),new Date("2023/01/10"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());

        List<Contract> contractList1=new ArrayList<>( Arrays.asList( new Contract() ));
        List<Hotel> hotelList=new ArrayList<>(Arrays.asList( hotel ));
        List<RoomType> roomTypeList=new ArrayList<>();

        BDDMockito.given( hotelRepository.findByName( roomTypeDto.getHotelDto().getName())).willReturn(hotelList);
        BDDMockito.given( modelMapper.map(hotelList.get(0),HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( modelMapper.map(roomTypeDto, RoomType.class )).willReturn(roomType);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId(roomType.getType(),roomType.getHotel().getId())).willReturn(roomTypeList);
        assertEquals("06",roomTypeService.insert( roomTypeDto ));
    }
    @Test
    void insert_successful()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/05/23"),new Date("2023/06/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);

        List<Contract> contractList1=new ArrayList<>( Arrays.asList( new Contract() ));
        List<Hotel> hotelList=new ArrayList<>(Arrays.asList( hotel ));
        List<RoomType> roomTypeList=new ArrayList<>();

        BDDMockito.given( hotelRepository.findByName( roomTypeDto.getHotelDto().getName())).willReturn(hotelList);
        BDDMockito.given( modelMapper.map(hotelList.get(0),HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( modelMapper.map(roomTypeDto, RoomType.class )).willReturn(roomType);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId(roomType.getType(),roomType.getHotel().getId())).willReturn(roomTypeList);
        BDDMockito.given( roomTypeRepository.save(roomType)).willReturn(roomType);
        BDDMockito.given( modelMapper.map(contractDto, Contract.class )).willReturn(contract);
        BDDMockito.given( contractRepository.save(contract)).willReturn(contract);

        assertEquals("00",roomTypeService.insert( roomTypeDto ));
    }


    @Test
    void search_fail_DateIsNotValid()
    {
        SearchDto searchDto=new SearchDto(new Date("2023/01/25"),new Date("2023/01/23"),"Colombo",new ArrayList<>(Arrays.asList( new RoomDto(1,2) )));
        assertEquals(null,roomTypeService.search( searchDto ));
    }
    @Test
    void search_successful()
    {
        RoomDto room=new RoomDto(1,2);
        SearchDto searchDto=new SearchDto(new Date("2023/01/23"),new Date("2023/01/25"),"Colombo",new ArrayList<>(Arrays.asList(room )));

        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);

        List<RoomType> roomTypeList=new ArrayList<>(Arrays.asList( roomType ));
        List<RoomTypeDto> roomTypeDtoList=new ArrayList<>(Arrays.asList( roomTypeDto ));


        BDDMockito.given( roomTypeRepository.findAvailableRoomType( searchDto.getLocation(),searchDto.getStart(),searchDto.getEnd(),room.getMaxAdults(),room.getNumberRoom())).willReturn(roomTypeList);
        BDDMockito.given(modelMapper.map(roomTypeList,new TypeToken<List<RoomTypeDto>>(){}.getType() )).willReturn(roomTypeDtoList);
        BDDMockito.given(roomTypeRepository.findById(roomType.getId())).willReturn( Optional.of(roomType));
        BDDMockito.given(modelMapper.map(roomType.getHotel(),HotelDto.class)).willReturn(hotelDto);

        assertEquals(roomTypeDtoList,roomTypeService.search( searchDto ));
    }
}
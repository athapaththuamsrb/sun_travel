package com.sunTravel.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sunTravel.demo.dto.ContractDto;
import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.entity.Contract;
import com.sunTravel.demo.entity.Hotel;
import com.sunTravel.demo.entity.RoomType;
import com.sunTravel.demo.repository.ContractRepository;
import com.sunTravel.demo.repository.HotelRepository;
import com.sunTravel.demo.repository.RoomTypeRepository;
import net.bytebuddy.dynamic.DynamicType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
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
class ContractServiceTest
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
    private ContractService contractService;
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();




    @Test
    void delete_successful()
    {
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,null,null,null,null,null,5,"xx",null);
        Hotel hotel=new Hotel("","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",roomType,new Date("2023/01/23"),new Date("2023/02/28") , BigDecimal.valueOf( 45000 ),100,10," " ,false);

        BDDMockito.given( contractRepository.findById(contractDto.getId())).willReturn(Optional.of(contract));
        BDDMockito.given( contractRepository.save( contract )).willReturn(contract  );
        assertEquals(true,contractService.delete(contractDto.getId()) );

    }
    @Test
    void delete_fail()
    {
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,null,null,null,null,null,5,"xx",null);
        Hotel hotel=new Hotel("","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",roomType,new Date("2023/01/23"),new Date("2023/02/28") , BigDecimal.valueOf( 45000 ),100 ,5,"xx",false);

        BDDMockito.given( contractRepository.findById(contractDto.getId())).willReturn(Optional.ofNullable(null  ));
        assertEquals(false,contractService.delete(contractDto.getId()) );

    }
    @Test
    void delete_fail_tryToDeleteAlreadyExpiredOne()
    {
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,null,null,null,null,null,5,"xx",null);
        Hotel hotel=new Hotel("","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",roomType,new Date("2023/01/23"),new Date("2023/02/28") , BigDecimal.valueOf( 45000 ),100 ,5,"xx",true);

        BDDMockito.given( contractRepository.findById(contractDto.getId())).willReturn(Optional.of(contract));
        assertEquals(false,contractService.delete(contractDto.getId()) );

    }
    @Test
    void isValidateStartAndEndDate_fail()
    {
        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );

        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        List<Contract> contractList1=new ArrayList<>(Arrays.asList( new Contract() ));
        List<Contract> contractList2=new ArrayList<>( );
        BDDMockito.given( contractRepository.findIsNotExpired(roomType.getId())).willReturn(contractList1  );
        BDDMockito.given( contractRepository.dateValidate(new Date("2023/01/23"),new Date("2023/01/25"),roomType.getId())).willReturn(contractList2  );

        assertEquals(false,contractService.isValidateStartAndEndDate(new Date("2023/01/23"),new Date("2023/01/23"),roomType.getId()));//start and end date both are equal
        assertEquals(false,contractService.isValidateStartAndEndDate(new Date("2023/01/25"),new Date("2023/01/23"),roomType.getId()));//start date after end date
        assertEquals(false,contractService.isValidateStartAndEndDate(new Date("2023/01/23"),new Date("2023/01/25"),roomType.getId()));//there exist valid contract
    }
    @Test
    void isValidateStartAndEndDate_successful()
    {
        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );

        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        List<Contract> contractList1=new ArrayList<>(Arrays.asList( new Contract() ));
        BDDMockito.given( contractRepository.findIsNotExpired(roomType.getId())).willReturn(contractList1  );
        BDDMockito.given( contractRepository.dateValidate(new Date("2023/01/23"),new Date("2023/01/25"),roomType.getId())).willReturn(contractList1  );

        assertEquals(true,contractService.isValidateStartAndEndDate(new Date("2023/01/23"),new Date("2023/01/25"),roomType.getId()));
    }
    @Test
    void search_fail_HotelIsNotExist()
    {   Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );

        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());

        BDDMockito.given( hotelRepository.findByName(hotel.getName())).willReturn(new ArrayList<>()  );
        assertEquals(null,contractService.search(hotel.getName(),roomType.getType()));
    }
    @Test
    void search_fail_TypeIsNotExist()
    {   Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );

        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());

        BDDMockito.given( hotelRepository.findByName(hotel.getName())).willReturn(new ArrayList<>(Arrays.asList( hotel ))  );
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId(roomType.getType(),hotel.getId())).willReturn(new ArrayList<>()  );
        assertEquals(null,contractService.search(hotel.getName(),roomType.getType()));
    }
    @Test
    void search_successful()
    {   Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );

        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>());

        BDDMockito.given( hotelRepository.findByName(hotel.getName())).willReturn(new ArrayList<>(Arrays.asList( hotel ))  );
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId(roomType.getType(),hotel.getId())).willReturn(new ArrayList<>(Arrays.asList( roomType ))  );
        BDDMockito.given( modelMapper.map(  roomType,RoomTypeDto.class)).willReturn(roomTypeDto  );
        assertEquals(roomTypeDto,contractService.search(hotel.getName(),roomType.getType()));
    }
    @Test
    void insert_fail_NullOrZeroLengthDescription()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>());
        ContractDto contractDto1=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",roomTypeDto,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"",null);
        ContractDto contractDto2=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",roomTypeDto,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,null,null);

        assertEquals(null,contractService.insert(contractDto1));
        assertEquals(null,contractService.insert(contractDto2));
    }
    @Test
    void insert_fail_HotelIsNotExist()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>());
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",roomTypeDto,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);
        BDDMockito.given( hotelRepository.findByName( contractDto.getRoom_type_dto().getHotelDto().getName())).willReturn(new ArrayList<>()  );
        assertEquals(null,contractService.insert(contractDto));
    }
    @Test
    void insert_fail_RoomTypeIsNotExist()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>());
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",roomTypeDto,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/23"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);
        BDDMockito.given( hotelRepository.findByName( contractDto.getRoom_type_dto().getHotelDto().getName())).willReturn(new ArrayList<>(Arrays.asList( hotel ))  );
        BDDMockito.given( modelMapper.map(hotel,HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId( contractDto.getRoom_type_dto().getType(),hotelDto.getId() )).willReturn(new ArrayList<>());

        assertEquals(null,contractService.insert(contractDto));
    }
    @Test
    void insert_fail_DateNotValid()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );

        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/26"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        contractDto.setRoom_type_dto( roomTypeDto );

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/26"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);
        BDDMockito.given( hotelRepository.findByName( contractDto.getRoom_type_dto().getHotelDto().getName())).willReturn(new ArrayList<>(Arrays.asList( hotel ))  );
        BDDMockito.given( modelMapper.map(hotel,HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId( roomTypeDto.getType(),hotelDto.getId() )).willReturn(new ArrayList<>(Arrays.asList( roomType)));
        BDDMockito.given( modelMapper.map(roomType,RoomTypeDto.class)).willReturn(roomTypeDto);
        assertEquals(null,contractService.insert(contractDto));
    }
    @Test
    void insert_fail_EndDateNotValid()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );

        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2022/12/20"),new Date("2023/01/20"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        contractDto.setRoom_type_dto( roomTypeDto );

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/26"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);
        BDDMockito.given( hotelRepository.findByName( contractDto.getRoom_type_dto().getHotelDto().getName())).willReturn(new ArrayList<>(Arrays.asList( hotel ))  );
        BDDMockito.given( modelMapper.map(hotel,HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId( roomTypeDto.getType(),hotelDto.getId() )).willReturn(new ArrayList<>(Arrays.asList( roomType)));
        BDDMockito.given( modelMapper.map(roomType,RoomTypeDto.class)).willReturn(roomTypeDto);
        assertEquals(null,contractService.insert(contractDto));
    }
    @Test
    void insert_fail_StartDateNotValid()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );

        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/02"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,new ArrayList<>(Arrays.asList( contractDto )));

        contractDto.setRoom_type_dto( roomTypeDto );

        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/26"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);
        BDDMockito.given( hotelRepository.findByName( contractDto.getRoom_type_dto().getHotelDto().getName())).willReturn(new ArrayList<>(Arrays.asList( hotel ))  );
        BDDMockito.given( modelMapper.map(hotel,HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId( roomTypeDto.getType(),hotelDto.getId() )).willReturn(new ArrayList<>(Arrays.asList( roomType)));
        BDDMockito.given( modelMapper.map(roomType,RoomTypeDto.class)).willReturn(roomTypeDto);
        assertEquals(null,contractService.insert(contractDto));
    }
    @Test
    void insert_successful()
    {
        HotelDto hotelDto=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",null );
        RoomTypeDto roomTypeDto=new RoomTypeDto("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotelDto,"single room",2,null);
        ContractDto contractDto=new ContractDto("c0a80214-85c5-1b38-8185-c58253ff0003",roomTypeDto,new Date("2023/05/25"),new Date("2023/06/26"), BigDecimal.valueOf( 45000 ),100,false,5,"xx",null);


        Hotel hotel=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
        RoomType roomType=new RoomType("c0a83414-85c3-1c0d-8185-c31cb04c0000",hotel,"single room",2,new ArrayList<>());
        Contract contract=new Contract("c0a80214-85c5-1b38-8185-c58253ff0003",null,new Date("2023/01/24"),new Date("2023/01/25"), BigDecimal.valueOf( 45000 ),100,5,"xx",false);
        BDDMockito.given( hotelRepository.findByName( contractDto.getRoom_type_dto().getHotelDto().getName())).willReturn(new ArrayList<>(Arrays.asList( hotel ))  );
        BDDMockito.given( modelMapper.map(hotel,HotelDto.class)).willReturn(hotelDto);
        BDDMockito.given( roomTypeRepository.findByTypeAndHotelId( roomTypeDto.getType(),hotelDto.getId() )).willReturn(new ArrayList<>(Arrays.asList( roomType)));
        BDDMockito.given( modelMapper.map(roomType,RoomTypeDto.class)).willReturn(roomTypeDto);
        BDDMockito.given( modelMapper.map( contractDto,Contract.class )).willReturn(contract);
        BDDMockito.given( modelMapper.map(contractRepository.save(contract),ContractDto.class)).willReturn(contractDto);
        assertEquals(contractDto,contractService.insert(contractDto));
    }

}
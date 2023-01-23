package com.sunTravel.demo.repository;

import com.sunTravel.demo.entity.Contract;
import com.sunTravel.demo.entity.Hotel;
import com.sunTravel.demo.entity.RoomType;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class RoomTypeRepositoryTest
{
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @BeforeEach
    void setUp(){
        //add hotel
        List<RoomType> roomTypeList=new ArrayList<>();
        Hotel hotel = new Hotel(null,"Hilton","Colombo","0775436865",roomTypeList);
        hotelRepository.save( hotel );
        Hotel hotel1=hotelRepository.findByName( "Hilton" ).get(0);
        //add room type to above hotel
        List<Contract> contractList=new ArrayList<>();
        RoomType roomType=new RoomType(null,hotel1,"single room",2,contractList);
        roomTypeRepository.save( roomType );
        RoomType roomType1=roomTypeRepository.findByTypeAndHotelId( roomType.getType(),hotel1.getId() ).get( 0 );
        //add contract to above hotel's room type
        Contract contract=new Contract(null,roomType1,new Date("2023/01/20"),new Date("2023/02/20"), BigDecimal.valueOf( 45000 ),200,5,"This room we gave for day out package",false);
        contractRepository.save( contract );
    }
    @Transactional
    @Test
    void findAvailableRoomType_availableRoom()
    {
        assertEquals(1,roomTypeRepository.findAvailableRoomType( "Colombo",new Date("2023/01/23"),new Date("2023/01/25"),2,2 ).size());//outside date
    }
    @Transactional
    @Test
    void findAvailableRoomType_NotAvailableRoom()
    {
        assertEquals(0,roomTypeRepository.findAvailableRoomType( "Colombo",new Date("2023/02/23"),new Date("2023/02/25"),2,2 ).size());//outside date
    }
}
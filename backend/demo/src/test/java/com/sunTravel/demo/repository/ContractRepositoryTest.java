package com.sunTravel.demo.repository;

import com.sunTravel.demo.entity.Contract;
import com.sunTravel.demo.entity.Hotel;
import com.sunTravel.demo.entity.RoomType;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ContractRepositoryTest
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
/*When contract already exits and try to validate
* */
    @Test
    @Transactional
    void dateValidate_invalid()
    {
        Hotel hotel= hotelRepository.findByName( "Hilton" ).get( 0 );
        RoomType roomType=roomTypeRepository.findByTypeAndHotelId( "single room",hotel.getId() ).get( 0 );
        List<Contract> list1=contractRepository.dateValidate( new Date("2023/01/21"),new Date("2023/02/19"), roomType.getId());
        assertEquals(0,list1.size());//inside date
    }
    /*When contract already exits and try to validate
     * */
    @Test
    @Transactional
    void dateValidate_valid()
    {
        Hotel hotel= hotelRepository.findByName( "Hilton" ).get( 0 );
        RoomType roomType=roomTypeRepository.findByTypeAndHotelId( "single room",hotel.getId() ).get( 0 );
        List<Contract> list2=contractRepository.dateValidate( new Date("2023/03/21"),new Date("2023/04/19"), roomType.getId());
        assertEquals(1,list2.size());//outside date
    }

    @Test
    @Transactional
    void findIsNotExpired_zeroElementList()
    {
        Hotel hotel= hotelRepository.findByName( "Hilton" ).get( 0 );
        RoomType roomType=roomTypeRepository.findByTypeAndHotelId( "single room",hotel.getId() ).get( 0 );
        List<Contract> list1=contractRepository.findIsNotExpired(roomType.getId());
        assertEquals(1,list1.size());//outside date

    }
    @Test
    @Transactional
    void findIsNotExpired_OneElementList()
    {
        Hotel hotel= hotelRepository.findByName( "Hilton" ).get( 0 );
        RoomType roomType=roomTypeRepository.findByTypeAndHotelId( "single room",hotel.getId() ).get( 0 );

        Contract contract=contractRepository.findIsNotExpired(roomType.getId()).get(0);
        contract.setIsExpired( true );//expired contract
        contractRepository.save( contract );
        List<Contract> list1=contractRepository.findIsNotExpired(roomType.getId());
        assertEquals(0,list1.size());//outside date

    }
}
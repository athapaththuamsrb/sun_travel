package com.sunTravel.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sunTravel.demo.controller.HotelController;
import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.entity.Hotel;
import com.sunTravel.demo.entity.RoomType;
import com.sunTravel.demo.repository.HotelRepository;
import com.sunTravel.demo.repository.RoomTypeRepository;
import com.sunTravel.demo.share.Share;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith( MockitoExtension.class )
class HotelServiceTest
{
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private HotelRepository hotelRepository;
    @Mock
    private RoomTypeRepository roomTypeRepository ;
    @InjectMocks
    private HotelService hotelService;
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();
    Hotel hotel1=new Hotel("c0a80214-85c5-1b38-8185-c57cae670000","Hilton","Colombo","0768294279",new ArrayList<RoomType>() );
    Hotel hotel2=new Hotel("c0a83414-85c3-1656-8185-c3175da70000","Taj Samudra","Colombo","0775226866",new ArrayList<RoomType>() );
    HotelDto hotelDto1=new HotelDto("c0a80214-85c5-1b38-8185-c57cae670000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
    HotelDto hotelDto2=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Taj Samudra","Colombo","0775226866",new ArrayList<RoomTypeDto>() );

    @Test
    void insert_fail()
    {
        BDDMockito.given( modelMapper.map( hotelDto1, Hotel.class )).willReturn( hotel1 );
        BDDMockito.given( hotelRepository.findByName( hotel1.getName())).willReturn(new ArrayList<>(Arrays.asList( hotel1 ))  );
        assertEquals(null,hotelService.insert(hotelDto1) );
    }
    @Test
    void insert_successful()
    {
        BDDMockito.given( modelMapper.map( hotelDto1, Hotel.class )).willReturn( hotel1 );
        BDDMockito.given( hotelRepository.findByName( hotel1.getName())).willReturn(new ArrayList<>(Arrays.asList())  );
        BDDMockito.given( modelMapper.map(hotelRepository.save(hotel1),HotelDto.class)).willReturn( hotelDto1 );
        assertEquals(hotelDto1,hotelService.insert(hotelDto1) );
    }

    @Test
    void getAll()
    {

        List<Hotel> hotelList=new ArrayList<>( Arrays.asList(hotel1,hotel2));
        List<HotelDto> hotelDtoList=new ArrayList<>( Arrays.asList(hotelDto1,hotelDto2));
        BDDMockito.given( hotelRepository.findAll()).willReturn( hotelList );
        BDDMockito.given( modelMapper.map(hotelList,new TypeToken<List<HotelDto>>(){}.getType() )).willReturn( hotelDtoList );
        assertEquals(hotelDtoList,hotelService.getAll() );
    }
}
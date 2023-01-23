package com.sunTravel.demo.service;

import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.entity.Hotel;
import com.sunTravel.demo.repository.HotelRepository;
import com.sunTravel.demo.repository.RoomTypeRepository;
import com.sunTravel.demo.share.Share;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HotelService
{
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private ModelMapper modelMapper;

    public HotelDto insert( HotelDto hotelDto )
    {
        Hotel hotel=modelMapper.map( hotelDto, Hotel.class );
        hotel.setName( Share.setFormatOfWords(hotel.getName()));//set hotel name format

        List<Hotel>hotelList= hotelRepository.findByName( hotel.getName());//check database
        if(hotelList.isEmpty()){//check whether is there exist hotel
            return modelMapper.map(hotelRepository.save(hotel),HotelDto.class);
        }else{
            return null;
        }
    }

//    public HotelDto get( String hotelId )
//    {
//        Optional<Hotel> hotel= hotelRepository.findById( hotelId );
//        return hotel.isEmpty()?null:modelMapper.map( hotel.get(),HotelDto.class );
//    }
    public List<HotelDto> getAll(){
        List<Hotel> hotelList=hotelRepository.findAll();//fetch all hotel
        List<HotelDto> hotelDtoList=modelMapper.map(hotelList,new TypeToken<List<HotelDto>>(){}.getType() );
        return hotelDtoList;
    }

//    public HotelDto update( HotelDto hotelDto )
//    {
//        Hotel hotel=modelMapper.map( hotelDto, Hotel.class );
//        List<Hotel>hotelList= hotelRepository.findByIdAndName(hotel.getId(),hotel.getName());
//        if(hotelList.isEmpty()){
//            return null;
//        }else{
//            return modelMapper.map(hotelRepository.save(hotel),HotelDto.class);
//        }
//    }
//    public boolean delete(HotelDto hotelDto){
//        Integer num=hotelRepository.deleteByIdAndName(hotelDto.getId(),hotelDto.getName() ).size();
//        System.out.println(num);
//        return num >0?true:false;
//    }


}
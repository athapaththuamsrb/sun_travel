package com.sunTravel.demo.service;

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
import com.sunTravel.demo.share.Share;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Transactional
public class RoomTypeService
{
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ModelMapper modelMapper;

    public String insert( RoomTypeDto roomTypeDto )
    {
        if(roomTypeDto.getContractList()==null || roomTypeDto.getContractList().size()==0){
            return "07";
        }
        if(roomTypeDto.getContractList().get(0).getDescription()==null||roomTypeDto.getContractList().get(0).getDescription().length()==0){
                return "07";
        }
        roomTypeDto.getHotelDto().setName( Share.setFormatOfWords(roomTypeDto.getHotelDto().getName()));//set format of hotel name
        List<Hotel> hotelList=hotelRepository.findByName( roomTypeDto.getHotelDto().getName());//check hotel name exist
        if(!hotelList.isEmpty()){//if it not exits add to database
            HotelDto hotelDto=modelMapper.map(hotelList.get(0),HotelDto.class);
            roomTypeDto.setHotelDto(hotelDto);
            ContractDto contractDto =roomTypeDto.getContractList().get(0);
            roomTypeDto.setContractList(new ArrayList<>());
            RoomType roomType=modelMapper.map(roomTypeDto, RoomType.class );
            roomType.setType( roomType.getType().toLowerCase());//set format of room type
            List<RoomType> roomTypeList= roomTypeRepository.findByTypeAndHotelId(roomType.getType(),roomType.getHotel().getId());
            Date current=new Date(System.currentTimeMillis());
            Boolean isValidDate=Share.isStartAndEndDateValid( contractDto.getStart_contract(),contractDto.getEnd_contract());
            Boolean isValidStart=Share.isStartAndEndDateValid( current,contractDto.getStart_contract());
            Boolean isValidEnd=Share.isStartAndEndDateValid( current,contractDto.getEnd_contract());
            if(roomTypeList.isEmpty() &&  isValidDate && isValidStart && isValidEnd){//if type is not exits add to database
                    RoomType roomType1=roomTypeRepository.save(roomType);
                    contractDto.setRoom_type_dto(new RoomTypeDto(roomType1.getId()));
                    contractDto.setDescription(contractDto.getDescription().toLowerCase());
                    contractDto.setIsExpired( false );
                    Contract contract=modelMapper.map(contractDto, Contract.class );
                    contractRepository.save(contract);
                    return "00";
            }else{
                    return "06";
            }
        }else{
                return "07";
        }
    }
    public List<RoomTypeDto> search( SearchDto searchDto )
    {
        List<RoomTypeDto> searchOutputList=new ArrayList<>();
        List<String> roomTypeIdList=new ArrayList<>();
        if(!Share.isStartAndEndDateValid( searchDto.getStart(),searchDto.getEnd())){//check date valid or not
            return null;
        }
        for( RoomDto room:searchDto.getPairs() ){//fetch rooms
            searchDto.setLocation( Share.setFormatOfWords( searchDto.getLocation() ) );
            List<RoomType> roomTypetList=roomTypeRepository.findAvailableRoomType( searchDto.getLocation(),searchDto.getStart(),searchDto.getEnd(),room.getMaxAdults(),room.getNumberRoom());
            List<RoomTypeDto> roomTypetDtoList=modelMapper.map(roomTypetList,new TypeToken<List<RoomTypeDto>>(){}.getType() );
            for(RoomTypeDto type:roomTypetDtoList){
                if(!roomTypeIdList.contains(type.getId()) ){
                    for(ContractDto contract:type.getContractList()){
                        if(!contract.getIsExpired().booleanValue()){
                            roomTypeIdList.add( type.getId() );
                            List<ContractDto>list=new ArrayList<>();
                            list.add(contract);
                            type.setContractList(list);
                            RoomType roomType=roomTypeRepository.findById(type.getId()).get();
                            type.setHotelDto(modelMapper.map(roomType.getHotel(),HotelDto.class) );
                            searchOutputList.add( type);
                        }
                    }

                }
            }
        }

        return searchOutputList;

    }




//    public RoomTypeDto get( String hotelName,String type )
//    {
//        List<Hotel> hotel= hotelRepository.findByName(hotelName);
//        if(hotel.isEmpty()){
//            return null;
//        }
//        List<RoomType> roomTypeList=roomTypeRepository.findByTypeAndHotelId( type,hotel.get(0).getId());
//        return roomTypeList.isEmpty()?null:modelMapper.map( roomTypeList.get( 0 ),RoomTypeDto.class );
//    }
//    public List<RoomTypeDto> getAll(){
//        List<RoomType> roomTypeList=roomTypeRepository.findAll();
//        List<RoomTypeDto> roomTypeDtoList=modelMapper.map(roomTypeList,new TypeToken<List<HotelDto>>(){}.getType() );
//        return roomTypeDtoList;
//    }
//    public RoomTypeDto update( RoomTypeDto roomTypeDto )
//    {
//        List<Hotel> hotelList=hotelRepository.findByName( roomTypeDto.getHotelDto().getName());
//        if(!hotelList.isEmpty()){
//            HotelDto hotelDto=modelMapper.map( hotelList.get( 0 ), HotelDto.class );
//            roomTypeDto.setHotelDto(hotelDto);
//            RoomType roomType=modelMapper.map( roomTypeDto, RoomType.class );
//            List<RoomType>roomTypeList= roomTypeRepository.findByTypeAndHotelId(roomType.getType(),roomType.getHotel().getId());
//            if(roomTypeList.isEmpty()){
//                return null;
//            }else{
//                return modelMapper.map(roomTypeRepository.save(roomType),RoomTypeDto.class);
//            }
//        }else{
//            return null;
//        }
//
//    }
//    public boolean delete( RoomTypeDto roomTypeDto ){
//        List<Hotel> hotelList=hotelRepository.findByName(roomTypeDto.getHotelDto().getName());
//        if(!hotelList.isEmpty()){
//            Integer num=roomTypeRepository.deleteByHotelIdAndType(hotelList.get( 0 ).getId(),roomTypeDto.getType() );
//            return num.longValue() >0;
//        }else{
//            return false;
//        }
//    }
}
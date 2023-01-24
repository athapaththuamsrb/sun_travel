package com.sunTravel.demo.service;

import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.dto.ContractDto;
import com.sunTravel.demo.entity.Hotel;
import com.sunTravel.demo.entity.Contract;
import com.sunTravel.demo.entity.RoomType;
import com.sunTravel.demo.repository.HotelRepository;
import com.sunTravel.demo.repository.ContractRepository;
import com.sunTravel.demo.repository.RoomTypeRepository;
import com.sunTravel.demo.share.Share;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContractService
{
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private RoomTypeRepository roomTypeRepository;
    @Autowired
    private ModelMapper modelMapper;

    public RoomTypeDto search( String hotelName, String roomType )
    {
        hotelName=Share.setFormatOfWords( hotelName );//make hotel name format
        roomType=roomType.toLowerCase();//make room type format
        List<Hotel> hotelList=hotelRepository.findByName(hotelName);
        if(!hotelList.isEmpty()){//check hotel name is exist or not
            List<RoomType> roomTypeList= roomTypeRepository.findByTypeAndHotelId(roomType,hotelList.get(0).getId());
            if(!roomTypeList.isEmpty()){
                RoomTypeDto roomTypeDto=modelMapper.map(  roomTypeList.get(0),RoomTypeDto.class);
                return roomTypeDto;
            }else{
                return null;
                }
        }else{
            return null;
        }
    }

    public Boolean delete(String contractId)
    {
        Optional<Contract> contract=contractRepository.findById(contractId );//check contract id exist or not
        if( contract.isEmpty() ){
            return false;
        }
        else
        {
            Contract contract1=contract.get();
            if(contract1.getIsExpired()){//check is it already expire if is it return false
                return false;
            }
            contract1.setIsExpired( true );//set to expired
            contractRepository.save( contract1 );//update the database
            return true;
        }
    }

    public ContractDto insert( ContractDto contractDto)
    {   if(contractDto.getDescription()==null||contractDto.getDescription().length()==0){
        return null;
    }
        contractDto.getRoom_type_dto().setType( contractDto.getRoom_type_dto().getType().toLowerCase());//Make format of type
        contractDto.getRoom_type_dto().getHotelDto().setName( Share.setFormatOfWords( contractDto.getRoom_type_dto().getHotelDto().getName() ));//Make format of hotel name
        List<Hotel> hotelList=  hotelRepository.findByName( contractDto.getRoom_type_dto().getHotelDto().getName());
        if( hotelList.isEmpty() ){//check whether hotel exist or not
            return  null;
        }
        HotelDto hotelDto=modelMapper.map(hotelList.get( 0 ),HotelDto.class);

        List<RoomType> roomTypeList=roomTypeRepository.findByTypeAndHotelId( contractDto.getRoom_type_dto().getType(),hotelDto.getId() );
        if( roomTypeList.isEmpty() ){//check whether room type exist or not
            return  null;
        }
        RoomTypeDto roomTypeDto=modelMapper.map(roomTypeList.get( 0 ),RoomTypeDto.class);
        contractDto.setRoom_type_dto(roomTypeDto);
        contractDto.getRoom_type_dto().setHotelDto(hotelDto);
        contractDto.setDescription(contractDto.getDescription().toLowerCase());
        Boolean isValidDate=isValidateStartAndEndDate(contractDto.getStart_contract(),contractDto.getEnd_contract(),contractDto.getRoom_type_dto().getId());//check dates are valid

        Date current=new Date(System.currentTimeMillis());
        Boolean isValidStart=Share.isStartAndEndDateValid( current,contractDto.getStart_contract());
        Boolean isValidEnd=Share.isStartAndEndDateValid( current,contractDto.getEnd_contract());
        if(isValidDate && isValidStart && isValidEnd){//if dates are valid
            contractDto.setIsExpired( false );
            Contract contract=modelMapper.map( contractDto,Contract.class );
            return modelMapper.map(contractRepository.save(contract),ContractDto.class);}
        else{//if date are not valid
            return null;
        }
    }
    public Boolean isValidateStartAndEndDate( Date startDate, Date endDate,String roomTypeId )//check dates combination is valid or not and is there any exist contract cross with these dates
    {
        if(startDate.compareTo(endDate) > 0) {//start date come after end date
            return false;
      } else if(startDate.compareTo(endDate) < 0) {//start date come before end date
            List<Contract> contractList1=contractRepository.findIsNotExpired(roomTypeId);//check is there not expired contract under room type
            List<Contract> contractList2=contractRepository.dateValidate(startDate,endDate,roomTypeId);
            if( (contractList1.size()==1 && contractList2.size()==1) || (contractList1.size()==0 && contractList2.size()==0)){
                return true;
            }
            else{
                return false;
            }

      } else {//start date and end date equal
            return false;
        }
    }

}
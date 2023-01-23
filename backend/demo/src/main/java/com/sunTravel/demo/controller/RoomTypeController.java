package com.sunTravel.demo.controller;


import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.dto.SearchDto;
import com.sunTravel.demo.handler.Response;
import com.sunTravel.demo.service.ContractService;
import com.sunTravel.demo.service.RoomTypeService;
import com.sunTravel.demo.share.Share;
import com.sunTravel.demo.util.VarList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/roomType")
@CrossOrigin
public class RoomTypeController
{
    @Autowired
    private RoomTypeService roomTypeService;
    @Autowired
    private ContractService contractService;


    //    @GetMapping("/get")
//    public ResponseEntity<Response<List<RoomTypeDto>>> getAll(){
//        List<RoomTypeDto> roomTypeDtoList=roomTypeService.getAll();
//        Response<List<RoomTypeDto>> response=new Response<>( VarList.RSP_SUCCESS.getCode(),true,roomTypeDtoList );
//        return new ResponseEntity<>( response, HttpStatus.OK );//200
//
//    }
    @Operation(summary = "Insert room type", description = "Insert room type",tags = "Post")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201",description = "Successfully add",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = RoomTypeDto.class) ) })
           ,@ApiResponse(responseCode = "400",description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = RoomTypeDto.class) ) })})
    @PostMapping("/add")
    public ResponseEntity<Response<RoomTypeDto>> add( @RequestBody RoomTypeDto roomTypeDto ){
        String res=roomTypeService.insert(roomTypeDto);//Try to insert room type
        if(res.equals("00")){//If it is room type successfully added
            Response<RoomTypeDto> response=new Response<>(VarList.RSP_SUCCESS.getCode(),true);
            return new ResponseEntity<>( response,HttpStatus.CREATED);//201
        }
        else if( res.equals("06") )//If it is room type duplicated
        {
            Response<RoomTypeDto> response=new Response<>(VarList.RSP_DUPLICATED.getCode(),false);
            return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
        }
        else{
            Response<RoomTypeDto> response=new Response<>(VarList.RSP_BAD_DATA.getCode(),false);
            return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
        }

    }
    @Operation(summary = "Search the available room type", description = "Fetch the available room types that satisfy the condition ",tags = "Post")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Successfully fetch",content = @Content)
           ,@ApiResponse(responseCode = "400",description = "Invalid data",content = @Content)
           ,@ApiResponse(responseCode = "404",description = "Invalid id",content = @Content)})
    @PostMapping("/search")
    public ResponseEntity<Response<List<RoomTypeDto>>> search( @RequestBody SearchDto searchDto ){
        Boolean isDatesValid=Share.isStartAndEndDateValid(searchDto.getStart(),searchDto.getEnd());//check whether dates are valid
        searchDto.setLocation(Share.setFormatOfWords(searchDto.getLocation()));//set location's word format (each word start from capital)
        Boolean isValidLocation= Share.isValidLocation(searchDto.getLocation());//check location whether location is valid
        Response<List<RoomTypeDto>> response;
        if( !isDatesValid ){//if Dates are not valid
            response=new Response<>(VarList.RSP_BAD_DATA.getCode(),false,"Invalid start and end date!!" );
            return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
        }
        else if( !isValidLocation){//if Location is not valid
            response=new Response<>(VarList.RSP_BAD_DATA.getCode(),false,"Invalid location!!" );
            return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
        }
        else if( searchDto.getPairs().size()==0 )//if pairs list empty
        {
            response=new Response<>(VarList.RSP_BAD_DATA.getCode(),false,"Empty pairs!!" );
            return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
        }
        else{//if all search data valid
            List<RoomTypeDto> roomTypeDtoList=roomTypeService.search(searchDto);
            if(roomTypeDtoList==null){
                response=new Response<>(VarList.RSP_NO_DATA_FOUND.getCode(),false,"Invalid id!!" );
                return new ResponseEntity<>( response,HttpStatus.NOT_FOUND);//404
            }
            else{
                response=new Response<>(VarList.RSP_SUCCESS.getCode(),true,roomTypeDtoList);
                return new ResponseEntity<>( response,HttpStatus.OK );//200
            }

        }

    }
//    @DeleteMapping("/delete")
//    public ResponseEntity<Response<RoomTypeDto>> delete(@RequestBody RoomTypeDto roomTypeDto){
//        Boolean value=roomTypeService.delete( roomTypeDto );
//        Response response;
//        if(value){
//            response=new Response<>(VarList.RSP_SUCCESS.getCode(),true);
//            return new ResponseEntity<>( response,HttpStatus.OK );//200
//        }else {
//            response=new Response<>(VarList.RSP_FAIL.getCode(),false,"check id and name");
//            return new ResponseEntity<>( response,HttpStatus.NO_CONTENT);//204
//        }
//    }
//    @PutMapping("/update")
//    public ResponseEntity<Response<HotelDto>> update(@RequestBody RoomTypeDto roomTypeDto){
//        RoomTypeDto roomTypeDto1=roomTypeService.update( roomTypeDto );
//        Response<HotelDto> response;
//        if(roomTypeDto1==null){
//            response=new Response<>(VarList.RSP_NO_DATA_FOUND.getCode(),false,"Invalid id and name" );
//            return new ResponseEntity<>( response,HttpStatus.NO_CONTENT );//204
//        }
//        else{
//            response=new Response<>(VarList.RSP_SUCCESS.getCode(),true);
//            return  new ResponseEntity<>( response,HttpStatus.OK );//200
//        }
//    }
}

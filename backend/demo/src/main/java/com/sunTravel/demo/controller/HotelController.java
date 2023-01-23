package com.sunTravel.demo.controller;

import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.handler.Response;
import com.sunTravel.demo.service.HotelService;
import com.sunTravel.demo.service.RoomTypeService;
import com.sunTravel.demo.share.Share;
import com.sunTravel.demo.util.VarList;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/hotel")
@CrossOrigin
public class HotelController
{
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomTypeService roomTypeService;

    @Operation(summary = "Get hotel list", description = "Get hotel and there room types",tags = "Get")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Successfully fetch hotel list",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = HotelDto.class) ) })
    })
    @GetMapping("/get")
    public ResponseEntity<Response<List<HotelDto>>> getAll(){
        List<HotelDto> hotelDtoList=hotelService.getAll();//fetch list of hotels
        Response<List<HotelDto>> response=new Response<>( VarList.RSP_SUCCESS.getCode(),true,hotelDtoList );
        return new ResponseEntity<>( response, HttpStatus.OK );//200

    }
//    @GetMapping("/get/{hotelId}")
//    public ResponseEntity<Response<HotelDto>> get( @PathVariable String hotelId){
//        HotelDto hotelDto=hotelService.get(hotelId);
//        Response<HotelDto> response;
//        if(hotelDto==null){
//            response=new Response<>(VarList.RSP_NO_DATA_FOUND.getCode(),false,"Invalid id" );
//            return new ResponseEntity<>( response,HttpStatus.NOT_FOUND);//404
//        }
//        else{
//            response=new Response<>(VarList.RSP_SUCCESS.getCode(),true,hotelDto);
//            return new ResponseEntity<>( response,HttpStatus.OK );//200
//        }
//    }

    @Operation(summary = "Insert hotel", description = "Insert hotel",tags = "Post")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201",description = "Successfully add",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = HotelDto.class) ) })
           ,@ApiResponse(responseCode = "400",description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = HotelDto.class) ) })})
    @PostMapping("/add")
    public ResponseEntity<Response<HotelDto>> add( @RequestBody HotelDto hotelDto ){
        hotelDto.setLocation(Share.setFormatOfWords(hotelDto.getLocation()));//set format of location
        if(Share.isValidLocation(hotelDto.getLocation())){//if is it valid location
            HotelDto hotelDto1=hotelService.insert( hotelDto );
            if(hotelDto1!=null){
                Response<HotelDto> response=new Response<>(VarList.RSP_SUCCESS.getCode(),true);
                return new ResponseEntity<>( response,HttpStatus.CREATED);//201
            }
            else{
                Response<HotelDto> response=new Response<>(VarList.RSP_DUPLICATED.getCode(),false);
                return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
            }
        }
        else{//if is it not valid location
            Response<HotelDto> response=new Response<>(VarList.RSP_BAD_DATA.getCode(),false,"Invalid location!!" );
            return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
        }

    }
//    @DeleteMapping("/delete")
//    public ResponseEntity<Response<HotelDto>> delete(@RequestBody HotelDto hotelDto){
//        Boolean value=hotelService.delete( hotelDto );
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
//    public ResponseEntity<Response<HotelDto>> update(@RequestBody HotelDto hotelDto){
//        HotelDto hotelDto1=hotelService.update( hotelDto );
//        Response<HotelDto> response;
//        if(hotelDto1==null){
//            response=new Response<>(VarList.RSP_NO_DATA_FOUND.getCode(),false,"Invalid id and name" );
//            return new ResponseEntity<>( response,HttpStatus.NO_CONTENT );//204
//        }
//        else{
//            response=new Response<>(VarList.RSP_SUCCESS.getCode(),true);
//            return  new ResponseEntity<>( response,HttpStatus.OK );//200
//        }
//    }
}

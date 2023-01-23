package com.sunTravel.demo.controller;

import com.sunTravel.demo.dto.ContractDto;
import com.sunTravel.demo.dto.FilterDto;
import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.handler.Response;
import com.sunTravel.demo.service.ContractService;
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
@RequestMapping(value = "api/v1/contract")
@CrossOrigin
public class ContractController
{
    @Autowired
    private ContractService contractService;
    @Operation(summary = "Fetch contracts ", description = "Fetch contracts under room type",tags = "Post")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Successfully fetch",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = RoomTypeDto.class) ) })
            ,@ApiResponse(responseCode = "404",description = "Not found",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = RoomTypeDto.class) ) })})

    @PostMapping("/search")
    public ResponseEntity<Response<RoomTypeDto>> get( @RequestBody FilterDto filterDto ){
        RoomTypeDto contractDto=contractService.search(filterDto.getE1(),filterDto.getE2());
        Response<RoomTypeDto> response;
        if(contractDto==null){
            response=new Response<>(VarList.RSP_NO_DATA_FOUND.getCode(),false,"Invalid id" );
            return new ResponseEntity<>( response,HttpStatus.NOT_FOUND);//404
        }
        else{
            response=new Response<>(VarList.RSP_SUCCESS.getCode(),true,contractDto);
            return new ResponseEntity<>( response,HttpStatus.OK );//200
        }


    }
    @Operation(summary = "Insert contract", description = "Insert contract",tags = "Post")
    @ApiResponses(value={
            @ApiResponse(responseCode = "201",description = "Successfully add",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = ContractDto.class) ) })
            ,@ApiResponse(responseCode = "400",description = "Bad request",content = {@Content(mediaType = "application/json",schema = @Schema(implementation = ContractDto.class) ) })})
    @PostMapping("/add")
    public ResponseEntity<Response<ContractDto>> add( @RequestBody ContractDto contractDto ){
        ContractDto contractDto1=contractService.insert( contractDto );
        if(contractDto1!=null){
            Response<ContractDto> response=new Response<>(VarList.RSP_SUCCESS.getCode(),true,contractDto1);
            return new ResponseEntity<>( response,HttpStatus.CREATED);//201
        }
        else{
            Response<ContractDto> response=new Response<>(VarList.RSP_DUPLICATED.getCode(),false);
            return new ResponseEntity<>( response,HttpStatus.BAD_REQUEST);//400
        }

    }
    @Operation(summary = "Delete contract", description = "Delete the contract when we send the id",tags = "Post")
    @ApiResponses(value={
            @ApiResponse(responseCode = "200",description = "Successfully delete",content = @Content )
            ,@ApiResponse(responseCode = "204",description = "No content",content = @Content)})

    @PostMapping("/delete")
    public ResponseEntity<Response<ContractDto>> delete(@RequestBody ContractDto contractDto){
        Boolean value=contractService.delete( contractDto.getId());
        Response response;
        if(value){
            response=new Response<>(VarList.RSP_SUCCESS.getCode(),true);
            return new ResponseEntity<>( response,HttpStatus.OK );//200
        }else {
            response=new Response<>(VarList.RSP_FAIL.getCode(),false,"check id and name");
            return new ResponseEntity<>( response,HttpStatus.NO_CONTENT);//204
        }
    }

}

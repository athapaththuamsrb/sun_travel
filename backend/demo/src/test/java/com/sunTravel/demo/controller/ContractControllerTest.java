package com.sunTravel.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sunTravel.demo.dto.ContractDto;
import com.sunTravel.demo.dto.FilterDto;
import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.service.ContractService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( ContractController.class )
class ContractControllerTest
{
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;


    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();
    HotelDto hotelDto1=new HotelDto("c0a80214-85c5-1b38-8185-c57cae670000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
    HotelDto hotelDto2=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Taj Samudra","Colombo","0775226866",new ArrayList<RoomTypeDto>() );

    @Test
    void get_Successful() throws Exception
    {
        RoomTypeDto roomTypeDto=new RoomTypeDto("",null,"single room",2,new ArrayList<>());
        FilterDto filterDto=FilterDto.builder().E1( "Hilton" ).E2( "single room" ).build();
        String content =objectWriter.writeValueAsString( filterDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/contract/search").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( contractService.search(filterDto.getE1(),filterDto.getE2())).willReturn( roomTypeDto);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isOk() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("00" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(true)))
               .andExpect( jsonPath( "$.data.type",Matchers.is("single room") ) );
    }
    @Test
    void get_fail() throws Exception
    {

        FilterDto filterDto=FilterDto.builder().E1( "Hilton" ).E2( "single room" ).build();
        String content =objectWriter.writeValueAsString( filterDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/contract/search").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( contractService.search(filterDto.getE1(),filterDto.getE2())).willReturn( null);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isNotFound() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("01" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }

    @Test
    void add_fail() throws Exception
    {
        HotelDto hotelDto=new HotelDto("","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        RoomTypeDto roomTypeDto=new RoomTypeDto("",hotelDto,"single room",2,new ArrayList<>());
        ContractDto contractDto=ContractDto.builder().start_contract(new Date("2023/01/23")).end_contract(new Date("2023/02/28")  ).count(100).price( BigDecimal.valueOf( 45000 ) ).room_type_dto( roomTypeDto ).build();
        String content =objectWriter.writeValueAsString( contractDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/contract/add").
                                                                                               contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( contractService.insert( contractDto )).willReturn( null);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
                   .andExpect( jsonPath( "$.code", Matchers.is("06" )))
                   .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
                   .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );

    }
    @Test
    void add_successfully() throws Exception
    {
        HotelDto hotelDto=new HotelDto("","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        RoomTypeDto roomTypeDto=new RoomTypeDto("",hotelDto,"single room",2,new ArrayList<>());
        ContractDto contractDto=ContractDto.builder().start_contract(new Date("2023/01/23")).end_contract(new Date("2023/02/28")  ).count(100).price( BigDecimal.valueOf( 45000 ) ).room_type_dto( roomTypeDto ).build();
        String content =objectWriter.writeValueAsString( contractDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/contract/add").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( contractService.insert( contractDto )).willReturn( contractDto);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isCreated() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("00" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(true)))
               .andExpect( jsonPath( "$.data.count",Matchers.is(100) ) );

    }


    @Test
    void delete_fail() throws Exception
    {

        ContractDto contractDto=ContractDto.builder().id( "c0a80214-85c5-1b38-8185-c57f025b0002" ).build();
        String content =objectWriter.writeValueAsString( contractDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/contract/delete").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( contractService.delete( contractDto.getId())).willReturn( false);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isNoContent() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("10" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }
    @Test
    void delete_success() throws Exception
    {

        ContractDto contractDto=ContractDto.builder().id( "c0a80214-85c5-1b38-8185-c57f025b0002" ).build();
        String content =objectWriter.writeValueAsString( contractDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/contract/delete").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( contractService.delete( contractDto.getId())).willReturn( true);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isOk() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("00" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(true)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }
}
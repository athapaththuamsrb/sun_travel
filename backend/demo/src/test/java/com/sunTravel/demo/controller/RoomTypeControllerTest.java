package com.sunTravel.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.dto.SearchDto;
import com.sunTravel.demo.service.ContractService;
import com.sunTravel.demo.service.RoomTypeService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( RoomTypeController.class )
class RoomTypeControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RoomTypeService roomTypeService;
    @MockBean
    private ContractService contractService;


    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();
    RoomDto roomDto1=new RoomDto(1,2);
    RoomDto roomDto2=new RoomDto(2,2);
    @Test
    void add_SuccessFullyRoomType () throws Exception
    {
        RoomTypeDto roomTypeDto=RoomTypeDto.builder().type("single bed").max_adults( 3 ).build();

        BDDMockito.given( roomTypeService.insert( roomTypeDto )).willReturn( "00");

        String content =objectWriter.writeValueAsString( roomTypeDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/add").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isCreated() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("00" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(true)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );

    }
    @Test
    void add_DuplicateRoomType() throws Exception
    {
        RoomTypeDto roomTypeDto=RoomTypeDto.builder().type("single bed").max_adults( 3 ).build();

        BDDMockito.given( roomTypeService.insert( roomTypeDto )).willReturn( "06");

        String content =objectWriter.writeValueAsString( roomTypeDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/add").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("06" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );

    }
    @Test
    void add_BadDataRoomType() throws Exception
    {
        RoomTypeDto roomTypeDto=RoomTypeDto.builder().type("single bed").max_adults( 3 ).build();

        BDDMockito.given( roomTypeService.insert( roomTypeDto )).willReturn( "07");

        String content =objectWriter.writeValueAsString( roomTypeDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/add").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("07" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }

    @Test
    void search_NotValidDate() throws Exception
    {
        SearchDto searchDto=SearchDto.builder().start( new Date("2023/01/25")).end( new Date("2023/01/20")).location( "Colombo" ).pairs(new ArrayList<>( Arrays.asList(roomDto1,roomDto2))  ).build();
        String content =objectWriter.writeValueAsString( searchDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/search").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("07" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }
    @Test
    void search_NotValidLocation() throws Exception
    {
        SearchDto searchDto=SearchDto.builder().start( new Date("2023/01/20")).end( new Date("2023/01/22")).location( "Colombx" ).pairs(new ArrayList<>( Arrays.asList(roomDto1,roomDto2))  ).build();
        String content =objectWriter.writeValueAsString( searchDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/search").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("07" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );

    }
    @Test
    void search_EmptyPairs() throws Exception
    {
        SearchDto searchDto=SearchDto.builder().start( new Date("2023/01/25")).end( new Date("2023/01/20")).location( "Colombo" ).pairs(new ArrayList<>()).build();
        String content =objectWriter.writeValueAsString( searchDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/search").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("07" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );


    }
    @Test
    void search_nullResult() throws Exception
    {
        SearchDto searchDto=SearchDto.builder().start( new Date("2023/01/20")).end( new Date("2023/01/25")).location( "Colombo" ).pairs(new ArrayList<>( Arrays.asList(roomDto1,roomDto2))  ).build();
        String content =objectWriter.writeValueAsString( searchDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/search").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( roomTypeService.search(searchDto)).willReturn( null);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isNotFound() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("01" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );

    }

    @Test
    void search_success() throws Exception
    {
        HotelDto hotelDto=new HotelDto("c0a80214-85c5-1b38-8185-c57cae670000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
        RoomTypeDto roomTypeDto=new RoomTypeDto("",hotelDto,"single bed",2,new ArrayList<>());
        List<RoomTypeDto> roomTypeDtoList=new ArrayList<>(Arrays.asList( roomTypeDto ));
        SearchDto searchDto=SearchDto.builder().start( new Date("2023/01/20")).end( new Date("2023/01/25")).location( "Colombo" ).pairs(new ArrayList<>( Arrays.asList(roomDto1,roomDto2))  ).build();
        String content =objectWriter.writeValueAsString( searchDto );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/roomType/search").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        BDDMockito.given( roomTypeService.search(searchDto)).willReturn( roomTypeDtoList);

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isOk() )//$-> mean root
               .andExpect( jsonPath( "$.code", Matchers.is("00" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(true)))
               .andExpect( jsonPath( "$.data[0].type",Matchers.is("single bed")) );


    }


}
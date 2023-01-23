package com.sunTravel.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.sunTravel.demo.dto.HotelDto;
import com.sunTravel.demo.dto.RoomTypeDto;
import com.sunTravel.demo.service.HotelService;
import com.sunTravel.demo.service.RoomTypeService;
import com.sunTravel.demo.share.Share;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest( HotelController.class )
class HotelControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private HotelService hotelService;
    @MockBean
    private RoomTypeService roomTypeService;
    ObjectMapper objectMapper=new ObjectMapper();
    ObjectWriter objectWriter=objectMapper.writer();
    HotelDto hotelDto1=new HotelDto("c0a80214-85c5-1b38-8185-c57cae670000","Hilton","Colombo","0768294279",new ArrayList<RoomTypeDto>() );
    HotelDto hotelDto2=new HotelDto("c0a83414-85c3-1656-8185-c3175da70000","Taj Samudra","Colombo","0775226866",new ArrayList<RoomTypeDto>() );

    @Test
    void getAll() throws Exception
    {
        List<HotelDto> hotelDtoList=new ArrayList<>( Arrays.asList(hotelDto1,hotelDto2));
        BDDMockito.given( hotelService.getAll()).willReturn( hotelDtoList );
        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/hotel/get").
                                      contentType( MediaType.APPLICATION_JSON)
        ).andExpect( status().isOk() )//$-> mean root
                .andExpect( jsonPath( "$.code",Matchers.is("00" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(true)))
                .andExpect( jsonPath( "$.data",hasSize( 2 ) ) )
                .andExpect( jsonPath( "$.data[0].name",Matchers.is( "Hilton" ) ) );
//                .andDo( r->{
//                    System.out.println(r.getResponse().getContentAsString());
//                } );
//        {"code":"00","isSuccess":true,"description":null,"data":[{"id":"c0a80214-85c5-1b38-8185-c57cae670000","name":"Hilton","location":"Colombo","contact":"0768294279","roomTypeList":[]},{"id":"c0a83414-85c3-1656-8185-c3175da70000","name":"Taj Samudra","location":"Colombo","contact":"0775226866","roomTypeList":[]}]}

    }

    @Test
    void add_NotValidLocationHotel() throws Exception
    {
        HotelDto hotelDto3=HotelDto.builder().name("Cinnamon Red").location( "xxxxx" ).contact( "0768294279" ).build();

//        BDDMockito.given( Share.setFormatOfWords("xxxxx")).willReturn( "Xxxxx");
//        BDDMockito.given( Share.isValidLocation("Xxxxx")).willReturn( false);

        String content =objectWriter.writeValueAsString( hotelDto3 );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/hotel/add").
                              contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
               .andExpect( jsonPath( "$.code",Matchers.is("07" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }
    @Test
    void add_DuplicateHotel() throws Exception
    {
        HotelDto hotelDto3=HotelDto.builder().name("Cinnamon Red").location( "Colombo" ).contact( "0768294279" ).build();

        BDDMockito.given( hotelService.insert( hotelDto3 )).willReturn( null);
//        BDDMockito.given( Share.isValidLocation("Xxxxx")).willReturn( false);

        String content =objectWriter.writeValueAsString( hotelDto3 );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/hotel/add").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isBadRequest() )//$-> mean root
               .andExpect( jsonPath( "$.code",Matchers.is("06" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(false)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }
    @Test
    void add_SuccessHotel() throws Exception
    {
        HotelDto hotelDto3=HotelDto.builder().name("Cinnamon Red").location( "Colombo" ).contact( "0768294279" ).build();

        BDDMockito.given( hotelService.insert( hotelDto3 )).willReturn( hotelDto3);
//        BDDMockito.given( Share.isValidLocation("Xxxxx")).willReturn( false);

        String content =objectWriter.writeValueAsString( hotelDto3 );
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder= MockMvcRequestBuilders.post("/api/v1/hotel/add").
                                                                                           contentType( MediaType.APPLICATION_JSON).content( content );

        mockMvc.perform(mockHttpServletRequestBuilder).andExpect( status().isCreated() )//$-> mean root
               .andExpect( jsonPath( "$.code",Matchers.is("00" )))
               .andExpect( jsonPath( "$.isSuccess",Matchers.is(true)))
               .andExpect( jsonPath( "$.data",Matchers.nullValue() ) );
    }
}
package com.sunTravel.demo.dto;
import com.sunTravel.demo.entity.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class HotelDto
{
    private String id;
    private String name;
    private  String location;
    private String contact;
    private List<RoomTypeDto> roomTypeList;
}

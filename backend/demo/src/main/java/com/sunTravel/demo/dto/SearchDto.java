package com.sunTravel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SearchDto
{
    private Date start;
    private Date end;
    private String location;

    private List<RoomDto> pairs;

}

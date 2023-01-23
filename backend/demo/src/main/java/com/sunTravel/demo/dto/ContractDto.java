package com.sunTravel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ContractDto
{
    private String id;
    private RoomTypeDto room_type_dto;
    private Date start_contract;
    private Date end_contract;
    private BigDecimal price;
    private Integer count;
    private Boolean  isExpired;
    private Integer markup;
    private String description;
    private List<RoomBookingDto> roomBookingDtoList;
}

package com.sunTravel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomBookingDto
{    private String id;
    private ContractDto contract;
    private Date start_date;
    private Date end_date;
    private Integer count;
}

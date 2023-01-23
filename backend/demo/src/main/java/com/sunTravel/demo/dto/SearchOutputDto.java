package com.sunTravel.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SearchOutputDto
{
    private String  name;
    private String contact;
    private String type;
    private int max_adults;
    private int count;
    private Date start_contract;
    private Date end_contract;
}

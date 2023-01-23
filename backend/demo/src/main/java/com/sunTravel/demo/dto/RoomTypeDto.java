package com.sunTravel.demo.dto;


import jakarta.persistence.AssociationOverride;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RoomTypeDto
{
    private String id;
    private HotelDto hotelDto;
    private String type;
    private Integer max_adults;
    private List<ContractDto> contractList;

    public RoomTypeDto( String id )
    {
        this.id=id;
    }

}

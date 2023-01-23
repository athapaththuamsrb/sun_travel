package com.sunTravel.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomType
{
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator",
            parameters = {
                    @Parameter(
                            name = "uuid_gen_strategy_class",
                            value = "org.hibernate.id.uuid.CustomVersionOneStrategy"
                    )
            }
    )
    @Column(name="id",nullable = false,updatable = false)
    private String id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false )
    @JoinColumn(name="hotel_id")
    private Hotel hotel;
    @Column(name="type",nullable = false,updatable = false,length = 100)
    private String type;


    @Column(name="max_adults",nullable = false,updatable = false)
    private Integer max_adults;
    @OneToMany(cascade ={ CascadeType.ALL})
    @Fetch( FetchMode.JOIN )
    @JoinColumn(name="room_type_id",referencedColumnName = "id")//name->Contract using name for RoomType id,referencedColumnName->which column Contract refer in this roomType table
    private List<Contract> contractList;
}

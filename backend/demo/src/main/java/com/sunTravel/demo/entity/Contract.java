package com.sunTravel.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.math.BigDecimal;
import java.util.Date;
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Contract
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
    private String id;

    @ManyToOne(fetch = FetchType.LAZY,optional = false )
    @JoinColumn(name="room_type_id")
    private RoomType room_type;
    @Temporal( TemporalType.DATE )
    @Column(name="start_contract",nullable = false,updatable = false)
    private Date start_contract ;
    @Temporal( TemporalType.DATE )
    @Column(name="end_contract",nullable = false,updatable = false)
    private Date end_contract;
    @Column(name="price",nullable = false,updatable = false,precision = 8,scale = 2)
    private BigDecimal price;
    @Column(name="count",nullable = false,updatable = false)
    private Integer count;
    @Column(name="markup",nullable = false,updatable = false)
    private Integer markup;

    @Column(name="description",nullable = false,updatable = false,length = 2000)
    private String description;
    @Column(name="isExpired",nullable = false,updatable = true)
    private Boolean  isExpired=false;
}

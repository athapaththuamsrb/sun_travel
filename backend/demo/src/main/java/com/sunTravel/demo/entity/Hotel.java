package com.sunTravel.demo.entity;

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
import lombok.Builder;
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
public class Hotel
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
    @Column(name="name",nullable = false,updatable = false,unique = true)
    private String name;
    @Column(name="location",nullable = false,updatable = false)
    private  String location;
    @Column(name="contact",nullable = false,updatable = false)
    private String contact;

    @OneToMany(cascade ={ CascadeType.ALL})
    @Fetch( FetchMode.JOIN )
    @JoinColumn(name="hotel_id",referencedColumnName = "id")//name->RoomType using name for Hotel id,referencedColumnName->which column RoomType refer in this hotel table
    private List<RoomType> roomTypeList;
}

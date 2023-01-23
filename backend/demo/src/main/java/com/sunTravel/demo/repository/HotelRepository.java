package com.sunTravel.demo.repository;

import com.sunTravel.demo.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface HotelRepository extends JpaRepository<Hotel,String>
{
    List<Hotel> findByName( String name);
    @Override
    Optional<Hotel> findById( String s );
    Hotel findByIdAndName( String id, String name );
    List<Hotel> deleteByIdAndName( String id, String name );


}

package com.sunTravel.demo.repository;


import com.sunTravel.demo.dto.SearchOutputDto;
import com.sunTravel.demo.entity.Contract;
import com.sunTravel.demo.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType,String>
{
    @Override
    Optional<RoomType> findById( String s );
    List<RoomType> findByTypeAndHotelId(String type ,String hotelId);
    RoomType deleteByHotelIdAndType(String hotelId, String type );
    @Query(value = "SELECT r.* FROM hotel AS h INNER JOIN room_type AS r ON h.id = r.hotel_id INNER JOIN contract AS c ON r.id = c.room_type_id WHERE h.location=?1 AND c.is_expired=false AND Date(c.start_contract)<?2 AND Date(c.end_contract)>?3 AND r.max_adults>=?4 AND c.count>=?5",nativeQuery = true)
    List<RoomType> findAvailableRoomType( String location, Date startDate, Date endDate, Integer maxAdults, Integer numberOfRoom);
}

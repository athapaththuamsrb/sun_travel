package com.sunTravel.demo.repository;

import com.sunTravel.demo.entity.Contract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract,String>
{
    @Override
    Optional<Contract> findById( String s );

    @Query(value = "SELECT * FROM contract WHERE  Date(start_contract) <?1 AND Date(start_contract) <?2 AND Date(end_contract) <?1 AND Date(end_contract) <?2 AND is_expired=false AND room_type_id=?3 ",nativeQuery = true)
    List<Contract> dateValidate(Date start_contract,Date   end_contract,String room_type_id );
    @Query(value = "SELECT * FROM contract WHERE  is_expired=false AND room_type_id=?1",nativeQuery = true)
    List<Contract> findIsNotExpired( String roomTypeId );

}

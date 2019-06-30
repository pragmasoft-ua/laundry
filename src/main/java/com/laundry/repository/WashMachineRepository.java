package com.laundry.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.laundry.domain.WashMachine;


/**
 * Spring Data JPA repository for the WashMachine entity.
 */
@Repository
public interface WashMachineRepository extends JpaRepository<WashMachine,Long> {

	 @Query(value =
		 "SELECT * FROM WASH_MACHINE WM " +
		 "WHERE WM.AVAILABLE IS TRUE AND " + 
		 "ID NOT IN " +
		 "(SELECT MACHINES_ID FROM JHI_ORDER AS O JOIN ORDER_MACHINE AS OM " +
		 "ON O.ID=OM.ORDERS_ID "+
		 "WHERE " +
		 "START_ON < ?2 " +
		 "AND " +
		 "DATE_ADD(START_ON, INTERVAL DURATION_HOURS HOUR) >= ?1)"
	 	, nativeQuery = true)
	 List<WashMachine> findAllAvailableBetween(ZonedDateTime from, ZonedDateTime to);	
	
}

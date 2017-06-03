package com.laundry.repository;

import com.laundry.domain.User;
import com.laundry.domain.WashPrice;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WashPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WashPriceRepository extends JpaRepository<WashPrice,Long> {


    /**
     * @return current wash price
     */
    Optional<WashPrice> findOneByEfferctiveToIsNull();
	
}

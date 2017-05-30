package com.laundry.repository;

import com.laundry.domain.WashPrice;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WashPrice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WashPriceRepository extends JpaRepository<WashPrice,Long> {

}

package com.laundry.repository;

import com.laundry.domain.WashMachine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the WashMachine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WashMachineRepository extends JpaRepository<WashMachine,Long> {

}

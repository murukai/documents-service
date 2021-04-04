package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Holiday;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Holiday entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {}

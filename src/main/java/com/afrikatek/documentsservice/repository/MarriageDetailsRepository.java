package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.MarriageDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MarriageDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarriageDetailsRepository extends JpaRepository<MarriageDetails, Long> {}

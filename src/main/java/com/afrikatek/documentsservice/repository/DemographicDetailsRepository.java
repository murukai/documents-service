package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.DemographicDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemographicDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemographicDetailsRepository extends JpaRepository<DemographicDetails, Long> {}

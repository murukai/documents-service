package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Applicant;
import com.afrikatek.documentsservice.domain.DemographicDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DemographicDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DemographicDetailsRepository extends JpaRepository<DemographicDetails, Long> {
    Optional<DemographicDetails> findByApplicant(Applicant applicant);

    @Query(
        "select demographicDetails from DemographicDetails demographicDetails where demographicDetails.applicant.user.login = ?#{principal.username}"
    )
    Page<DemographicDetails> findByApplicantAsCurrentUser(Pageable pageable);
}

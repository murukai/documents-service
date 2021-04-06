package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Applicant;
import com.afrikatek.documentsservice.domain.MarriageDetails;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MarriageDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MarriageDetailsRepository extends JpaRepository<MarriageDetails, Long> {
    Optional<MarriageDetails> findByApplicant(Applicant applicant);

    @Query(
        "select marriageDetails from MarriageDetails marriageDetails where marriageDetails.applicant.user.login = ?#{principal.username}"
    )
    Page<List<MarriageDetails>> findByApplicantAsCurrentUser(Pageable pageable);
}

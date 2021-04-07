package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Applicant;
import com.afrikatek.documentsservice.domain.NextOfKeen;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NextOfKeen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextOfKeenRepository extends JpaRepository<NextOfKeen, Long> {
    Optional<NextOfKeen> findByApplicant(Applicant applicant);

    @Query("select nextOfKeen from NextOfKeen nextOfKeen where nextOfKeen.applicant.user.login = ?#{principal.username}")
    Page<NextOfKeen> findByApplicantAsCurrentUser(Pageable pageable);
}

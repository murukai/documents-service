package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Applicant;
import com.afrikatek.documentsservice.domain.Guardian;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Guardian entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {
    Optional<Guardian> findByApplicant(Applicant applicant);

    @Query("select guardian from Guardian guardian WHERE guardian.applicant.user.login = ?#{principal.username}")
    Page<List<Guardian>> findByApplicantAsCurrentUser(Pageable pageable);
}

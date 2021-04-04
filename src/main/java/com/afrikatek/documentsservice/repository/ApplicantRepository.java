package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Applicant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Applicant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    @Query("select applicant from Applicant applicant where applicant.user.login = ?#{principal.username}")
    List<Applicant> findByUserIsCurrentUser();
}

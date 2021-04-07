package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Applicant;
import com.afrikatek.documentsservice.domain.Declaration;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Declaration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {
    Optional<Declaration> findByApplicant(Applicant applicant);

    @Query("select declaration from Declaration declaration where declaration.applicant.user.login = ?#{principal.username}")
    Page<Declaration> findByApplicantAsCurrentUser(Pageable pageable);
}

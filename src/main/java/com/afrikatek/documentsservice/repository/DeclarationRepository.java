package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Declaration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Declaration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DeclarationRepository extends JpaRepository<Declaration, Long> {}

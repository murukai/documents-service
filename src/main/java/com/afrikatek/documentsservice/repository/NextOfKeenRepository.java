package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.NextOfKeen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NextOfKeen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NextOfKeenRepository extends JpaRepository<NextOfKeen, Long> {}

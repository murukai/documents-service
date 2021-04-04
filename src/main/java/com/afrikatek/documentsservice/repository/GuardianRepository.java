package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Guardian;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Guardian entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GuardianRepository extends JpaRepository<Guardian, Long> {}

package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Passport;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Passport entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PassportRepository extends JpaRepository<Passport, Long> {}

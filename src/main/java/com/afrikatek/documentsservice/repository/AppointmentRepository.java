package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Appointment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Appointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {}

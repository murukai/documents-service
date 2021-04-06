package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.AppointmentSlot;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AppointmentSlot entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentSlotRepository extends JpaRepository<AppointmentSlot, Long> {}

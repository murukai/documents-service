package com.afrikatek.documentsservice.repository;

import com.afrikatek.documentsservice.domain.Address;
import com.afrikatek.documentsservice.domain.Applicant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByApplicant(Applicant applicant);

    @Query("select address from Address address where address.applicant.user.login = ?#{principal.username}")
    Page<List<Address>> findByApplicantAsCurrentUser(Pageable pageable);
}

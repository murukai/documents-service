package com.afrikatek.documentsservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsservice.domain.NextOfKeen} entity.
 */
public class NextOfKeenDTO implements Serializable {

    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private String relationshipToApplicant;

    private ApplicantDTO applicant;

    private AddressDTO address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRelationshipToApplicant() {
        return relationshipToApplicant;
    }

    public void setRelationshipToApplicant(String relationshipToApplicant) {
        this.relationshipToApplicant = relationshipToApplicant;
    }

    public ApplicantDTO getApplicant() {
        return applicant;
    }

    public void setApplicant(ApplicantDTO applicant) {
        this.applicant = applicant;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOfKeenDTO)) {
            return false;
        }

        NextOfKeenDTO nextOfKeenDTO = (NextOfKeenDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, nextOfKeenDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOfKeenDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", relationshipToApplicant='" + getRelationshipToApplicant() + "'" +
            ", applicant=" + getApplicant() +
            ", address=" + getAddress() +
            "}";
    }
}

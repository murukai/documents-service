package com.afrikatek.documentsservice.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsservice.domain.Guardian} entity.
 */
public class GuardianDTO implements Serializable {

    private Long id;

    @NotNull
    private String fullName;

    @NotNull
    private String idNumber;

    @NotNull
    private String relationshipToApplicant;

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

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getRelationshipToApplicant() {
        return relationshipToApplicant;
    }

    public void setRelationshipToApplicant(String relationshipToApplicant) {
        this.relationshipToApplicant = relationshipToApplicant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GuardianDTO)) {
            return false;
        }

        GuardianDTO guardianDTO = (GuardianDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, guardianDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GuardianDTO{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", relationshipToApplicant='" + getRelationshipToApplicant() + "'" +
            "}";
    }
}

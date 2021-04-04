package com.afrikatek.documentsservice.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsservice.domain.Passport} entity.
 */
public class PassportDTO implements Serializable {

    private Long id;

    @NotNull
    private String passportNumber;

    @NotNull
    private String issuedAt;

    @NotNull
    private Instant issuedDate;

    @NotNull
    private String lossDescription;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Instant issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getLossDescription() {
        return lossDescription;
    }

    public void setLossDescription(String lossDescription) {
        this.lossDescription = lossDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PassportDTO)) {
            return false;
        }

        PassportDTO passportDTO = (PassportDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, passportDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PassportDTO{" +
            "id=" + getId() +
            ", passportNumber='" + getPassportNumber() + "'" +
            ", issuedAt='" + getIssuedAt() + "'" +
            ", issuedDate='" + getIssuedDate() + "'" +
            ", lossDescription='" + getLossDescription() + "'" +
            "}";
    }
}

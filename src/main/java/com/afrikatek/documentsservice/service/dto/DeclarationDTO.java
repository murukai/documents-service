package com.afrikatek.documentsservice.service.dto;

import com.afrikatek.documentsservice.domain.enumeration.CitizenOptions;
import com.afrikatek.documentsservice.domain.enumeration.ForeignDocumentsOptions;
import com.afrikatek.documentsservice.domain.enumeration.PassportOptions;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsservice.domain.Declaration} entity.
 */
public class DeclarationDTO implements Serializable {

    private Long id;

    @NotNull
    private CitizenOptions citizen;

    @NotNull
    private PassportOptions passport;

    @NotNull
    private ForeignDocumentsOptions foreignPassport;

    @NotNull
    private String passportNumber;

    @NotNull
    private ForeignDocumentsOptions renouncedCitizenship;

    @NotNull
    private ForeignDocumentsOptions passportSurrendered;

    @NotNull
    private String foreignPassportNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CitizenOptions getCitizen() {
        return citizen;
    }

    public void setCitizen(CitizenOptions citizen) {
        this.citizen = citizen;
    }

    public PassportOptions getPassport() {
        return passport;
    }

    public void setPassport(PassportOptions passport) {
        this.passport = passport;
    }

    public ForeignDocumentsOptions getForeignPassport() {
        return foreignPassport;
    }

    public void setForeignPassport(ForeignDocumentsOptions foreignPassport) {
        this.foreignPassport = foreignPassport;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public ForeignDocumentsOptions getRenouncedCitizenship() {
        return renouncedCitizenship;
    }

    public void setRenouncedCitizenship(ForeignDocumentsOptions renouncedCitizenship) {
        this.renouncedCitizenship = renouncedCitizenship;
    }

    public ForeignDocumentsOptions getPassportSurrendered() {
        return passportSurrendered;
    }

    public void setPassportSurrendered(ForeignDocumentsOptions passportSurrendered) {
        this.passportSurrendered = passportSurrendered;
    }

    public String getForeignPassportNumber() {
        return foreignPassportNumber;
    }

    public void setForeignPassportNumber(String foreignPassportNumber) {
        this.foreignPassportNumber = foreignPassportNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeclarationDTO)) {
            return false;
        }

        DeclarationDTO declarationDTO = (DeclarationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, declarationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DeclarationDTO{" +
            "id=" + getId() +
            ", citizen='" + getCitizen() + "'" +
            ", passport='" + getPassport() + "'" +
            ", foreignPassport='" + getForeignPassport() + "'" +
            ", passportNumber='" + getPassportNumber() + "'" +
            ", renouncedCitizenship='" + getRenouncedCitizenship() + "'" +
            ", passportSurrendered='" + getPassportSurrendered() + "'" +
            ", foreignPassportNumber='" + getForeignPassportNumber() + "'" +
            "}";
    }
}

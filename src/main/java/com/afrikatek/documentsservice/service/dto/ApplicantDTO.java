package com.afrikatek.documentsservice.service.dto;

import com.afrikatek.documentsservice.domain.enumeration.Gender;
import com.afrikatek.documentsservice.domain.enumeration.MaritalStatus;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsservice.domain.Applicant} entity.
 */
public class ApplicantDTO implements Serializable {

    private Long id;

    @NotNull
    private String surname;

    @NotNull
    private String otherNames;

    @NotNull
    private String maidenName;

    @NotNull
    private Boolean changedName;

    private Gender gender;

    private MaritalStatus maritalStatus;

    @NotNull
    private LocalDate dateOfBirth;

    @NotNull
    private String idNumber;

    @NotNull
    private String birthEntryNumber;

    private DemographicDetailsDTO democraphicDetails;

    private DeclarationDTO declaration;

    private GuardianDTO guardian;

    private UserDTO user;

    private AppointmentSlotDTO appointmentSlot;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return otherNames;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public Boolean getChangedName() {
        return changedName;
    }

    public void setChangedName(Boolean changedName) {
        this.changedName = changedName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBirthEntryNumber() {
        return birthEntryNumber;
    }

    public void setBirthEntryNumber(String birthEntryNumber) {
        this.birthEntryNumber = birthEntryNumber;
    }

    public DemographicDetailsDTO getDemocraphicDetails() {
        return democraphicDetails;
    }

    public void setDemocraphicDetails(DemographicDetailsDTO democraphicDetails) {
        this.democraphicDetails = democraphicDetails;
    }

    public DeclarationDTO getDeclaration() {
        return declaration;
    }

    public void setDeclaration(DeclarationDTO declaration) {
        this.declaration = declaration;
    }

    public GuardianDTO getGuardian() {
        return guardian;
    }

    public void setGuardian(GuardianDTO guardian) {
        this.guardian = guardian;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public AppointmentSlotDTO getAppointmentSlot() {
        return appointmentSlot;
    }

    public void setAppointmentSlot(AppointmentSlotDTO appointmentSlot) {
        this.appointmentSlot = appointmentSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicantDTO)) {
            return false;
        }

        ApplicantDTO applicantDTO = (ApplicantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, applicantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicantDTO{" +
            "id=" + getId() +
            ", surname='" + getSurname() + "'" +
            ", otherNames='" + getOtherNames() + "'" +
            ", maidenName='" + getMaidenName() + "'" +
            ", changedName='" + getChangedName() + "'" +
            ", gender='" + getGender() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", dateOfBirth='" + getDateOfBirth() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", birthEntryNumber='" + getBirthEntryNumber() + "'" +
            ", democraphicDetails=" + getDemocraphicDetails() +
            ", declaration=" + getDeclaration() +
            ", guardian=" + getGuardian() +
            ", user=" + getUser() +
            ", appointmentSlot=" + getAppointmentSlot() +
            "}";
    }
}

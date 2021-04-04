package com.afrikatek.documentsservice.domain;

import com.afrikatek.documentsservice.domain.enumeration.CitizenOptions;
import com.afrikatek.documentsservice.domain.enumeration.ForeignDocumentsOptions;
import com.afrikatek.documentsservice.domain.enumeration.PassportOptions;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Declaration.
 */
@Entity
@Table(name = "declaration")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Declaration implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "citizen", nullable = false)
    private CitizenOptions citizen;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "passport", nullable = false)
    private PassportOptions passport;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "foreign_passport", nullable = false)
    private ForeignDocumentsOptions foreignPassport;

    @NotNull
    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "renounced_citizenship", nullable = false)
    private ForeignDocumentsOptions renouncedCitizenship;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "passport_surrendered", nullable = false)
    private ForeignDocumentsOptions passportSurrendered;

    @NotNull
    @Column(name = "foreign_passport_number", nullable = false)
    private String foreignPassportNumber;

    @JsonIgnoreProperties(
        value = {
            "democraphicDetails",
            "declaration",
            "guardian",
            "addresses",
            "countryOfBirths",
            "user",
            "marriageDetails",
            "nextOfKeen",
            "appointmentSlot",
        },
        allowSetters = true
    )
    @OneToOne(mappedBy = "declaration")
    private Applicant applicant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Declaration id(Long id) {
        this.id = id;
        return this;
    }

    public CitizenOptions getCitizen() {
        return this.citizen;
    }

    public Declaration citizen(CitizenOptions citizen) {
        this.citizen = citizen;
        return this;
    }

    public void setCitizen(CitizenOptions citizen) {
        this.citizen = citizen;
    }

    public PassportOptions getPassport() {
        return this.passport;
    }

    public Declaration passport(PassportOptions passport) {
        this.passport = passport;
        return this;
    }

    public void setPassport(PassportOptions passport) {
        this.passport = passport;
    }

    public ForeignDocumentsOptions getForeignPassport() {
        return this.foreignPassport;
    }

    public Declaration foreignPassport(ForeignDocumentsOptions foreignPassport) {
        this.foreignPassport = foreignPassport;
        return this;
    }

    public void setForeignPassport(ForeignDocumentsOptions foreignPassport) {
        this.foreignPassport = foreignPassport;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public Declaration passportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public ForeignDocumentsOptions getRenouncedCitizenship() {
        return this.renouncedCitizenship;
    }

    public Declaration renouncedCitizenship(ForeignDocumentsOptions renouncedCitizenship) {
        this.renouncedCitizenship = renouncedCitizenship;
        return this;
    }

    public void setRenouncedCitizenship(ForeignDocumentsOptions renouncedCitizenship) {
        this.renouncedCitizenship = renouncedCitizenship;
    }

    public ForeignDocumentsOptions getPassportSurrendered() {
        return this.passportSurrendered;
    }

    public Declaration passportSurrendered(ForeignDocumentsOptions passportSurrendered) {
        this.passportSurrendered = passportSurrendered;
        return this;
    }

    public void setPassportSurrendered(ForeignDocumentsOptions passportSurrendered) {
        this.passportSurrendered = passportSurrendered;
    }

    public String getForeignPassportNumber() {
        return this.foreignPassportNumber;
    }

    public Declaration foreignPassportNumber(String foreignPassportNumber) {
        this.foreignPassportNumber = foreignPassportNumber;
        return this;
    }

    public void setForeignPassportNumber(String foreignPassportNumber) {
        this.foreignPassportNumber = foreignPassportNumber;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public Declaration applicant(Applicant applicant) {
        this.setApplicant(applicant);
        return this;
    }

    public void setApplicant(Applicant applicant) {
        if (this.applicant != null) {
            this.applicant.setDeclaration(null);
        }
        if (applicant != null) {
            applicant.setDeclaration(this);
        }
        this.applicant = applicant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Declaration)) {
            return false;
        }
        return id != null && id.equals(((Declaration) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Declaration{" +
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

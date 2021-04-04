package com.afrikatek.documentsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Guardian.
 */
@Entity
@Table(name = "guardian")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Guardian implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @NotNull
    @Column(name = "relationship_to_applicant", nullable = false)
    private String relationshipToApplicant;

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
    @OneToOne(mappedBy = "guardian")
    private Applicant applicant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Guardian id(Long id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Guardian fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Guardian idNumber(String idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getRelationshipToApplicant() {
        return this.relationshipToApplicant;
    }

    public Guardian relationshipToApplicant(String relationshipToApplicant) {
        this.relationshipToApplicant = relationshipToApplicant;
        return this;
    }

    public void setRelationshipToApplicant(String relationshipToApplicant) {
        this.relationshipToApplicant = relationshipToApplicant;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public Guardian applicant(Applicant applicant) {
        this.setApplicant(applicant);
        return this;
    }

    public void setApplicant(Applicant applicant) {
        if (this.applicant != null) {
            this.applicant.setGuardian(null);
        }
        if (applicant != null) {
            applicant.setGuardian(this);
        }
        this.applicant = applicant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Guardian)) {
            return false;
        }
        return id != null && id.equals(((Guardian) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Guardian{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", idNumber='" + getIdNumber() + "'" +
            ", relationshipToApplicant='" + getRelationshipToApplicant() + "'" +
            "}";
    }
}

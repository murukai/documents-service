package com.afrikatek.documentsservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NextOfKeen.
 */
@Entity
@Table(name = "next_of_keen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NextOfKeen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

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
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Applicant applicant;

    @JsonIgnoreProperties(value = { "country", "nextOfKeen", "applicant" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Address address;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public NextOfKeen id(Long id) {
        this.id = id;
        return this;
    }

    public String getFullName() {
        return this.fullName;
    }

    public NextOfKeen fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRelationshipToApplicant() {
        return this.relationshipToApplicant;
    }

    public NextOfKeen relationshipToApplicant(String relationshipToApplicant) {
        this.relationshipToApplicant = relationshipToApplicant;
        return this;
    }

    public void setRelationshipToApplicant(String relationshipToApplicant) {
        this.relationshipToApplicant = relationshipToApplicant;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public NextOfKeen applicant(Applicant applicant) {
        this.setApplicant(applicant);
        return this;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }

    public Address getAddress() {
        return this.address;
    }

    public NextOfKeen address(Address address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NextOfKeen)) {
            return false;
        }
        return id != null && id.equals(((NextOfKeen) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NextOfKeen{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", relationshipToApplicant='" + getRelationshipToApplicant() + "'" +
            "}";
    }
}

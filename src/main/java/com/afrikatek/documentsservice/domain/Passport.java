package com.afrikatek.documentsservice.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Passport.
 */
@Entity
@Table(name = "passport")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Passport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "passport_number", nullable = false)
    private String passportNumber;

    @NotNull
    @Column(name = "issued_at", nullable = false)
    private String issuedAt;

    @NotNull
    @Column(name = "issued_date", nullable = false)
    private Instant issuedDate;

    @NotNull
    @Column(name = "loss_description", nullable = false)
    private String lossDescription;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Passport id(Long id) {
        this.id = id;
        return this;
    }

    public String getPassportNumber() {
        return this.passportNumber;
    }

    public Passport passportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
        return this;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getIssuedAt() {
        return this.issuedAt;
    }

    public Passport issuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }

    public void setIssuedAt(String issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Instant getIssuedDate() {
        return this.issuedDate;
    }

    public Passport issuedDate(Instant issuedDate) {
        this.issuedDate = issuedDate;
        return this;
    }

    public void setIssuedDate(Instant issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getLossDescription() {
        return this.lossDescription;
    }

    public Passport lossDescription(String lossDescription) {
        this.lossDescription = lossDescription;
        return this;
    }

    public void setLossDescription(String lossDescription) {
        this.lossDescription = lossDescription;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Passport)) {
            return false;
        }
        return id != null && id.equals(((Passport) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Passport{" +
            "id=" + getId() +
            ", passportNumber='" + getPassportNumber() + "'" +
            ", issuedAt='" + getIssuedAt() + "'" +
            ", issuedDate='" + getIssuedDate() + "'" +
            ", lossDescription='" + getLossDescription() + "'" +
            "}";
    }
}

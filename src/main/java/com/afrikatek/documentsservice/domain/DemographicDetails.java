package com.afrikatek.documentsservice.domain;

import com.afrikatek.documentsservice.domain.enumeration.EyeColor;
import com.afrikatek.documentsservice.domain.enumeration.HairColor;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A DemographicDetails.
 */
@Entity
@Table(name = "demographic_details")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DemographicDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "eye_color", nullable = false)
    private EyeColor eyeColor;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "hair_color", nullable = false)
    private HairColor hairColor;

    @NotNull
    @Column(name = "visible_marks", nullable = false)
    private String visibleMarks;

    @NotNull
    @Column(name = "profession", nullable = false)
    private String profession;

    @NotNull
    @Column(name = "place_of_birth", nullable = false)
    private String placeOfBirth;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

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
    @OneToOne(mappedBy = "democraphicDetails")
    private Applicant applicant;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DemographicDetails id(Long id) {
        this.id = id;
        return this;
    }

    public EyeColor getEyeColor() {
        return this.eyeColor;
    }

    public DemographicDetails eyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
        return this;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public HairColor getHairColor() {
        return this.hairColor;
    }

    public DemographicDetails hairColor(HairColor hairColor) {
        this.hairColor = hairColor;
        return this;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public String getVisibleMarks() {
        return this.visibleMarks;
    }

    public DemographicDetails visibleMarks(String visibleMarks) {
        this.visibleMarks = visibleMarks;
        return this;
    }

    public void setVisibleMarks(String visibleMarks) {
        this.visibleMarks = visibleMarks;
    }

    public String getProfession() {
        return this.profession;
    }

    public DemographicDetails profession(String profession) {
        this.profession = profession;
        return this;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public DemographicDetails placeOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
        return this;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public byte[] getImage() {
        return this.image;
    }

    public DemographicDetails image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public DemographicDetails imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Applicant getApplicant() {
        return this.applicant;
    }

    public DemographicDetails applicant(Applicant applicant) {
        this.setApplicant(applicant);
        return this;
    }

    public void setApplicant(Applicant applicant) {
        if (this.applicant != null) {
            this.applicant.setDemocraphicDetails(null);
        }
        if (applicant != null) {
            applicant.setDemocraphicDetails(this);
        }
        this.applicant = applicant;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemographicDetails)) {
            return false;
        }
        return id != null && id.equals(((DemographicDetails) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemographicDetails{" +
            "id=" + getId() +
            ", eyeColor='" + getEyeColor() + "'" +
            ", hairColor='" + getHairColor() + "'" +
            ", visibleMarks='" + getVisibleMarks() + "'" +
            ", profession='" + getProfession() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}

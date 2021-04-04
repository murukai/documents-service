package com.afrikatek.documentsservice.service.dto;

import com.afrikatek.documentsservice.domain.enumeration.EyeColor;
import com.afrikatek.documentsservice.domain.enumeration.HairColor;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.afrikatek.documentsservice.domain.DemographicDetails} entity.
 */
public class DemographicDetailsDTO implements Serializable {

    private Long id;

    @NotNull
    private EyeColor eyeColor;

    @NotNull
    private HairColor hairColor;

    @NotNull
    private String visibleMarks;

    @NotNull
    private String profession;

    @NotNull
    private String placeOfBirth;

    @Lob
    private byte[] image;

    private String imageContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EyeColor getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(EyeColor eyeColor) {
        this.eyeColor = eyeColor;
    }

    public HairColor getHairColor() {
        return hairColor;
    }

    public void setHairColor(HairColor hairColor) {
        this.hairColor = hairColor;
    }

    public String getVisibleMarks() {
        return visibleMarks;
    }

    public void setVisibleMarks(String visibleMarks) {
        this.visibleMarks = visibleMarks;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DemographicDetailsDTO)) {
            return false;
        }

        DemographicDetailsDTO demographicDetailsDTO = (DemographicDetailsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, demographicDetailsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemographicDetailsDTO{" +
            "id=" + getId() +
            ", eyeColor='" + getEyeColor() + "'" +
            ", hairColor='" + getHairColor() + "'" +
            ", visibleMarks='" + getVisibleMarks() + "'" +
            ", profession='" + getProfession() + "'" +
            ", placeOfBirth='" + getPlaceOfBirth() + "'" +
            ", image='" + getImage() + "'" +
            "}";
    }
}

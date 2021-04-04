package com.afrikatek.documentsservice.domain;

import com.afrikatek.documentsservice.domain.enumeration.Gender;
import com.afrikatek.documentsservice.domain.enumeration.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Applicant.
 */
@Entity
@Table(name = "applicant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Applicant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "surname", nullable = false)
    private String surname;

    @NotNull
    @Column(name = "other_names", nullable = false)
    private String otherNames;

    @NotNull
    @Column(name = "maiden_name", nullable = false)
    private String maidenName;

    @NotNull
    @Column(name = "changed_name", nullable = false)
    private Boolean changedName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @NotNull
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @NotNull
    @Column(name = "id_number", nullable = false)
    private String idNumber;

    @NotNull
    @Column(name = "birth_entry_number", nullable = false)
    private String birthEntryNumber;

    @JsonIgnoreProperties(value = { "applicant" }, allowSetters = true)
    @OneToOne(optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private DemographicDetails democraphicDetails;

    @JsonIgnoreProperties(value = { "applicant" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Declaration declaration;

    @JsonIgnoreProperties(value = { "applicant" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Guardian guardian;

    @OneToMany(mappedBy = "applicant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "country", "nextOfKeen", "applicant" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @OneToMany(mappedBy = "applicant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "applicant" }, allowSetters = true)
    private Set<Country> countryOfBirths = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private User user;

    @JsonIgnoreProperties(value = { "applicant" }, allowSetters = true)
    @OneToOne(mappedBy = "applicant")
    private MarriageDetails marriageDetails;

    @JsonIgnoreProperties(value = { "applicant", "address" }, allowSetters = true)
    @OneToOne(mappedBy = "applicant")
    private NextOfKeen nextOfKeen;

    @ManyToOne
    @JsonIgnoreProperties(value = { "applicants", "appointment" }, allowSetters = true)
    private AppointmentSlot appointmentSlot;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Applicant id(Long id) {
        this.id = id;
        return this;
    }

    public String getSurname() {
        return this.surname;
    }

    public Applicant surname(String surname) {
        this.surname = surname;
        return this;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOtherNames() {
        return this.otherNames;
    }

    public Applicant otherNames(String otherNames) {
        this.otherNames = otherNames;
        return this;
    }

    public void setOtherNames(String otherNames) {
        this.otherNames = otherNames;
    }

    public String getMaidenName() {
        return this.maidenName;
    }

    public Applicant maidenName(String maidenName) {
        this.maidenName = maidenName;
        return this;
    }

    public void setMaidenName(String maidenName) {
        this.maidenName = maidenName;
    }

    public Boolean getChangedName() {
        return this.changedName;
    }

    public Applicant changedName(Boolean changedName) {
        this.changedName = changedName;
        return this;
    }

    public void setChangedName(Boolean changedName) {
        this.changedName = changedName;
    }

    public Gender getGender() {
        return this.gender;
    }

    public Applicant gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public MaritalStatus getMaritalStatus() {
        return this.maritalStatus;
    }

    public Applicant maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public Applicant dateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getIdNumber() {
        return this.idNumber;
    }

    public Applicant idNumber(String idNumber) {
        this.idNumber = idNumber;
        return this;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getBirthEntryNumber() {
        return this.birthEntryNumber;
    }

    public Applicant birthEntryNumber(String birthEntryNumber) {
        this.birthEntryNumber = birthEntryNumber;
        return this;
    }

    public void setBirthEntryNumber(String birthEntryNumber) {
        this.birthEntryNumber = birthEntryNumber;
    }

    public DemographicDetails getDemocraphicDetails() {
        return this.democraphicDetails;
    }

    public Applicant democraphicDetails(DemographicDetails demographicDetails) {
        this.setDemocraphicDetails(demographicDetails);
        return this;
    }

    public void setDemocraphicDetails(DemographicDetails demographicDetails) {
        this.democraphicDetails = demographicDetails;
    }

    public Declaration getDeclaration() {
        return this.declaration;
    }

    public Applicant declaration(Declaration declaration) {
        this.setDeclaration(declaration);
        return this;
    }

    public void setDeclaration(Declaration declaration) {
        this.declaration = declaration;
    }

    public Guardian getGuardian() {
        return this.guardian;
    }

    public Applicant guardian(Guardian guardian) {
        this.setGuardian(guardian);
        return this;
    }

    public void setGuardian(Guardian guardian) {
        this.guardian = guardian;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public Applicant addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Applicant addAddresses(Address address) {
        this.addresses.add(address);
        address.setApplicant(this);
        return this;
    }

    public Applicant removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setApplicant(null);
        return this;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setApplicant(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setApplicant(this));
        }
        this.addresses = addresses;
    }

    public Set<Country> getCountryOfBirths() {
        return this.countryOfBirths;
    }

    public Applicant countryOfBirths(Set<Country> countries) {
        this.setCountryOfBirths(countries);
        return this;
    }

    public Applicant addCountryOfBirth(Country country) {
        this.countryOfBirths.add(country);
        country.setApplicant(this);
        return this;
    }

    public Applicant removeCountryOfBirth(Country country) {
        this.countryOfBirths.remove(country);
        country.setApplicant(null);
        return this;
    }

    public void setCountryOfBirths(Set<Country> countries) {
        if (this.countryOfBirths != null) {
            this.countryOfBirths.forEach(i -> i.setApplicant(null));
        }
        if (countries != null) {
            countries.forEach(i -> i.setApplicant(this));
        }
        this.countryOfBirths = countries;
    }

    public User getUser() {
        return this.user;
    }

    public Applicant user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MarriageDetails getMarriageDetails() {
        return this.marriageDetails;
    }

    public Applicant marriageDetails(MarriageDetails marriageDetails) {
        this.setMarriageDetails(marriageDetails);
        return this;
    }

    public void setMarriageDetails(MarriageDetails marriageDetails) {
        if (this.marriageDetails != null) {
            this.marriageDetails.setApplicant(null);
        }
        if (marriageDetails != null) {
            marriageDetails.setApplicant(this);
        }
        this.marriageDetails = marriageDetails;
    }

    public NextOfKeen getNextOfKeen() {
        return this.nextOfKeen;
    }

    public Applicant nextOfKeen(NextOfKeen nextOfKeen) {
        this.setNextOfKeen(nextOfKeen);
        return this;
    }

    public void setNextOfKeen(NextOfKeen nextOfKeen) {
        if (this.nextOfKeen != null) {
            this.nextOfKeen.setApplicant(null);
        }
        if (nextOfKeen != null) {
            nextOfKeen.setApplicant(this);
        }
        this.nextOfKeen = nextOfKeen;
    }

    public AppointmentSlot getAppointmentSlot() {
        return this.appointmentSlot;
    }

    public Applicant appointmentSlot(AppointmentSlot appointmentSlot) {
        this.setAppointmentSlot(appointmentSlot);
        return this;
    }

    public void setAppointmentSlot(AppointmentSlot appointmentSlot) {
        this.appointmentSlot = appointmentSlot;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Applicant)) {
            return false;
        }
        return id != null && id.equals(((Applicant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Applicant{" +
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
            "}";
    }
}

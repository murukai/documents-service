package com.afrikatek.documentsservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsservice.IntegrationTest;
import com.afrikatek.documentsservice.domain.Applicant;
import com.afrikatek.documentsservice.domain.DemographicDetails;
import com.afrikatek.documentsservice.domain.User;
import com.afrikatek.documentsservice.domain.enumeration.Gender;
import com.afrikatek.documentsservice.domain.enumeration.MaritalStatus;
import com.afrikatek.documentsservice.repository.ApplicantRepository;
import com.afrikatek.documentsservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsservice.service.mapper.ApplicantMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ApplicantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApplicantResourceIT {

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_NAMES = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_NAMES = "BBBBBBBBBB";

    private static final String DEFAULT_MAIDEN_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MAIDEN_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CHANGED_NAME = false;
    private static final Boolean UPDATED_CHANGED_NAME = true;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final MaritalStatus DEFAULT_MARITAL_STATUS = MaritalStatus.MARRIED;
    private static final MaritalStatus UPDATED_MARITAL_STATUS = MaritalStatus.SINGLE;

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BIRTH_ENTRY_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_BIRTH_ENTRY_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/applicants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ApplicantRepository applicantRepository;

    @Autowired
    private ApplicantMapper applicantMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApplicantMockMvc;

    private Applicant applicant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicant createEntity(EntityManager em) {
        Applicant applicant = new Applicant()
            .surname(DEFAULT_SURNAME)
            .otherNames(DEFAULT_OTHER_NAMES)
            .maidenName(DEFAULT_MAIDEN_NAME)
            .changedName(DEFAULT_CHANGED_NAME)
            .gender(DEFAULT_GENDER)
            .maritalStatus(DEFAULT_MARITAL_STATUS)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .idNumber(DEFAULT_ID_NUMBER)
            .birthEntryNumber(DEFAULT_BIRTH_ENTRY_NUMBER);
        // Add required entity
        DemographicDetails demographicDetails;
        if (TestUtil.findAll(em, DemographicDetails.class).isEmpty()) {
            demographicDetails = DemographicDetailsResourceIT.createEntity(em);
            em.persist(demographicDetails);
            em.flush();
        } else {
            demographicDetails = TestUtil.findAll(em, DemographicDetails.class).get(0);
        }
        applicant.setDemocraphicDetails(demographicDetails);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicant.setUser(user);
        return applicant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Applicant createUpdatedEntity(EntityManager em) {
        Applicant applicant = new Applicant()
            .surname(UPDATED_SURNAME)
            .otherNames(UPDATED_OTHER_NAMES)
            .maidenName(UPDATED_MAIDEN_NAME)
            .changedName(UPDATED_CHANGED_NAME)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .birthEntryNumber(UPDATED_BIRTH_ENTRY_NUMBER);
        // Add required entity
        DemographicDetails demographicDetails;
        if (TestUtil.findAll(em, DemographicDetails.class).isEmpty()) {
            demographicDetails = DemographicDetailsResourceIT.createUpdatedEntity(em);
            em.persist(demographicDetails);
            em.flush();
        } else {
            demographicDetails = TestUtil.findAll(em, DemographicDetails.class).get(0);
        }
        applicant.setDemocraphicDetails(demographicDetails);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        applicant.setUser(user);
        return applicant;
    }

    @BeforeEach
    public void initTest() {
        applicant = createEntity(em);
    }

    @Test
    @Transactional
    void createApplicant() throws Exception {
        int databaseSizeBeforeCreate = applicantRepository.findAll().size();
        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);
        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isCreated());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate + 1);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testApplicant.getOtherNames()).isEqualTo(DEFAULT_OTHER_NAMES);
        assertThat(testApplicant.getMaidenName()).isEqualTo(DEFAULT_MAIDEN_NAME);
        assertThat(testApplicant.getChangedName()).isEqualTo(DEFAULT_CHANGED_NAME);
        assertThat(testApplicant.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(DEFAULT_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(DEFAULT_BIRTH_ENTRY_NUMBER);
    }

    @Test
    @Transactional
    void createApplicantWithExistingId() throws Exception {
        // Create the Applicant with an existing ID
        applicant.setId(1L);
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        int databaseSizeBeforeCreate = applicantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSurnameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setSurname(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOtherNamesIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setOtherNames(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaidenNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setMaidenName(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChangedNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setChangedName(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setDateOfBirth(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setIdNumber(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBirthEntryNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = applicantRepository.findAll().size();
        // set the field null
        applicant.setBirthEntryNumber(null);

        // Create the Applicant, which fails.
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        restApplicantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isBadRequest());

        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApplicants() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get all the applicantList
        restApplicantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(applicant.getId().intValue())))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].otherNames").value(hasItem(DEFAULT_OTHER_NAMES)))
            .andExpect(jsonPath("$.[*].maidenName").value(hasItem(DEFAULT_MAIDEN_NAME)))
            .andExpect(jsonPath("$.[*].changedName").value(hasItem(DEFAULT_CHANGED_NAME.booleanValue())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].maritalStatus").value(hasItem(DEFAULT_MARITAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].birthEntryNumber").value(hasItem(DEFAULT_BIRTH_ENTRY_NUMBER)));
    }

    @Test
    @Transactional
    void getApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        // Get the applicant
        restApplicantMockMvc
            .perform(get(ENTITY_API_URL_ID, applicant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(applicant.getId().intValue()))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.otherNames").value(DEFAULT_OTHER_NAMES))
            .andExpect(jsonPath("$.maidenName").value(DEFAULT_MAIDEN_NAME))
            .andExpect(jsonPath("$.changedName").value(DEFAULT_CHANGED_NAME.booleanValue()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.maritalStatus").value(DEFAULT_MARITAL_STATUS.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.birthEntryNumber").value(DEFAULT_BIRTH_ENTRY_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingApplicant() throws Exception {
        // Get the applicant
        restApplicantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant
        Applicant updatedApplicant = applicantRepository.findById(applicant.getId()).get();
        // Disconnect from session so that the updates on updatedApplicant are not directly saved in db
        em.detach(updatedApplicant);
        updatedApplicant
            .surname(UPDATED_SURNAME)
            .otherNames(UPDATED_OTHER_NAMES)
            .maidenName(UPDATED_MAIDEN_NAME)
            .changedName(UPDATED_CHANGED_NAME)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .birthEntryNumber(UPDATED_BIRTH_ENTRY_NUMBER);
        ApplicantDTO applicantDTO = applicantMapper.toDto(updatedApplicant);

        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testApplicant.getOtherNames()).isEqualTo(UPDATED_OTHER_NAMES);
        assertThat(testApplicant.getMaidenName()).isEqualTo(UPDATED_MAIDEN_NAME);
        assertThat(testApplicant.getChangedName()).isEqualTo(UPDATED_CHANGED_NAME);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(UPDATED_BIRTH_ENTRY_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, applicantDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(applicantDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApplicantWithPatch() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant using partial update
        Applicant partialUpdatedApplicant = new Applicant();
        partialUpdatedApplicant.setId(applicant.getId());

        partialUpdatedApplicant
            .otherNames(UPDATED_OTHER_NAMES)
            .maidenName(UPDATED_MAIDEN_NAME)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .birthEntryNumber(UPDATED_BIRTH_ENTRY_NUMBER);

        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicant))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testApplicant.getOtherNames()).isEqualTo(UPDATED_OTHER_NAMES);
        assertThat(testApplicant.getMaidenName()).isEqualTo(UPDATED_MAIDEN_NAME);
        assertThat(testApplicant.getChangedName()).isEqualTo(DEFAULT_CHANGED_NAME);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(UPDATED_BIRTH_ENTRY_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateApplicantWithPatch() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();

        // Update the applicant using partial update
        Applicant partialUpdatedApplicant = new Applicant();
        partialUpdatedApplicant.setId(applicant.getId());

        partialUpdatedApplicant
            .surname(UPDATED_SURNAME)
            .otherNames(UPDATED_OTHER_NAMES)
            .maidenName(UPDATED_MAIDEN_NAME)
            .changedName(UPDATED_CHANGED_NAME)
            .gender(UPDATED_GENDER)
            .maritalStatus(UPDATED_MARITAL_STATUS)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .idNumber(UPDATED_ID_NUMBER)
            .birthEntryNumber(UPDATED_BIRTH_ENTRY_NUMBER);

        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApplicant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedApplicant))
            )
            .andExpect(status().isOk());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
        Applicant testApplicant = applicantList.get(applicantList.size() - 1);
        assertThat(testApplicant.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testApplicant.getOtherNames()).isEqualTo(UPDATED_OTHER_NAMES);
        assertThat(testApplicant.getMaidenName()).isEqualTo(UPDATED_MAIDEN_NAME);
        assertThat(testApplicant.getChangedName()).isEqualTo(UPDATED_CHANGED_NAME);
        assertThat(testApplicant.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testApplicant.getMaritalStatus()).isEqualTo(UPDATED_MARITAL_STATUS);
        assertThat(testApplicant.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testApplicant.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testApplicant.getBirthEntryNumber()).isEqualTo(UPDATED_BIRTH_ENTRY_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, applicantDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApplicant() throws Exception {
        int databaseSizeBeforeUpdate = applicantRepository.findAll().size();
        applicant.setId(count.incrementAndGet());

        // Create the Applicant
        ApplicantDTO applicantDTO = applicantMapper.toDto(applicant);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApplicantMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(applicantDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Applicant in the database
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApplicant() throws Exception {
        // Initialize the database
        applicantRepository.saveAndFlush(applicant);

        int databaseSizeBeforeDelete = applicantRepository.findAll().size();

        // Delete the applicant
        restApplicantMockMvc
            .perform(delete(ENTITY_API_URL_ID, applicant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Applicant> applicantList = applicantRepository.findAll();
        assertThat(applicantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

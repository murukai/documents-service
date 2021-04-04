package com.afrikatek.documentsservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsservice.IntegrationTest;
import com.afrikatek.documentsservice.domain.Guardian;
import com.afrikatek.documentsservice.repository.GuardianRepository;
import com.afrikatek.documentsservice.service.dto.GuardianDTO;
import com.afrikatek.documentsservice.service.mapper.GuardianMapper;
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
 * Integration tests for the {@link GuardianResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GuardianResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ID_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ID_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP_TO_APPLICANT = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_TO_APPLICANT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/guardians";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GuardianRepository guardianRepository;

    @Autowired
    private GuardianMapper guardianMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGuardianMockMvc;

    private Guardian guardian;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guardian createEntity(EntityManager em) {
        Guardian guardian = new Guardian()
            .fullName(DEFAULT_FULL_NAME)
            .idNumber(DEFAULT_ID_NUMBER)
            .relationshipToApplicant(DEFAULT_RELATIONSHIP_TO_APPLICANT);
        return guardian;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Guardian createUpdatedEntity(EntityManager em) {
        Guardian guardian = new Guardian()
            .fullName(UPDATED_FULL_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .relationshipToApplicant(UPDATED_RELATIONSHIP_TO_APPLICANT);
        return guardian;
    }

    @BeforeEach
    public void initTest() {
        guardian = createEntity(em);
    }

    @Test
    @Transactional
    void createGuardian() throws Exception {
        int databaseSizeBeforeCreate = guardianRepository.findAll().size();
        // Create the Guardian
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);
        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardianDTO)))
            .andExpect(status().isCreated());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeCreate + 1);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testGuardian.getRelationshipToApplicant()).isEqualTo(DEFAULT_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void createGuardianWithExistingId() throws Exception {
        // Create the Guardian with an existing ID
        guardian.setId(1L);
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        int databaseSizeBeforeCreate = guardianRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardianDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setFullName(null);

        // Create the Guardian, which fails.
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardianDTO)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIdNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setIdNumber(null);

        // Create the Guardian, which fails.
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardianDTO)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationshipToApplicantIsRequired() throws Exception {
        int databaseSizeBeforeTest = guardianRepository.findAll().size();
        // set the field null
        guardian.setRelationshipToApplicant(null);

        // Create the Guardian, which fails.
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        restGuardianMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardianDTO)))
            .andExpect(status().isBadRequest());

        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllGuardians() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        // Get all the guardianList
        restGuardianMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(guardian.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].idNumber").value(hasItem(DEFAULT_ID_NUMBER)))
            .andExpect(jsonPath("$.[*].relationshipToApplicant").value(hasItem(DEFAULT_RELATIONSHIP_TO_APPLICANT)));
    }

    @Test
    @Transactional
    void getGuardian() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        // Get the guardian
        restGuardianMockMvc
            .perform(get(ENTITY_API_URL_ID, guardian.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(guardian.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.idNumber").value(DEFAULT_ID_NUMBER))
            .andExpect(jsonPath("$.relationshipToApplicant").value(DEFAULT_RELATIONSHIP_TO_APPLICANT));
    }

    @Test
    @Transactional
    void getNonExistingGuardian() throws Exception {
        // Get the guardian
        restGuardianMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGuardian() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();

        // Update the guardian
        Guardian updatedGuardian = guardianRepository.findById(guardian.getId()).get();
        // Disconnect from session so that the updates on updatedGuardian are not directly saved in db
        em.detach(updatedGuardian);
        updatedGuardian.fullName(UPDATED_FULL_NAME).idNumber(UPDATED_ID_NUMBER).relationshipToApplicant(UPDATED_RELATIONSHIP_TO_APPLICANT);
        GuardianDTO guardianDTO = guardianMapper.toDto(updatedGuardian);

        restGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guardianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardianDTO))
            )
            .andExpect(status().isOk());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testGuardian.getRelationshipToApplicant()).isEqualTo(UPDATED_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void putNonExistingGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // Create the Guardian
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, guardianDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // Create the Guardian
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(guardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // Create the Guardian
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(guardianDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGuardianWithPatch() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();

        // Update the guardian using partial update
        Guardian partialUpdatedGuardian = new Guardian();
        partialUpdatedGuardian.setId(guardian.getId());

        partialUpdatedGuardian.fullName(UPDATED_FULL_NAME);

        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuardian.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuardian))
            )
            .andExpect(status().isOk());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(DEFAULT_ID_NUMBER);
        assertThat(testGuardian.getRelationshipToApplicant()).isEqualTo(DEFAULT_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void fullUpdateGuardianWithPatch() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();

        // Update the guardian using partial update
        Guardian partialUpdatedGuardian = new Guardian();
        partialUpdatedGuardian.setId(guardian.getId());

        partialUpdatedGuardian
            .fullName(UPDATED_FULL_NAME)
            .idNumber(UPDATED_ID_NUMBER)
            .relationshipToApplicant(UPDATED_RELATIONSHIP_TO_APPLICANT);

        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGuardian.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGuardian))
            )
            .andExpect(status().isOk());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
        Guardian testGuardian = guardianList.get(guardianList.size() - 1);
        assertThat(testGuardian.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testGuardian.getIdNumber()).isEqualTo(UPDATED_ID_NUMBER);
        assertThat(testGuardian.getRelationshipToApplicant()).isEqualTo(UPDATED_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void patchNonExistingGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // Create the Guardian
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, guardianDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // Create the Guardian
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(guardianDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGuardian() throws Exception {
        int databaseSizeBeforeUpdate = guardianRepository.findAll().size();
        guardian.setId(count.incrementAndGet());

        // Create the Guardian
        GuardianDTO guardianDTO = guardianMapper.toDto(guardian);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGuardianMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(guardianDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Guardian in the database
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGuardian() throws Exception {
        // Initialize the database
        guardianRepository.saveAndFlush(guardian);

        int databaseSizeBeforeDelete = guardianRepository.findAll().size();

        // Delete the guardian
        restGuardianMockMvc
            .perform(delete(ENTITY_API_URL_ID, guardian.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Guardian> guardianList = guardianRepository.findAll();
        assertThat(guardianList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

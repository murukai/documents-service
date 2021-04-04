package com.afrikatek.documentsservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsservice.IntegrationTest;
import com.afrikatek.documentsservice.domain.Address;
import com.afrikatek.documentsservice.domain.Applicant;
import com.afrikatek.documentsservice.domain.NextOfKeen;
import com.afrikatek.documentsservice.repository.NextOfKeenRepository;
import com.afrikatek.documentsservice.service.dto.NextOfKeenDTO;
import com.afrikatek.documentsservice.service.mapper.NextOfKeenMapper;
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
 * Integration tests for the {@link NextOfKeenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NextOfKeenResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RELATIONSHIP_TO_APPLICANT = "AAAAAAAAAA";
    private static final String UPDATED_RELATIONSHIP_TO_APPLICANT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/next-of-keens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NextOfKeenRepository nextOfKeenRepository;

    @Autowired
    private NextOfKeenMapper nextOfKeenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNextOfKeenMockMvc;

    private NextOfKeen nextOfKeen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOfKeen createEntity(EntityManager em) {
        NextOfKeen nextOfKeen = new NextOfKeen().fullName(DEFAULT_FULL_NAME).relationshipToApplicant(DEFAULT_RELATIONSHIP_TO_APPLICANT);
        // Add required entity
        Applicant applicant;
        if (TestUtil.findAll(em, Applicant.class).isEmpty()) {
            applicant = ApplicantResourceIT.createEntity(em);
            em.persist(applicant);
            em.flush();
        } else {
            applicant = TestUtil.findAll(em, Applicant.class).get(0);
        }
        nextOfKeen.setApplicant(applicant);
        // Add required entity
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            address = AddressResourceIT.createEntity(em);
            em.persist(address);
            em.flush();
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        nextOfKeen.setAddress(address);
        return nextOfKeen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NextOfKeen createUpdatedEntity(EntityManager em) {
        NextOfKeen nextOfKeen = new NextOfKeen().fullName(UPDATED_FULL_NAME).relationshipToApplicant(UPDATED_RELATIONSHIP_TO_APPLICANT);
        // Add required entity
        Applicant applicant;
        if (TestUtil.findAll(em, Applicant.class).isEmpty()) {
            applicant = ApplicantResourceIT.createUpdatedEntity(em);
            em.persist(applicant);
            em.flush();
        } else {
            applicant = TestUtil.findAll(em, Applicant.class).get(0);
        }
        nextOfKeen.setApplicant(applicant);
        // Add required entity
        Address address;
        if (TestUtil.findAll(em, Address.class).isEmpty()) {
            address = AddressResourceIT.createUpdatedEntity(em);
            em.persist(address);
            em.flush();
        } else {
            address = TestUtil.findAll(em, Address.class).get(0);
        }
        nextOfKeen.setAddress(address);
        return nextOfKeen;
    }

    @BeforeEach
    public void initTest() {
        nextOfKeen = createEntity(em);
    }

    @Test
    @Transactional
    void createNextOfKeen() throws Exception {
        int databaseSizeBeforeCreate = nextOfKeenRepository.findAll().size();
        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);
        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isCreated());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeCreate + 1);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testNextOfKeen.getRelationshipToApplicant()).isEqualTo(DEFAULT_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void createNextOfKeenWithExistingId() throws Exception {
        // Create the NextOfKeen with an existing ID
        nextOfKeen.setId(1L);
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        int databaseSizeBeforeCreate = nextOfKeenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = nextOfKeenRepository.findAll().size();
        // set the field null
        nextOfKeen.setFullName(null);

        // Create the NextOfKeen, which fails.
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isBadRequest());

        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationshipToApplicantIsRequired() throws Exception {
        int databaseSizeBeforeTest = nextOfKeenRepository.findAll().size();
        // set the field null
        nextOfKeen.setRelationshipToApplicant(null);

        // Create the NextOfKeen, which fails.
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        restNextOfKeenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isBadRequest());

        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNextOfKeens() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        // Get all the nextOfKeenList
        restNextOfKeenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nextOfKeen.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].relationshipToApplicant").value(hasItem(DEFAULT_RELATIONSHIP_TO_APPLICANT)));
    }

    @Test
    @Transactional
    void getNextOfKeen() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        // Get the nextOfKeen
        restNextOfKeenMockMvc
            .perform(get(ENTITY_API_URL_ID, nextOfKeen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nextOfKeen.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.relationshipToApplicant").value(DEFAULT_RELATIONSHIP_TO_APPLICANT));
    }

    @Test
    @Transactional
    void getNonExistingNextOfKeen() throws Exception {
        // Get the nextOfKeen
        restNextOfKeenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNextOfKeen() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();

        // Update the nextOfKeen
        NextOfKeen updatedNextOfKeen = nextOfKeenRepository.findById(nextOfKeen.getId()).get();
        // Disconnect from session so that the updates on updatedNextOfKeen are not directly saved in db
        em.detach(updatedNextOfKeen);
        updatedNextOfKeen.fullName(UPDATED_FULL_NAME).relationshipToApplicant(UPDATED_RELATIONSHIP_TO_APPLICANT);
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(updatedNextOfKeen);

        restNextOfKeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOfKeenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isOk());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testNextOfKeen.getRelationshipToApplicant()).isEqualTo(UPDATED_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void putNonExistingNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nextOfKeenDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNextOfKeenWithPatch() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();

        // Update the nextOfKeen using partial update
        NextOfKeen partialUpdatedNextOfKeen = new NextOfKeen();
        partialUpdatedNextOfKeen.setId(nextOfKeen.getId());

        partialUpdatedNextOfKeen.relationshipToApplicant(UPDATED_RELATIONSHIP_TO_APPLICANT);

        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOfKeen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNextOfKeen))
            )
            .andExpect(status().isOk());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testNextOfKeen.getRelationshipToApplicant()).isEqualTo(UPDATED_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void fullUpdateNextOfKeenWithPatch() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();

        // Update the nextOfKeen using partial update
        NextOfKeen partialUpdatedNextOfKeen = new NextOfKeen();
        partialUpdatedNextOfKeen.setId(nextOfKeen.getId());

        partialUpdatedNextOfKeen.fullName(UPDATED_FULL_NAME).relationshipToApplicant(UPDATED_RELATIONSHIP_TO_APPLICANT);

        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNextOfKeen.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNextOfKeen))
            )
            .andExpect(status().isOk());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
        NextOfKeen testNextOfKeen = nextOfKeenList.get(nextOfKeenList.size() - 1);
        assertThat(testNextOfKeen.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testNextOfKeen.getRelationshipToApplicant()).isEqualTo(UPDATED_RELATIONSHIP_TO_APPLICANT);
    }

    @Test
    @Transactional
    void patchNonExistingNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nextOfKeenDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNextOfKeen() throws Exception {
        int databaseSizeBeforeUpdate = nextOfKeenRepository.findAll().size();
        nextOfKeen.setId(count.incrementAndGet());

        // Create the NextOfKeen
        NextOfKeenDTO nextOfKeenDTO = nextOfKeenMapper.toDto(nextOfKeen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNextOfKeenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nextOfKeenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NextOfKeen in the database
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNextOfKeen() throws Exception {
        // Initialize the database
        nextOfKeenRepository.saveAndFlush(nextOfKeen);

        int databaseSizeBeforeDelete = nextOfKeenRepository.findAll().size();

        // Delete the nextOfKeen
        restNextOfKeenMockMvc
            .perform(delete(ENTITY_API_URL_ID, nextOfKeen.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NextOfKeen> nextOfKeenList = nextOfKeenRepository.findAll();
        assertThat(nextOfKeenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

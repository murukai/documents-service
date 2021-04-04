package com.afrikatek.documentsservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsservice.IntegrationTest;
import com.afrikatek.documentsservice.domain.DemographicDetails;
import com.afrikatek.documentsservice.domain.enumeration.EyeColor;
import com.afrikatek.documentsservice.domain.enumeration.HairColor;
import com.afrikatek.documentsservice.repository.DemographicDetailsRepository;
import com.afrikatek.documentsservice.service.dto.DemographicDetailsDTO;
import com.afrikatek.documentsservice.service.mapper.DemographicDetailsMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DemographicDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DemographicDetailsResourceIT {

    private static final EyeColor DEFAULT_EYE_COLOR = EyeColor.BROWN;
    private static final EyeColor UPDATED_EYE_COLOR = EyeColor.BLACK;

    private static final HairColor DEFAULT_HAIR_COLOR = HairColor.BLACK;
    private static final HairColor UPDATED_HAIR_COLOR = HairColor.BROWN;

    private static final String DEFAULT_VISIBLE_MARKS = "AAAAAAAAAA";
    private static final String UPDATED_VISIBLE_MARKS = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final String DEFAULT_PLACE_OF_BIRTH = "AAAAAAAAAA";
    private static final String UPDATED_PLACE_OF_BIRTH = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/demographic-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DemographicDetailsRepository demographicDetailsRepository;

    @Autowired
    private DemographicDetailsMapper demographicDetailsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDemographicDetailsMockMvc;

    private DemographicDetails demographicDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemographicDetails createEntity(EntityManager em) {
        DemographicDetails demographicDetails = new DemographicDetails()
            .eyeColor(DEFAULT_EYE_COLOR)
            .hairColor(DEFAULT_HAIR_COLOR)
            .visibleMarks(DEFAULT_VISIBLE_MARKS)
            .profession(DEFAULT_PROFESSION)
            .placeOfBirth(DEFAULT_PLACE_OF_BIRTH)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return demographicDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DemographicDetails createUpdatedEntity(EntityManager em) {
        DemographicDetails demographicDetails = new DemographicDetails()
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS)
            .profession(UPDATED_PROFESSION)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return demographicDetails;
    }

    @BeforeEach
    public void initTest() {
        demographicDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createDemographicDetails() throws Exception {
        int databaseSizeBeforeCreate = demographicDetailsRepository.findAll().size();
        // Create the DemographicDetails
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);
        restDemographicDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isCreated());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        DemographicDetails testDemographicDetails = demographicDetailsList.get(demographicDetailsList.size() - 1);
        assertThat(testDemographicDetails.getEyeColor()).isEqualTo(DEFAULT_EYE_COLOR);
        assertThat(testDemographicDetails.getHairColor()).isEqualTo(DEFAULT_HAIR_COLOR);
        assertThat(testDemographicDetails.getVisibleMarks()).isEqualTo(DEFAULT_VISIBLE_MARKS);
        assertThat(testDemographicDetails.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testDemographicDetails.getPlaceOfBirth()).isEqualTo(DEFAULT_PLACE_OF_BIRTH);
        assertThat(testDemographicDetails.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testDemographicDetails.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createDemographicDetailsWithExistingId() throws Exception {
        // Create the DemographicDetails with an existing ID
        demographicDetails.setId(1L);
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        int databaseSizeBeforeCreate = demographicDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDemographicDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEyeColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = demographicDetailsRepository.findAll().size();
        // set the field null
        demographicDetails.setEyeColor(null);

        // Create the DemographicDetails, which fails.
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        restDemographicDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkHairColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = demographicDetailsRepository.findAll().size();
        // set the field null
        demographicDetails.setHairColor(null);

        // Create the DemographicDetails, which fails.
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        restDemographicDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVisibleMarksIsRequired() throws Exception {
        int databaseSizeBeforeTest = demographicDetailsRepository.findAll().size();
        // set the field null
        demographicDetails.setVisibleMarks(null);

        // Create the DemographicDetails, which fails.
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        restDemographicDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProfessionIsRequired() throws Exception {
        int databaseSizeBeforeTest = demographicDetailsRepository.findAll().size();
        // set the field null
        demographicDetails.setProfession(null);

        // Create the DemographicDetails, which fails.
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        restDemographicDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPlaceOfBirthIsRequired() throws Exception {
        int databaseSizeBeforeTest = demographicDetailsRepository.findAll().size();
        // set the field null
        demographicDetails.setPlaceOfBirth(null);

        // Create the DemographicDetails, which fails.
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        restDemographicDetailsMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDemographicDetails() throws Exception {
        // Initialize the database
        demographicDetailsRepository.saveAndFlush(demographicDetails);

        // Get all the demographicDetailsList
        restDemographicDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(demographicDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].eyeColor").value(hasItem(DEFAULT_EYE_COLOR.toString())))
            .andExpect(jsonPath("$.[*].hairColor").value(hasItem(DEFAULT_HAIR_COLOR.toString())))
            .andExpect(jsonPath("$.[*].visibleMarks").value(hasItem(DEFAULT_VISIBLE_MARKS)))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION)))
            .andExpect(jsonPath("$.[*].placeOfBirth").value(hasItem(DEFAULT_PLACE_OF_BIRTH)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }

    @Test
    @Transactional
    void getDemographicDetails() throws Exception {
        // Initialize the database
        demographicDetailsRepository.saveAndFlush(demographicDetails);

        // Get the demographicDetails
        restDemographicDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, demographicDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(demographicDetails.getId().intValue()))
            .andExpect(jsonPath("$.eyeColor").value(DEFAULT_EYE_COLOR.toString()))
            .andExpect(jsonPath("$.hairColor").value(DEFAULT_HAIR_COLOR.toString()))
            .andExpect(jsonPath("$.visibleMarks").value(DEFAULT_VISIBLE_MARKS))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION))
            .andExpect(jsonPath("$.placeOfBirth").value(DEFAULT_PLACE_OF_BIRTH))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }

    @Test
    @Transactional
    void getNonExistingDemographicDetails() throws Exception {
        // Get the demographicDetails
        restDemographicDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDemographicDetails() throws Exception {
        // Initialize the database
        demographicDetailsRepository.saveAndFlush(demographicDetails);

        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();

        // Update the demographicDetails
        DemographicDetails updatedDemographicDetails = demographicDetailsRepository.findById(demographicDetails.getId()).get();
        // Disconnect from session so that the updates on updatedDemographicDetails are not directly saved in db
        em.detach(updatedDemographicDetails);
        updatedDemographicDetails
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS)
            .profession(UPDATED_PROFESSION)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(updatedDemographicDetails);

        restDemographicDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demographicDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isOk());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
        DemographicDetails testDemographicDetails = demographicDetailsList.get(demographicDetailsList.size() - 1);
        assertThat(testDemographicDetails.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testDemographicDetails.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testDemographicDetails.getVisibleMarks()).isEqualTo(UPDATED_VISIBLE_MARKS);
        assertThat(testDemographicDetails.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testDemographicDetails.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testDemographicDetails.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testDemographicDetails.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDemographicDetails() throws Exception {
        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();
        demographicDetails.setId(count.incrementAndGet());

        // Create the DemographicDetails
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemographicDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, demographicDetailsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDemographicDetails() throws Exception {
        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();
        demographicDetails.setId(count.incrementAndGet());

        // Create the DemographicDetails
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDemographicDetails() throws Exception {
        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();
        demographicDetails.setId(count.incrementAndGet());

        // Create the DemographicDetails
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDetailsMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDemographicDetailsWithPatch() throws Exception {
        // Initialize the database
        demographicDetailsRepository.saveAndFlush(demographicDetails);

        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();

        // Update the demographicDetails using partial update
        DemographicDetails partialUpdatedDemographicDetails = new DemographicDetails();
        partialUpdatedDemographicDetails.setId(demographicDetails.getId());

        partialUpdatedDemographicDetails
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH);

        restDemographicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemographicDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemographicDetails))
            )
            .andExpect(status().isOk());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
        DemographicDetails testDemographicDetails = demographicDetailsList.get(demographicDetailsList.size() - 1);
        assertThat(testDemographicDetails.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testDemographicDetails.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testDemographicDetails.getVisibleMarks()).isEqualTo(UPDATED_VISIBLE_MARKS);
        assertThat(testDemographicDetails.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testDemographicDetails.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testDemographicDetails.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testDemographicDetails.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDemographicDetailsWithPatch() throws Exception {
        // Initialize the database
        demographicDetailsRepository.saveAndFlush(demographicDetails);

        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();

        // Update the demographicDetails using partial update
        DemographicDetails partialUpdatedDemographicDetails = new DemographicDetails();
        partialUpdatedDemographicDetails.setId(demographicDetails.getId());

        partialUpdatedDemographicDetails
            .eyeColor(UPDATED_EYE_COLOR)
            .hairColor(UPDATED_HAIR_COLOR)
            .visibleMarks(UPDATED_VISIBLE_MARKS)
            .profession(UPDATED_PROFESSION)
            .placeOfBirth(UPDATED_PLACE_OF_BIRTH)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);

        restDemographicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDemographicDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDemographicDetails))
            )
            .andExpect(status().isOk());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
        DemographicDetails testDemographicDetails = demographicDetailsList.get(demographicDetailsList.size() - 1);
        assertThat(testDemographicDetails.getEyeColor()).isEqualTo(UPDATED_EYE_COLOR);
        assertThat(testDemographicDetails.getHairColor()).isEqualTo(UPDATED_HAIR_COLOR);
        assertThat(testDemographicDetails.getVisibleMarks()).isEqualTo(UPDATED_VISIBLE_MARKS);
        assertThat(testDemographicDetails.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testDemographicDetails.getPlaceOfBirth()).isEqualTo(UPDATED_PLACE_OF_BIRTH);
        assertThat(testDemographicDetails.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testDemographicDetails.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDemographicDetails() throws Exception {
        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();
        demographicDetails.setId(count.incrementAndGet());

        // Create the DemographicDetails
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDemographicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, demographicDetailsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDemographicDetails() throws Exception {
        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();
        demographicDetails.setId(count.incrementAndGet());

        // Create the DemographicDetails
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDemographicDetails() throws Exception {
        int databaseSizeBeforeUpdate = demographicDetailsRepository.findAll().size();
        demographicDetails.setId(count.incrementAndGet());

        // Create the DemographicDetails
        DemographicDetailsDTO demographicDetailsDTO = demographicDetailsMapper.toDto(demographicDetails);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDemographicDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(demographicDetailsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DemographicDetails in the database
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDemographicDetails() throws Exception {
        // Initialize the database
        demographicDetailsRepository.saveAndFlush(demographicDetails);

        int databaseSizeBeforeDelete = demographicDetailsRepository.findAll().size();

        // Delete the demographicDetails
        restDemographicDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, demographicDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DemographicDetails> demographicDetailsList = demographicDetailsRepository.findAll();
        assertThat(demographicDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

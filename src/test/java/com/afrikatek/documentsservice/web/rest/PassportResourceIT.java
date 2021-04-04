package com.afrikatek.documentsservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsservice.IntegrationTest;
import com.afrikatek.documentsservice.domain.Passport;
import com.afrikatek.documentsservice.repository.PassportRepository;
import com.afrikatek.documentsservice.service.dto.PassportDTO;
import com.afrikatek.documentsservice.service.mapper.PassportMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PassportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PassportResourceIT {

    private static final String DEFAULT_PASSPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ISSUED_AT = "AAAAAAAAAA";
    private static final String UPDATED_ISSUED_AT = "BBBBBBBBBB";

    private static final Instant DEFAULT_ISSUED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ISSUED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LOSS_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_LOSS_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/passports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PassportRepository passportRepository;

    @Autowired
    private PassportMapper passportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPassportMockMvc;

    private Passport passport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passport createEntity(EntityManager em) {
        Passport passport = new Passport()
            .passportNumber(DEFAULT_PASSPORT_NUMBER)
            .issuedAt(DEFAULT_ISSUED_AT)
            .issuedDate(DEFAULT_ISSUED_DATE)
            .lossDescription(DEFAULT_LOSS_DESCRIPTION);
        return passport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Passport createUpdatedEntity(EntityManager em) {
        Passport passport = new Passport()
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .issuedAt(UPDATED_ISSUED_AT)
            .issuedDate(UPDATED_ISSUED_DATE)
            .lossDescription(UPDATED_LOSS_DESCRIPTION);
        return passport;
    }

    @BeforeEach
    public void initTest() {
        passport = createEntity(em);
    }

    @Test
    @Transactional
    void createPassport() throws Exception {
        int databaseSizeBeforeCreate = passportRepository.findAll().size();
        // Create the Passport
        PassportDTO passportDTO = passportMapper.toDto(passport);
        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passportDTO)))
            .andExpect(status().isCreated());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeCreate + 1);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
        assertThat(testPassport.getIssuedAt()).isEqualTo(DEFAULT_ISSUED_AT);
        assertThat(testPassport.getIssuedDate()).isEqualTo(DEFAULT_ISSUED_DATE);
        assertThat(testPassport.getLossDescription()).isEqualTo(DEFAULT_LOSS_DESCRIPTION);
    }

    @Test
    @Transactional
    void createPassportWithExistingId() throws Exception {
        // Create the Passport with an existing ID
        passport.setId(1L);
        PassportDTO passportDTO = passportMapper.toDto(passport);

        int databaseSizeBeforeCreate = passportRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPassportNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setPassportNumber(null);

        // Create the Passport, which fails.
        PassportDTO passportDTO = passportMapper.toDto(passport);

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passportDTO)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssuedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setIssuedAt(null);

        // Create the Passport, which fails.
        PassportDTO passportDTO = passportMapper.toDto(passport);

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passportDTO)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIssuedDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setIssuedDate(null);

        // Create the Passport, which fails.
        PassportDTO passportDTO = passportMapper.toDto(passport);

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passportDTO)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLossDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = passportRepository.findAll().size();
        // set the field null
        passport.setLossDescription(null);

        // Create the Passport, which fails.
        PassportDTO passportDTO = passportMapper.toDto(passport);

        restPassportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passportDTO)))
            .andExpect(status().isBadRequest());

        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPassports() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        // Get all the passportList
        restPassportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passport.getId().intValue())))
            .andExpect(jsonPath("$.[*].passportNumber").value(hasItem(DEFAULT_PASSPORT_NUMBER)))
            .andExpect(jsonPath("$.[*].issuedAt").value(hasItem(DEFAULT_ISSUED_AT)))
            .andExpect(jsonPath("$.[*].issuedDate").value(hasItem(DEFAULT_ISSUED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lossDescription").value(hasItem(DEFAULT_LOSS_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getPassport() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        // Get the passport
        restPassportMockMvc
            .perform(get(ENTITY_API_URL_ID, passport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(passport.getId().intValue()))
            .andExpect(jsonPath("$.passportNumber").value(DEFAULT_PASSPORT_NUMBER))
            .andExpect(jsonPath("$.issuedAt").value(DEFAULT_ISSUED_AT))
            .andExpect(jsonPath("$.issuedDate").value(DEFAULT_ISSUED_DATE.toString()))
            .andExpect(jsonPath("$.lossDescription").value(DEFAULT_LOSS_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingPassport() throws Exception {
        // Get the passport
        restPassportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPassport() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Update the passport
        Passport updatedPassport = passportRepository.findById(passport.getId()).get();
        // Disconnect from session so that the updates on updatedPassport are not directly saved in db
        em.detach(updatedPassport);
        updatedPassport
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .issuedAt(UPDATED_ISSUED_AT)
            .issuedDate(UPDATED_ISSUED_DATE)
            .lossDescription(UPDATED_LOSS_DESCRIPTION);
        PassportDTO passportDTO = passportMapper.toDto(updatedPassport);

        restPassportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passportDTO))
            )
            .andExpect(status().isOk());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testPassport.getIssuedAt()).isEqualTo(UPDATED_ISSUED_AT);
        assertThat(testPassport.getIssuedDate()).isEqualTo(UPDATED_ISSUED_DATE);
        assertThat(testPassport.getLossDescription()).isEqualTo(UPDATED_LOSS_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // Create the Passport
        PassportDTO passportDTO = passportMapper.toDto(passport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, passportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // Create the Passport
        PassportDTO passportDTO = passportMapper.toDto(passport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(passportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // Create the Passport
        PassportDTO passportDTO = passportMapper.toDto(passport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(passportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePassportWithPatch() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Update the passport using partial update
        Passport partialUpdatedPassport = new Passport();
        partialUpdatedPassport.setId(passport.getId());

        partialUpdatedPassport
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .issuedAt(UPDATED_ISSUED_AT)
            .issuedDate(UPDATED_ISSUED_DATE)
            .lossDescription(UPDATED_LOSS_DESCRIPTION);

        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPassport))
            )
            .andExpect(status().isOk());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testPassport.getIssuedAt()).isEqualTo(UPDATED_ISSUED_AT);
        assertThat(testPassport.getIssuedDate()).isEqualTo(UPDATED_ISSUED_DATE);
        assertThat(testPassport.getLossDescription()).isEqualTo(UPDATED_LOSS_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdatePassportWithPatch() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeUpdate = passportRepository.findAll().size();

        // Update the passport using partial update
        Passport partialUpdatedPassport = new Passport();
        partialUpdatedPassport.setId(passport.getId());

        partialUpdatedPassport
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .issuedAt(UPDATED_ISSUED_AT)
            .issuedDate(UPDATED_ISSUED_DATE)
            .lossDescription(UPDATED_LOSS_DESCRIPTION);

        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPassport.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPassport))
            )
            .andExpect(status().isOk());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
        Passport testPassport = passportList.get(passportList.size() - 1);
        assertThat(testPassport.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testPassport.getIssuedAt()).isEqualTo(UPDATED_ISSUED_AT);
        assertThat(testPassport.getIssuedDate()).isEqualTo(UPDATED_ISSUED_DATE);
        assertThat(testPassport.getLossDescription()).isEqualTo(UPDATED_LOSS_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // Create the Passport
        PassportDTO passportDTO = passportMapper.toDto(passport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, passportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // Create the Passport
        PassportDTO passportDTO = passportMapper.toDto(passport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(passportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPassport() throws Exception {
        int databaseSizeBeforeUpdate = passportRepository.findAll().size();
        passport.setId(count.incrementAndGet());

        // Create the Passport
        PassportDTO passportDTO = passportMapper.toDto(passport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPassportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(passportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Passport in the database
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePassport() throws Exception {
        // Initialize the database
        passportRepository.saveAndFlush(passport);

        int databaseSizeBeforeDelete = passportRepository.findAll().size();

        // Delete the passport
        restPassportMockMvc
            .perform(delete(ENTITY_API_URL_ID, passport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Passport> passportList = passportRepository.findAll();
        assertThat(passportList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

package com.afrikatek.documentsservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.afrikatek.documentsservice.IntegrationTest;
import com.afrikatek.documentsservice.domain.Declaration;
import com.afrikatek.documentsservice.domain.enumeration.CitizenOptions;
import com.afrikatek.documentsservice.domain.enumeration.ForeignDocumentsOptions;
import com.afrikatek.documentsservice.domain.enumeration.ForeignDocumentsOptions;
import com.afrikatek.documentsservice.domain.enumeration.ForeignDocumentsOptions;
import com.afrikatek.documentsservice.domain.enumeration.PassportOptions;
import com.afrikatek.documentsservice.repository.DeclarationRepository;
import com.afrikatek.documentsservice.service.dto.DeclarationDTO;
import com.afrikatek.documentsservice.service.mapper.DeclarationMapper;
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
 * Integration tests for the {@link DeclarationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DeclarationResourceIT {

    private static final CitizenOptions DEFAULT_CITIZEN = CitizenOptions.BIRTH;
    private static final CitizenOptions UPDATED_CITIZEN = CitizenOptions.DESCENT;

    private static final PassportOptions DEFAULT_PASSPORT = PassportOptions.NO;
    private static final PassportOptions UPDATED_PASSPORT = PassportOptions.YES_VALID;

    private static final ForeignDocumentsOptions DEFAULT_FOREIGN_PASSPORT = ForeignDocumentsOptions.NA;
    private static final ForeignDocumentsOptions UPDATED_FOREIGN_PASSPORT = ForeignDocumentsOptions.NO;

    private static final String DEFAULT_PASSPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PASSPORT_NUMBER = "BBBBBBBBBB";

    private static final ForeignDocumentsOptions DEFAULT_RENOUNCED_CITIZENSHIP = ForeignDocumentsOptions.NA;
    private static final ForeignDocumentsOptions UPDATED_RENOUNCED_CITIZENSHIP = ForeignDocumentsOptions.NO;

    private static final ForeignDocumentsOptions DEFAULT_PASSPORT_SURRENDERED = ForeignDocumentsOptions.NA;
    private static final ForeignDocumentsOptions UPDATED_PASSPORT_SURRENDERED = ForeignDocumentsOptions.NO;

    private static final String DEFAULT_FOREIGN_PASSPORT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_FOREIGN_PASSPORT_NUMBER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/declarations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DeclarationRepository declarationRepository;

    @Autowired
    private DeclarationMapper declarationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeclarationMockMvc;

    private Declaration declaration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Declaration createEntity(EntityManager em) {
        Declaration declaration = new Declaration()
            .citizen(DEFAULT_CITIZEN)
            .passport(DEFAULT_PASSPORT)
            .foreignPassport(DEFAULT_FOREIGN_PASSPORT)
            .passportNumber(DEFAULT_PASSPORT_NUMBER)
            .renouncedCitizenship(DEFAULT_RENOUNCED_CITIZENSHIP)
            .passportSurrendered(DEFAULT_PASSPORT_SURRENDERED)
            .foreignPassportNumber(DEFAULT_FOREIGN_PASSPORT_NUMBER);
        return declaration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Declaration createUpdatedEntity(EntityManager em) {
        Declaration declaration = new Declaration()
            .citizen(UPDATED_CITIZEN)
            .passport(UPDATED_PASSPORT)
            .foreignPassport(UPDATED_FOREIGN_PASSPORT)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .renouncedCitizenship(UPDATED_RENOUNCED_CITIZENSHIP)
            .passportSurrendered(UPDATED_PASSPORT_SURRENDERED)
            .foreignPassportNumber(UPDATED_FOREIGN_PASSPORT_NUMBER);
        return declaration;
    }

    @BeforeEach
    public void initTest() {
        declaration = createEntity(em);
    }

    @Test
    @Transactional
    void createDeclaration() throws Exception {
        int databaseSizeBeforeCreate = declarationRepository.findAll().size();
        // Create the Declaration
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);
        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeCreate + 1);
        Declaration testDeclaration = declarationList.get(declarationList.size() - 1);
        assertThat(testDeclaration.getCitizen()).isEqualTo(DEFAULT_CITIZEN);
        assertThat(testDeclaration.getPassport()).isEqualTo(DEFAULT_PASSPORT);
        assertThat(testDeclaration.getForeignPassport()).isEqualTo(DEFAULT_FOREIGN_PASSPORT);
        assertThat(testDeclaration.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
        assertThat(testDeclaration.getRenouncedCitizenship()).isEqualTo(DEFAULT_RENOUNCED_CITIZENSHIP);
        assertThat(testDeclaration.getPassportSurrendered()).isEqualTo(DEFAULT_PASSPORT_SURRENDERED);
        assertThat(testDeclaration.getForeignPassportNumber()).isEqualTo(DEFAULT_FOREIGN_PASSPORT_NUMBER);
    }

    @Test
    @Transactional
    void createDeclarationWithExistingId() throws Exception {
        // Create the Declaration with an existing ID
        declaration.setId(1L);
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        int databaseSizeBeforeCreate = declarationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCitizenIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setCitizen(null);

        // Create the Declaration, which fails.
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPassportIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setPassport(null);

        // Create the Declaration, which fails.
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkForeignPassportIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setForeignPassport(null);

        // Create the Declaration, which fails.
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPassportNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setPassportNumber(null);

        // Create the Declaration, which fails.
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRenouncedCitizenshipIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setRenouncedCitizenship(null);

        // Create the Declaration, which fails.
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPassportSurrenderedIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setPassportSurrendered(null);

        // Create the Declaration, which fails.
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkForeignPassportNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = declarationRepository.findAll().size();
        // set the field null
        declaration.setForeignPassportNumber(null);

        // Create the Declaration, which fails.
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        restDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDeclarations() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        // Get all the declarationList
        restDeclarationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(declaration.getId().intValue())))
            .andExpect(jsonPath("$.[*].citizen").value(hasItem(DEFAULT_CITIZEN.toString())))
            .andExpect(jsonPath("$.[*].passport").value(hasItem(DEFAULT_PASSPORT.toString())))
            .andExpect(jsonPath("$.[*].foreignPassport").value(hasItem(DEFAULT_FOREIGN_PASSPORT.toString())))
            .andExpect(jsonPath("$.[*].passportNumber").value(hasItem(DEFAULT_PASSPORT_NUMBER)))
            .andExpect(jsonPath("$.[*].renouncedCitizenship").value(hasItem(DEFAULT_RENOUNCED_CITIZENSHIP.toString())))
            .andExpect(jsonPath("$.[*].passportSurrendered").value(hasItem(DEFAULT_PASSPORT_SURRENDERED.toString())))
            .andExpect(jsonPath("$.[*].foreignPassportNumber").value(hasItem(DEFAULT_FOREIGN_PASSPORT_NUMBER)));
    }

    @Test
    @Transactional
    void getDeclaration() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        // Get the declaration
        restDeclarationMockMvc
            .perform(get(ENTITY_API_URL_ID, declaration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(declaration.getId().intValue()))
            .andExpect(jsonPath("$.citizen").value(DEFAULT_CITIZEN.toString()))
            .andExpect(jsonPath("$.passport").value(DEFAULT_PASSPORT.toString()))
            .andExpect(jsonPath("$.foreignPassport").value(DEFAULT_FOREIGN_PASSPORT.toString()))
            .andExpect(jsonPath("$.passportNumber").value(DEFAULT_PASSPORT_NUMBER))
            .andExpect(jsonPath("$.renouncedCitizenship").value(DEFAULT_RENOUNCED_CITIZENSHIP.toString()))
            .andExpect(jsonPath("$.passportSurrendered").value(DEFAULT_PASSPORT_SURRENDERED.toString()))
            .andExpect(jsonPath("$.foreignPassportNumber").value(DEFAULT_FOREIGN_PASSPORT_NUMBER));
    }

    @Test
    @Transactional
    void getNonExistingDeclaration() throws Exception {
        // Get the declaration
        restDeclarationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDeclaration() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();

        // Update the declaration
        Declaration updatedDeclaration = declarationRepository.findById(declaration.getId()).get();
        // Disconnect from session so that the updates on updatedDeclaration are not directly saved in db
        em.detach(updatedDeclaration);
        updatedDeclaration
            .citizen(UPDATED_CITIZEN)
            .passport(UPDATED_PASSPORT)
            .foreignPassport(UPDATED_FOREIGN_PASSPORT)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .renouncedCitizenship(UPDATED_RENOUNCED_CITIZENSHIP)
            .passportSurrendered(UPDATED_PASSPORT_SURRENDERED)
            .foreignPassportNumber(UPDATED_FOREIGN_PASSPORT_NUMBER);
        DeclarationDTO declarationDTO = declarationMapper.toDto(updatedDeclaration);

        restDeclarationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, declarationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
        Declaration testDeclaration = declarationList.get(declarationList.size() - 1);
        assertThat(testDeclaration.getCitizen()).isEqualTo(UPDATED_CITIZEN);
        assertThat(testDeclaration.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testDeclaration.getForeignPassport()).isEqualTo(UPDATED_FOREIGN_PASSPORT);
        assertThat(testDeclaration.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testDeclaration.getRenouncedCitizenship()).isEqualTo(UPDATED_RENOUNCED_CITIZENSHIP);
        assertThat(testDeclaration.getPassportSurrendered()).isEqualTo(UPDATED_PASSPORT_SURRENDERED);
        assertThat(testDeclaration.getForeignPassportNumber()).isEqualTo(UPDATED_FOREIGN_PASSPORT_NUMBER);
    }

    @Test
    @Transactional
    void putNonExistingDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();
        declaration.setId(count.incrementAndGet());

        // Create the Declaration
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeclarationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, declarationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();
        declaration.setId(count.incrementAndGet());

        // Create the Declaration
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeclarationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();
        declaration.setId(count.incrementAndGet());

        // Create the Declaration
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeclarationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(declarationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDeclarationWithPatch() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();

        // Update the declaration using partial update
        Declaration partialUpdatedDeclaration = new Declaration();
        partialUpdatedDeclaration.setId(declaration.getId());

        partialUpdatedDeclaration
            .citizen(UPDATED_CITIZEN)
            .passport(UPDATED_PASSPORT)
            .foreignPassport(UPDATED_FOREIGN_PASSPORT)
            .foreignPassportNumber(UPDATED_FOREIGN_PASSPORT_NUMBER);

        restDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeclaration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeclaration))
            )
            .andExpect(status().isOk());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
        Declaration testDeclaration = declarationList.get(declarationList.size() - 1);
        assertThat(testDeclaration.getCitizen()).isEqualTo(UPDATED_CITIZEN);
        assertThat(testDeclaration.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testDeclaration.getForeignPassport()).isEqualTo(UPDATED_FOREIGN_PASSPORT);
        assertThat(testDeclaration.getPassportNumber()).isEqualTo(DEFAULT_PASSPORT_NUMBER);
        assertThat(testDeclaration.getRenouncedCitizenship()).isEqualTo(DEFAULT_RENOUNCED_CITIZENSHIP);
        assertThat(testDeclaration.getPassportSurrendered()).isEqualTo(DEFAULT_PASSPORT_SURRENDERED);
        assertThat(testDeclaration.getForeignPassportNumber()).isEqualTo(UPDATED_FOREIGN_PASSPORT_NUMBER);
    }

    @Test
    @Transactional
    void fullUpdateDeclarationWithPatch() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();

        // Update the declaration using partial update
        Declaration partialUpdatedDeclaration = new Declaration();
        partialUpdatedDeclaration.setId(declaration.getId());

        partialUpdatedDeclaration
            .citizen(UPDATED_CITIZEN)
            .passport(UPDATED_PASSPORT)
            .foreignPassport(UPDATED_FOREIGN_PASSPORT)
            .passportNumber(UPDATED_PASSPORT_NUMBER)
            .renouncedCitizenship(UPDATED_RENOUNCED_CITIZENSHIP)
            .passportSurrendered(UPDATED_PASSPORT_SURRENDERED)
            .foreignPassportNumber(UPDATED_FOREIGN_PASSPORT_NUMBER);

        restDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeclaration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDeclaration))
            )
            .andExpect(status().isOk());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
        Declaration testDeclaration = declarationList.get(declarationList.size() - 1);
        assertThat(testDeclaration.getCitizen()).isEqualTo(UPDATED_CITIZEN);
        assertThat(testDeclaration.getPassport()).isEqualTo(UPDATED_PASSPORT);
        assertThat(testDeclaration.getForeignPassport()).isEqualTo(UPDATED_FOREIGN_PASSPORT);
        assertThat(testDeclaration.getPassportNumber()).isEqualTo(UPDATED_PASSPORT_NUMBER);
        assertThat(testDeclaration.getRenouncedCitizenship()).isEqualTo(UPDATED_RENOUNCED_CITIZENSHIP);
        assertThat(testDeclaration.getPassportSurrendered()).isEqualTo(UPDATED_PASSPORT_SURRENDERED);
        assertThat(testDeclaration.getForeignPassportNumber()).isEqualTo(UPDATED_FOREIGN_PASSPORT_NUMBER);
    }

    @Test
    @Transactional
    void patchNonExistingDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();
        declaration.setId(count.incrementAndGet());

        // Create the Declaration
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, declarationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();
        declaration.setId(count.incrementAndGet());

        // Create the Declaration
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = declarationRepository.findAll().size();
        declaration.setId(count.incrementAndGet());

        // Create the Declaration
        DeclarationDTO declarationDTO = declarationMapper.toDto(declaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(declarationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Declaration in the database
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeclaration() throws Exception {
        // Initialize the database
        declarationRepository.saveAndFlush(declaration);

        int databaseSizeBeforeDelete = declarationRepository.findAll().size();

        // Delete the declaration
        restDeclarationMockMvc
            .perform(delete(ENTITY_API_URL_ID, declaration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Declaration> declarationList = declarationRepository.findAll();
        assertThat(declarationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}

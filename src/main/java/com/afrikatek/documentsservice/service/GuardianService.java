package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.Guardian;
import com.afrikatek.documentsservice.repository.GuardianRepository;
import com.afrikatek.documentsservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsservice.service.dto.GuardianDTO;
import com.afrikatek.documentsservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsservice.service.mapper.GuardianMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Guardian}.
 */
@Service
@Transactional
public class GuardianService {

    private final Logger log = LoggerFactory.getLogger(GuardianService.class);

    private final GuardianRepository guardianRepository;

    private final GuardianMapper guardianMapper;

    private final ApplicantMapper applicantMapper;

    public GuardianService(GuardianRepository guardianRepository, GuardianMapper guardianMapper, ApplicantMapper applicantMapper) {
        this.guardianRepository = guardianRepository;
        this.guardianMapper = guardianMapper;
        this.applicantMapper = applicantMapper;
    }

    /**
     * Save a guardian.
     *
     * @param guardianDTO the entity to save.
     * @return the persisted entity.
     */
    public GuardianDTO save(GuardianDTO guardianDTO) {
        log.debug("Request to save Guardian : {}", guardianDTO);
        Guardian guardian = guardianMapper.toEntity(guardianDTO);
        guardian = guardianRepository.save(guardian);
        return guardianMapper.toDto(guardian);
    }

    /**
     * Partially update a guardian.
     *
     * @param guardianDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<GuardianDTO> partialUpdate(GuardianDTO guardianDTO) {
        log.debug("Request to partially update Guardian : {}", guardianDTO);

        return guardianRepository
            .findById(guardianDTO.getId())
            .map(
                existingGuardian -> {
                    guardianMapper.partialUpdate(existingGuardian, guardianDTO);
                    return existingGuardian;
                }
            )
            .map(guardianRepository::save)
            .map(guardianMapper::toDto);
    }

    /**
     * Get all the guardians.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<GuardianDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Guardians");
        return guardianRepository.findAll(pageable).map(guardianMapper::toDto);
    }

    /**
     *  Get all the guardians where Applicant is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<GuardianDTO> findAllWhereApplicantIsNull() {
        log.debug("Request to get all guardians where Applicant is null");
        return StreamSupport
            .stream(guardianRepository.findAll().spliterator(), false)
            .filter(guardian -> guardian.getApplicant() == null)
            .map(guardianMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one guardian by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<GuardianDTO> findOne(Long id) {
        log.debug("Request to get Guardian : {}", id);
        return guardianRepository.findById(id).map(guardianMapper::toDto);
    }

    /**
     * Delete the guardian by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Guardian : {}", id);
        guardianRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<GuardianDTO> findByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request to find guardian details for applicant {}", applicantDTO);
        return guardianRepository.findByApplicant(applicantMapper.toEntity(applicantDTO)).map(guardianMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<GuardianDTO> findByApplicantAsCurrentUser(Pageable pageable) {
        log.debug("Request to find guardians for the current logged in user ");
        return guardianRepository.findByApplicantAsCurrentUser(pageable).map(guardianMapper::toDto);
    }
}

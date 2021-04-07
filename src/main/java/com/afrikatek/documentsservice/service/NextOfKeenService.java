package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.NextOfKeen;
import com.afrikatek.documentsservice.repository.NextOfKeenRepository;
import com.afrikatek.documentsservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsservice.service.dto.NextOfKeenDTO;
import com.afrikatek.documentsservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsservice.service.mapper.NextOfKeenMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NextOfKeen}.
 */
@Service
@Transactional
public class NextOfKeenService {

    private final Logger log = LoggerFactory.getLogger(NextOfKeenService.class);

    private final NextOfKeenRepository nextOfKeenRepository;

    private final NextOfKeenMapper nextOfKeenMapper;

    private final ApplicantMapper applicantMapper;

    public NextOfKeenService(
        NextOfKeenRepository nextOfKeenRepository,
        NextOfKeenMapper nextOfKeenMapper,
        ApplicantMapper applicantMapper
    ) {
        this.nextOfKeenRepository = nextOfKeenRepository;
        this.nextOfKeenMapper = nextOfKeenMapper;
        this.applicantMapper = applicantMapper;
    }

    /**
     * Save a nextOfKeen.
     *
     * @param nextOfKeenDTO the entity to save.
     * @return the persisted entity.
     */
    public NextOfKeenDTO save(NextOfKeenDTO nextOfKeenDTO) {
        log.debug("Request to save NextOfKeen : {}", nextOfKeenDTO);
        NextOfKeen nextOfKeen = nextOfKeenMapper.toEntity(nextOfKeenDTO);
        nextOfKeen = nextOfKeenRepository.save(nextOfKeen);
        return nextOfKeenMapper.toDto(nextOfKeen);
    }

    /**
     * Partially update a nextOfKeen.
     *
     * @param nextOfKeenDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NextOfKeenDTO> partialUpdate(NextOfKeenDTO nextOfKeenDTO) {
        log.debug("Request to partially update NextOfKeen : {}", nextOfKeenDTO);

        return nextOfKeenRepository
            .findById(nextOfKeenDTO.getId())
            .map(
                existingNextOfKeen -> {
                    nextOfKeenMapper.partialUpdate(existingNextOfKeen, nextOfKeenDTO);
                    return existingNextOfKeen;
                }
            )
            .map(nextOfKeenRepository::save)
            .map(nextOfKeenMapper::toDto);
    }

    /**
     * Get all the nextOfKeens.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NextOfKeenDTO> findAll(Pageable pageable) {
        log.debug("Request to get all NextOfKeens");
        return nextOfKeenRepository.findAll(pageable).map(nextOfKeenMapper::toDto);
    }

    /**
     * Get one nextOfKeen by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NextOfKeenDTO> findOne(Long id) {
        log.debug("Request to get NextOfKeen : {}", id);
        return nextOfKeenRepository.findById(id).map(nextOfKeenMapper::toDto);
    }

    /**
     * Delete the nextOfKeen by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NextOfKeen : {}", id);
        nextOfKeenRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<NextOfKeenDTO> findByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request to get next of keen for applicant {}", applicantDTO);
        return nextOfKeenRepository.findByApplicant(applicantMapper.toEntity(applicantDTO)).map(nextOfKeenMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<NextOfKeenDTO> findByApplicantAsCurrentUser(Pageable pageable) {
        log.debug("Request for next of keen details for applicant for current user");
        return nextOfKeenRepository.findByApplicantAsCurrentUser(pageable).map(nextOfKeenMapper::toDto);
    }
}

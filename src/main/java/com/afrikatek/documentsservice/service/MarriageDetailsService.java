package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.MarriageDetails;
import com.afrikatek.documentsservice.repository.MarriageDetailsRepository;
import com.afrikatek.documentsservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsservice.service.dto.MarriageDetailsDTO;
import com.afrikatek.documentsservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsservice.service.mapper.MarriageDetailsMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link MarriageDetails}.
 */
@Service
@Transactional
public class MarriageDetailsService {

    private final Logger log = LoggerFactory.getLogger(MarriageDetailsService.class);

    private final MarriageDetailsRepository marriageDetailsRepository;

    private final MarriageDetailsMapper marriageDetailsMapper;

    private final ApplicantMapper applicantMapper;

    public MarriageDetailsService(
        MarriageDetailsRepository marriageDetailsRepository,
        MarriageDetailsMapper marriageDetailsMapper,
        ApplicantMapper applicantMapper
    ) {
        this.marriageDetailsRepository = marriageDetailsRepository;
        this.marriageDetailsMapper = marriageDetailsMapper;
        this.applicantMapper = applicantMapper;
    }

    /**
     * Save a marriageDetails.
     *
     * @param marriageDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public MarriageDetailsDTO save(MarriageDetailsDTO marriageDetailsDTO) {
        log.debug("Request to save MarriageDetails : {}", marriageDetailsDTO);
        MarriageDetails marriageDetails = marriageDetailsMapper.toEntity(marriageDetailsDTO);
        marriageDetails = marriageDetailsRepository.save(marriageDetails);
        return marriageDetailsMapper.toDto(marriageDetails);
    }

    /**
     * Partially update a marriageDetails.
     *
     * @param marriageDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MarriageDetailsDTO> partialUpdate(MarriageDetailsDTO marriageDetailsDTO) {
        log.debug("Request to partially update MarriageDetails : {}", marriageDetailsDTO);

        return marriageDetailsRepository
            .findById(marriageDetailsDTO.getId())
            .map(
                existingMarriageDetails -> {
                    marriageDetailsMapper.partialUpdate(existingMarriageDetails, marriageDetailsDTO);
                    return existingMarriageDetails;
                }
            )
            .map(marriageDetailsRepository::save)
            .map(marriageDetailsMapper::toDto);
    }

    /**
     * Get all the marriageDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MarriageDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all MarriageDetails");
        return marriageDetailsRepository.findAll(pageable).map(marriageDetailsMapper::toDto);
    }

    /**
     * Get one marriageDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MarriageDetailsDTO> findOne(Long id) {
        log.debug("Request to get MarriageDetails : {}", id);
        return marriageDetailsRepository.findById(id).map(marriageDetailsMapper::toDto);
    }

    /**
     * Delete the marriageDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MarriageDetails : {}", id);
        marriageDetailsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<MarriageDetailsDTO> findByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request to find marriage details for applicant {}", applicantDTO);
        return marriageDetailsRepository.findByApplicant(applicantMapper.toEntity(applicantDTO)).map(marriageDetailsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<MarriageDetailsDTO> findByApplicantAsCurrentUser(Pageable pageable) {
        log.debug("Request to find marriage details for the current user");
        return marriageDetailsRepository.findByApplicantAsCurrentUser(pageable).map(marriageDetailsMapper::toDto);
    }
}

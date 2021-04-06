package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.DemographicDetails;
import com.afrikatek.documentsservice.repository.DemographicDetailsRepository;
import com.afrikatek.documentsservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsservice.service.dto.DemographicDetailsDTO;
import com.afrikatek.documentsservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsservice.service.mapper.DemographicDetailsMapper;
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
 * Service Implementation for managing {@link DemographicDetails}.
 */
@Service
@Transactional
public class DemographicDetailsService {

    private final Logger log = LoggerFactory.getLogger(DemographicDetailsService.class);

    private final DemographicDetailsRepository demographicDetailsRepository;

    private final DemographicDetailsMapper demographicDetailsMapper;

    private final ApplicantMapper applicantMapper;

    public DemographicDetailsService(
        DemographicDetailsRepository demographicDetailsRepository,
        DemographicDetailsMapper demographicDetailsMapper,
        ApplicantMapper applicantMapper
    ) {
        this.demographicDetailsRepository = demographicDetailsRepository;
        this.demographicDetailsMapper = demographicDetailsMapper;
        this.applicantMapper = applicantMapper;
    }

    /**
     * Save a demographicDetails.
     *
     * @param demographicDetailsDTO the entity to save.
     * @return the persisted entity.
     */
    public DemographicDetailsDTO save(DemographicDetailsDTO demographicDetailsDTO) {
        log.debug("Request to save DemographicDetails : {}", demographicDetailsDTO);
        DemographicDetails demographicDetails = demographicDetailsMapper.toEntity(demographicDetailsDTO);
        demographicDetails = demographicDetailsRepository.save(demographicDetails);
        return demographicDetailsMapper.toDto(demographicDetails);
    }

    /**
     * Partially update a demographicDetails.
     *
     * @param demographicDetailsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DemographicDetailsDTO> partialUpdate(DemographicDetailsDTO demographicDetailsDTO) {
        log.debug("Request to partially update DemographicDetails : {}", demographicDetailsDTO);

        return demographicDetailsRepository
            .findById(demographicDetailsDTO.getId())
            .map(
                existingDemographicDetails -> {
                    demographicDetailsMapper.partialUpdate(existingDemographicDetails, demographicDetailsDTO);
                    return existingDemographicDetails;
                }
            )
            .map(demographicDetailsRepository::save)
            .map(demographicDetailsMapper::toDto);
    }

    /**
     * Get all the demographicDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DemographicDetailsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DemographicDetails");
        return demographicDetailsRepository.findAll(pageable).map(demographicDetailsMapper::toDto);
    }

    /**
     *  Get all the demographicDetails where Applicant is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DemographicDetailsDTO> findAllWhereApplicantIsNull() {
        log.debug("Request to get all demographicDetails where Applicant is null");
        return StreamSupport
            .stream(demographicDetailsRepository.findAll().spliterator(), false)
            .filter(demographicDetails -> demographicDetails.getApplicant() == null)
            .map(demographicDetailsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one demographicDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DemographicDetailsDTO> findOne(Long id) {
        log.debug("Request to get DemographicDetails : {}", id);
        return demographicDetailsRepository.findById(id).map(demographicDetailsMapper::toDto);
    }

    /**
     * Delete the demographicDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DemographicDetails : {}", id);
        demographicDetailsRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<DemographicDetailsDTO> findByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request for demographic details for applicant {}", applicantDTO);
        return demographicDetailsRepository.findByApplicant(applicantMapper.toEntity(applicantDTO)).map(demographicDetailsMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<List<DemographicDetailsDTO>> findByApplicantAsCurrentUser(Pageable pageable) {
        log.debug("Request for demographic details for currently logged in user");
        return demographicDetailsRepository.findByApplicantAsCurrentUser(pageable).map(demographicDetailsMapper::toDto);
    }
}

package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.Declaration;
import com.afrikatek.documentsservice.repository.DeclarationRepository;
import com.afrikatek.documentsservice.service.dto.ApplicantDTO;
import com.afrikatek.documentsservice.service.dto.DeclarationDTO;
import com.afrikatek.documentsservice.service.mapper.ApplicantMapper;
import com.afrikatek.documentsservice.service.mapper.DeclarationMapper;
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
 * Service Implementation for managing {@link Declaration}.
 */
@Service
@Transactional
public class DeclarationService {

    private final Logger log = LoggerFactory.getLogger(DeclarationService.class);

    private final DeclarationRepository declarationRepository;

    private final DeclarationMapper declarationMapper;

    private final ApplicantMapper applicantMapper;

    public DeclarationService(
        DeclarationRepository declarationRepository,
        DeclarationMapper declarationMapper,
        ApplicantMapper applicantMapper
    ) {
        this.declarationRepository = declarationRepository;
        this.declarationMapper = declarationMapper;
        this.applicantMapper = applicantMapper;
    }

    /**
     * Save a declaration.
     *
     * @param declarationDTO the entity to save.
     * @return the persisted entity.
     */
    public DeclarationDTO save(DeclarationDTO declarationDTO) {
        log.debug("Request to save Declaration : {}", declarationDTO);
        Declaration declaration = declarationMapper.toEntity(declarationDTO);
        declaration = declarationRepository.save(declaration);
        return declarationMapper.toDto(declaration);
    }

    /**
     * Partially update a declaration.
     *
     * @param declarationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DeclarationDTO> partialUpdate(DeclarationDTO declarationDTO) {
        log.debug("Request to partially update Declaration : {}", declarationDTO);

        return declarationRepository
            .findById(declarationDTO.getId())
            .map(
                existingDeclaration -> {
                    declarationMapper.partialUpdate(existingDeclaration, declarationDTO);
                    return existingDeclaration;
                }
            )
            .map(declarationRepository::save)
            .map(declarationMapper::toDto);
    }

    /**
     * Get all the declarations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DeclarationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Declarations");
        return declarationRepository.findAll(pageable).map(declarationMapper::toDto);
    }

    /**
     *  Get all the declarations where Applicant is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DeclarationDTO> findAllWhereApplicantIsNull() {
        log.debug("Request to get all declarations where Applicant is null");
        return StreamSupport
            .stream(declarationRepository.findAll().spliterator(), false)
            .filter(declaration -> declaration.getApplicant() == null)
            .map(declarationMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one declaration by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DeclarationDTO> findOne(Long id) {
        log.debug("Request to get Declaration : {}", id);
        return declarationRepository.findById(id).map(declarationMapper::toDto);
    }

    /**
     * Delete the declaration by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Declaration : {}", id);
        declarationRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Optional<DeclarationDTO> findByApplicant(ApplicantDTO applicantDTO) {
        log.debug("Request for declaration details for applicant {}", applicantDTO);
        return declarationRepository.findByApplicant(applicantMapper.toEntity(applicantDTO)).map(declarationMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<DeclarationDTO> findByApplicantAsCurrentUser(Pageable pageable) {
        log.debug("Request for declarations for currently logged in user");
        return declarationRepository.findByApplicantAsCurrentUser(pageable).map(declarationMapper::toDto);
    }
}

package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.Passport;
import com.afrikatek.documentsservice.repository.PassportRepository;
import com.afrikatek.documentsservice.service.dto.PassportDTO;
import com.afrikatek.documentsservice.service.mapper.PassportMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Passport}.
 */
@Service
@Transactional
public class PassportService {

    private final Logger log = LoggerFactory.getLogger(PassportService.class);

    private final PassportRepository passportRepository;

    private final PassportMapper passportMapper;

    public PassportService(PassportRepository passportRepository, PassportMapper passportMapper) {
        this.passportRepository = passportRepository;
        this.passportMapper = passportMapper;
    }

    /**
     * Save a passport.
     *
     * @param passportDTO the entity to save.
     * @return the persisted entity.
     */
    public PassportDTO save(PassportDTO passportDTO) {
        log.debug("Request to save Passport : {}", passportDTO);
        Passport passport = passportMapper.toEntity(passportDTO);
        passport = passportRepository.save(passport);
        return passportMapper.toDto(passport);
    }

    /**
     * Partially update a passport.
     *
     * @param passportDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PassportDTO> partialUpdate(PassportDTO passportDTO) {
        log.debug("Request to partially update Passport : {}", passportDTO);

        return passportRepository
            .findById(passportDTO.getId())
            .map(
                existingPassport -> {
                    passportMapper.partialUpdate(existingPassport, passportDTO);
                    return existingPassport;
                }
            )
            .map(passportRepository::save)
            .map(passportMapper::toDto);
    }

    /**
     * Get all the passports.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PassportDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Passports");
        return passportRepository.findAll(pageable).map(passportMapper::toDto);
    }

    /**
     * Get one passport by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PassportDTO> findOne(Long id) {
        log.debug("Request to get Passport : {}", id);
        return passportRepository.findById(id).map(passportMapper::toDto);
    }

    /**
     * Delete the passport by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Passport : {}", id);
        passportRepository.deleteById(id);
    }
}

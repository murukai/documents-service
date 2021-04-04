package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.AppointmentSettings;
import com.afrikatek.documentsservice.repository.AppointmentSettingsRepository;
import com.afrikatek.documentsservice.service.dto.AppointmentSettingsDTO;
import com.afrikatek.documentsservice.service.mapper.AppointmentSettingsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppointmentSettings}.
 */
@Service
@Transactional
public class AppointmentSettingsService {

    private final Logger log = LoggerFactory.getLogger(AppointmentSettingsService.class);

    private final AppointmentSettingsRepository appointmentSettingsRepository;

    private final AppointmentSettingsMapper appointmentSettingsMapper;

    public AppointmentSettingsService(
        AppointmentSettingsRepository appointmentSettingsRepository,
        AppointmentSettingsMapper appointmentSettingsMapper
    ) {
        this.appointmentSettingsRepository = appointmentSettingsRepository;
        this.appointmentSettingsMapper = appointmentSettingsMapper;
    }

    /**
     * Save a appointmentSettings.
     *
     * @param appointmentSettingsDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointmentSettingsDTO save(AppointmentSettingsDTO appointmentSettingsDTO) {
        log.debug("Request to save AppointmentSettings : {}", appointmentSettingsDTO);
        AppointmentSettings appointmentSettings = appointmentSettingsMapper.toEntity(appointmentSettingsDTO);
        appointmentSettings = appointmentSettingsRepository.save(appointmentSettings);
        return appointmentSettingsMapper.toDto(appointmentSettings);
    }

    /**
     * Partially update a appointmentSettings.
     *
     * @param appointmentSettingsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppointmentSettingsDTO> partialUpdate(AppointmentSettingsDTO appointmentSettingsDTO) {
        log.debug("Request to partially update AppointmentSettings : {}", appointmentSettingsDTO);

        return appointmentSettingsRepository
            .findById(appointmentSettingsDTO.getId())
            .map(
                existingAppointmentSettings -> {
                    appointmentSettingsMapper.partialUpdate(existingAppointmentSettings, appointmentSettingsDTO);
                    return existingAppointmentSettings;
                }
            )
            .map(appointmentSettingsRepository::save)
            .map(appointmentSettingsMapper::toDto);
    }

    /**
     * Get all the appointmentSettings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentSettingsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppointmentSettings");
        return appointmentSettingsRepository.findAll(pageable).map(appointmentSettingsMapper::toDto);
    }

    /**
     * Get one appointmentSettings by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppointmentSettingsDTO> findOne(Long id) {
        log.debug("Request to get AppointmentSettings : {}", id);
        return appointmentSettingsRepository.findById(id).map(appointmentSettingsMapper::toDto);
    }

    /**
     * Delete the appointmentSettings by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppointmentSettings : {}", id);
        appointmentSettingsRepository.deleteById(id);
    }
}

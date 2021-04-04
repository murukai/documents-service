package com.afrikatek.documentsservice.service;

import com.afrikatek.documentsservice.domain.AppointmentSlot;
import com.afrikatek.documentsservice.repository.AppointmentSlotRepository;
import com.afrikatek.documentsservice.service.dto.AppointmentSlotDTO;
import com.afrikatek.documentsservice.service.mapper.AppointmentSlotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link AppointmentSlot}.
 */
@Service
@Transactional
public class AppointmentSlotService {

    private final Logger log = LoggerFactory.getLogger(AppointmentSlotService.class);

    private final AppointmentSlotRepository appointmentSlotRepository;

    private final AppointmentSlotMapper appointmentSlotMapper;

    public AppointmentSlotService(AppointmentSlotRepository appointmentSlotRepository, AppointmentSlotMapper appointmentSlotMapper) {
        this.appointmentSlotRepository = appointmentSlotRepository;
        this.appointmentSlotMapper = appointmentSlotMapper;
    }

    /**
     * Save a appointmentSlot.
     *
     * @param appointmentSlotDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointmentSlotDTO save(AppointmentSlotDTO appointmentSlotDTO) {
        log.debug("Request to save AppointmentSlot : {}", appointmentSlotDTO);
        AppointmentSlot appointmentSlot = appointmentSlotMapper.toEntity(appointmentSlotDTO);
        appointmentSlot = appointmentSlotRepository.save(appointmentSlot);
        return appointmentSlotMapper.toDto(appointmentSlot);
    }

    /**
     * Partially update a appointmentSlot.
     *
     * @param appointmentSlotDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppointmentSlotDTO> partialUpdate(AppointmentSlotDTO appointmentSlotDTO) {
        log.debug("Request to partially update AppointmentSlot : {}", appointmentSlotDTO);

        return appointmentSlotRepository
            .findById(appointmentSlotDTO.getId())
            .map(
                existingAppointmentSlot -> {
                    appointmentSlotMapper.partialUpdate(existingAppointmentSlot, appointmentSlotDTO);
                    return existingAppointmentSlot;
                }
            )
            .map(appointmentSlotRepository::save)
            .map(appointmentSlotMapper::toDto);
    }

    /**
     * Get all the appointmentSlots.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointmentSlotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all AppointmentSlots");
        return appointmentSlotRepository.findAll(pageable).map(appointmentSlotMapper::toDto);
    }

    /**
     * Get one appointmentSlot by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppointmentSlotDTO> findOne(Long id) {
        log.debug("Request to get AppointmentSlot : {}", id);
        return appointmentSlotRepository.findById(id).map(appointmentSlotMapper::toDto);
    }

    /**
     * Delete the appointmentSlot by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AppointmentSlot : {}", id);
        appointmentSlotRepository.deleteById(id);
    }
}

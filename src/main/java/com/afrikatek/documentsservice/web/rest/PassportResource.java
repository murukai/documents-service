package com.afrikatek.documentsservice.web.rest;

import com.afrikatek.documentsservice.repository.PassportRepository;
import com.afrikatek.documentsservice.service.PassportService;
import com.afrikatek.documentsservice.service.dto.PassportDTO;
import com.afrikatek.documentsservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.afrikatek.documentsservice.domain.Passport}.
 */
@RestController
@RequestMapping("/api")
public class PassportResource {

    private final Logger log = LoggerFactory.getLogger(PassportResource.class);

    private static final String ENTITY_NAME = "passport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PassportService passportService;

    private final PassportRepository passportRepository;

    public PassportResource(PassportService passportService, PassportRepository passportRepository) {
        this.passportService = passportService;
        this.passportRepository = passportRepository;
    }

    /**
     * {@code POST  /passports} : Create a new passport.
     *
     * @param passportDTO the passportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new passportDTO, or with status {@code 400 (Bad Request)} if the passport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/passports")
    public ResponseEntity<PassportDTO> createPassport(@Valid @RequestBody PassportDTO passportDTO) throws URISyntaxException {
        log.debug("REST request to save Passport : {}", passportDTO);
        if (passportDTO.getId() != null) {
            throw new BadRequestAlertException("A new passport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PassportDTO result = passportService.save(passportDTO);
        return ResponseEntity
            .created(new URI("/api/passports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /passports/:id} : Updates an existing passport.
     *
     * @param id the id of the passportDTO to save.
     * @param passportDTO the passportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passportDTO,
     * or with status {@code 400 (Bad Request)} if the passportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the passportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/passports/{id}")
    public ResponseEntity<PassportDTO> updatePassport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PassportDTO passportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Passport : {}, {}", id, passportDTO);
        if (passportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PassportDTO result = passportService.save(passportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, passportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /passports/:id} : Partial updates given fields of an existing passport, field will ignore if it is null
     *
     * @param id the id of the passportDTO to save.
     * @param passportDTO the passportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated passportDTO,
     * or with status {@code 400 (Bad Request)} if the passportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the passportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the passportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/passports/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<PassportDTO> partialUpdatePassport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PassportDTO passportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Passport partially : {}, {}", id, passportDTO);
        if (passportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, passportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!passportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PassportDTO> result = passportService.partialUpdate(passportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, passportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /passports} : get all the passports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of passports in body.
     */
    @GetMapping("/passports")
    public ResponseEntity<List<PassportDTO>> getAllPassports(Pageable pageable) {
        log.debug("REST request to get a page of Passports");
        Page<PassportDTO> page = passportService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /passports/:id} : get the "id" passport.
     *
     * @param id the id of the passportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the passportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/passports/{id}")
    public ResponseEntity<PassportDTO> getPassport(@PathVariable Long id) {
        log.debug("REST request to get Passport : {}", id);
        Optional<PassportDTO> passportDTO = passportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(passportDTO);
    }

    /**
     * {@code DELETE  /passports/:id} : delete the "id" passport.
     *
     * @param id the id of the passportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/passports/{id}")
    public ResponseEntity<Void> deletePassport(@PathVariable Long id) {
        log.debug("REST request to delete Passport : {}", id);
        passportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

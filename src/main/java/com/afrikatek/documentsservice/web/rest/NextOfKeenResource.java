package com.afrikatek.documentsservice.web.rest;

import com.afrikatek.documentsservice.repository.NextOfKeenRepository;
import com.afrikatek.documentsservice.service.NextOfKeenService;
import com.afrikatek.documentsservice.service.dto.NextOfKeenDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.afrikatek.documentsservice.domain.NextOfKeen}.
 */
@RestController
@RequestMapping("/api")
public class NextOfKeenResource {

    private final Logger log = LoggerFactory.getLogger(NextOfKeenResource.class);

    private static final String ENTITY_NAME = "nextOfKeen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NextOfKeenService nextOfKeenService;

    private final NextOfKeenRepository nextOfKeenRepository;

    public NextOfKeenResource(NextOfKeenService nextOfKeenService, NextOfKeenRepository nextOfKeenRepository) {
        this.nextOfKeenService = nextOfKeenService;
        this.nextOfKeenRepository = nextOfKeenRepository;
    }

    /**
     * {@code POST  /next-of-keens} : Create a new nextOfKeen.
     *
     * @param nextOfKeenDTO the nextOfKeenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nextOfKeenDTO, or with status {@code 400 (Bad Request)} if the nextOfKeen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/next-of-keens")
    public ResponseEntity<NextOfKeenDTO> createNextOfKeen(@Valid @RequestBody NextOfKeenDTO nextOfKeenDTO) throws URISyntaxException {
        log.debug("REST request to save NextOfKeen : {}", nextOfKeenDTO);
        if (nextOfKeenDTO.getId() != null) {
            throw new BadRequestAlertException("A new nextOfKeen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NextOfKeenDTO result = nextOfKeenService.save(nextOfKeenDTO);
        return ResponseEntity
            .created(new URI("/api/next-of-keens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /next-of-keens/:id} : Updates an existing nextOfKeen.
     *
     * @param id the id of the nextOfKeenDTO to save.
     * @param nextOfKeenDTO the nextOfKeenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOfKeenDTO,
     * or with status {@code 400 (Bad Request)} if the nextOfKeenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nextOfKeenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/next-of-keens/{id}")
    public ResponseEntity<NextOfKeenDTO> updateNextOfKeen(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NextOfKeenDTO nextOfKeenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NextOfKeen : {}, {}", id, nextOfKeenDTO);
        if (nextOfKeenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOfKeenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOfKeenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NextOfKeenDTO result = nextOfKeenService.save(nextOfKeenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nextOfKeenDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /next-of-keens/:id} : Partial updates given fields of an existing nextOfKeen, field will ignore if it is null
     *
     * @param id the id of the nextOfKeenDTO to save.
     * @param nextOfKeenDTO the nextOfKeenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nextOfKeenDTO,
     * or with status {@code 400 (Bad Request)} if the nextOfKeenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nextOfKeenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nextOfKeenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/next-of-keens/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<NextOfKeenDTO> partialUpdateNextOfKeen(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NextOfKeenDTO nextOfKeenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NextOfKeen partially : {}, {}", id, nextOfKeenDTO);
        if (nextOfKeenDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nextOfKeenDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nextOfKeenRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NextOfKeenDTO> result = nextOfKeenService.partialUpdate(nextOfKeenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, nextOfKeenDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /next-of-keens} : get all the nextOfKeens.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nextOfKeens in body.
     */
    @GetMapping("/next-of-keens")
    public ResponseEntity<List<NextOfKeenDTO>> getAllNextOfKeens(Pageable pageable) {
        log.debug("REST request to get a page of NextOfKeens");
        Page<NextOfKeenDTO> page = nextOfKeenService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /next-of-keens/:id} : get the "id" nextOfKeen.
     *
     * @param id the id of the nextOfKeenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nextOfKeenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/next-of-keens/{id}")
    public ResponseEntity<NextOfKeenDTO> getNextOfKeen(@PathVariable Long id) {
        log.debug("REST request to get NextOfKeen : {}", id);
        Optional<NextOfKeenDTO> nextOfKeenDTO = nextOfKeenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nextOfKeenDTO);
    }

    /**
     * {@code DELETE  /next-of-keens/:id} : delete the "id" nextOfKeen.
     *
     * @param id the id of the nextOfKeenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/next-of-keens/{id}")
    public ResponseEntity<Void> deleteNextOfKeen(@PathVariable Long id) {
        log.debug("REST request to delete NextOfKeen : {}", id);
        nextOfKeenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

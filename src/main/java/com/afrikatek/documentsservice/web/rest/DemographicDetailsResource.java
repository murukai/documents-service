package com.afrikatek.documentsservice.web.rest;

import com.afrikatek.documentsservice.repository.DemographicDetailsRepository;
import com.afrikatek.documentsservice.service.DemographicDetailsService;
import com.afrikatek.documentsservice.service.dto.DemographicDetailsDTO;
import com.afrikatek.documentsservice.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.afrikatek.documentsservice.domain.DemographicDetails}.
 */
@RestController
@RequestMapping("/api")
public class DemographicDetailsResource {

    private final Logger log = LoggerFactory.getLogger(DemographicDetailsResource.class);

    private static final String ENTITY_NAME = "demographicDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DemographicDetailsService demographicDetailsService;

    private final DemographicDetailsRepository demographicDetailsRepository;

    public DemographicDetailsResource(
        DemographicDetailsService demographicDetailsService,
        DemographicDetailsRepository demographicDetailsRepository
    ) {
        this.demographicDetailsService = demographicDetailsService;
        this.demographicDetailsRepository = demographicDetailsRepository;
    }

    /**
     * {@code POST  /demographic-details} : Create a new demographicDetails.
     *
     * @param demographicDetailsDTO the demographicDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new demographicDetailsDTO, or with status {@code 400 (Bad Request)} if the demographicDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/demographic-details")
    public ResponseEntity<DemographicDetailsDTO> createDemographicDetails(@Valid @RequestBody DemographicDetailsDTO demographicDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save DemographicDetails : {}", demographicDetailsDTO);
        if (demographicDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new demographicDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DemographicDetailsDTO result = demographicDetailsService.save(demographicDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/demographic-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /demographic-details/:id} : Updates an existing demographicDetails.
     *
     * @param id the id of the demographicDetailsDTO to save.
     * @param demographicDetailsDTO the demographicDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demographicDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the demographicDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the demographicDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/demographic-details/{id}")
    public ResponseEntity<DemographicDetailsDTO> updateDemographicDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DemographicDetailsDTO demographicDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DemographicDetails : {}, {}", id, demographicDetailsDTO);
        if (demographicDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demographicDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demographicDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DemographicDetailsDTO result = demographicDetailsService.save(demographicDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demographicDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /demographic-details/:id} : Partial updates given fields of an existing demographicDetails, field will ignore if it is null
     *
     * @param id the id of the demographicDetailsDTO to save.
     * @param demographicDetailsDTO the demographicDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated demographicDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the demographicDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the demographicDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the demographicDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/demographic-details/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DemographicDetailsDTO> partialUpdateDemographicDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DemographicDetailsDTO demographicDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DemographicDetails partially : {}, {}", id, demographicDetailsDTO);
        if (demographicDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, demographicDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!demographicDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DemographicDetailsDTO> result = demographicDetailsService.partialUpdate(demographicDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, demographicDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /demographic-details} : get all the demographicDetails.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of demographicDetails in body.
     */
    @GetMapping("/demographic-details")
    public ResponseEntity<List<DemographicDetailsDTO>> getAllDemographicDetails(
        Pageable pageable,
        @RequestParam(required = false) String filter
    ) {
        if ("applicant-is-null".equals(filter)) {
            log.debug("REST request to get all DemographicDetailss where applicant is null");
            return new ResponseEntity<>(demographicDetailsService.findAllWhereApplicantIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of DemographicDetails");
        Page<DemographicDetailsDTO> page = demographicDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /demographic-details/:id} : get the "id" demographicDetails.
     *
     * @param id the id of the demographicDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the demographicDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/demographic-details/{id}")
    public ResponseEntity<DemographicDetailsDTO> getDemographicDetails(@PathVariable Long id) {
        log.debug("REST request to get DemographicDetails : {}", id);
        Optional<DemographicDetailsDTO> demographicDetailsDTO = demographicDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(demographicDetailsDTO);
    }

    /**
     * {@code DELETE  /demographic-details/:id} : delete the "id" demographicDetails.
     *
     * @param id the id of the demographicDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/demographic-details/{id}")
    public ResponseEntity<Void> deleteDemographicDetails(@PathVariable Long id) {
        log.debug("REST request to delete DemographicDetails : {}", id);
        demographicDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

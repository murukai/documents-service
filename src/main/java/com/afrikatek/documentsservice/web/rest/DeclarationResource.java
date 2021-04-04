package com.afrikatek.documentsservice.web.rest;

import com.afrikatek.documentsservice.repository.DeclarationRepository;
import com.afrikatek.documentsservice.service.DeclarationService;
import com.afrikatek.documentsservice.service.dto.DeclarationDTO;
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
 * REST controller for managing {@link com.afrikatek.documentsservice.domain.Declaration}.
 */
@RestController
@RequestMapping("/api")
public class DeclarationResource {

    private final Logger log = LoggerFactory.getLogger(DeclarationResource.class);

    private static final String ENTITY_NAME = "declaration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeclarationService declarationService;

    private final DeclarationRepository declarationRepository;

    public DeclarationResource(DeclarationService declarationService, DeclarationRepository declarationRepository) {
        this.declarationService = declarationService;
        this.declarationRepository = declarationRepository;
    }

    /**
     * {@code POST  /declarations} : Create a new declaration.
     *
     * @param declarationDTO the declarationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new declarationDTO, or with status {@code 400 (Bad Request)} if the declaration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/declarations")
    public ResponseEntity<DeclarationDTO> createDeclaration(@Valid @RequestBody DeclarationDTO declarationDTO) throws URISyntaxException {
        log.debug("REST request to save Declaration : {}", declarationDTO);
        if (declarationDTO.getId() != null) {
            throw new BadRequestAlertException("A new declaration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeclarationDTO result = declarationService.save(declarationDTO);
        return ResponseEntity
            .created(new URI("/api/declarations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /declarations/:id} : Updates an existing declaration.
     *
     * @param id the id of the declarationDTO to save.
     * @param declarationDTO the declarationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated declarationDTO,
     * or with status {@code 400 (Bad Request)} if the declarationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the declarationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/declarations/{id}")
    public ResponseEntity<DeclarationDTO> updateDeclaration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeclarationDTO declarationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Declaration : {}, {}", id, declarationDTO);
        if (declarationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, declarationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!declarationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeclarationDTO result = declarationService.save(declarationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, declarationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /declarations/:id} : Partial updates given fields of an existing declaration, field will ignore if it is null
     *
     * @param id the id of the declarationDTO to save.
     * @param declarationDTO the declarationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated declarationDTO,
     * or with status {@code 400 (Bad Request)} if the declarationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the declarationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the declarationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/declarations/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<DeclarationDTO> partialUpdateDeclaration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeclarationDTO declarationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Declaration partially : {}, {}", id, declarationDTO);
        if (declarationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, declarationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!declarationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeclarationDTO> result = declarationService.partialUpdate(declarationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, declarationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /declarations} : get all the declarations.
     *
     * @param pageable the pagination information.
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of declarations in body.
     */
    @GetMapping("/declarations")
    public ResponseEntity<List<DeclarationDTO>> getAllDeclarations(Pageable pageable, @RequestParam(required = false) String filter) {
        if ("applicant-is-null".equals(filter)) {
            log.debug("REST request to get all Declarations where applicant is null");
            return new ResponseEntity<>(declarationService.findAllWhereApplicantIsNull(), HttpStatus.OK);
        }
        log.debug("REST request to get a page of Declarations");
        Page<DeclarationDTO> page = declarationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /declarations/:id} : get the "id" declaration.
     *
     * @param id the id of the declarationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the declarationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/declarations/{id}")
    public ResponseEntity<DeclarationDTO> getDeclaration(@PathVariable Long id) {
        log.debug("REST request to get Declaration : {}", id);
        Optional<DeclarationDTO> declarationDTO = declarationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(declarationDTO);
    }

    /**
     * {@code DELETE  /declarations/:id} : delete the "id" declaration.
     *
     * @param id the id of the declarationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/declarations/{id}")
    public ResponseEntity<Void> deleteDeclaration(@PathVariable Long id) {
        log.debug("REST request to delete Declaration : {}", id);
        declarationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}

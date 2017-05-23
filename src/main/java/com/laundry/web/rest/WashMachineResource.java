package com.laundry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.laundry.domain.WashMachine;

import com.laundry.repository.WashMachineRepository;
import com.laundry.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WashMachine.
 */
@RestController
@RequestMapping("/api")
public class WashMachineResource {

    private final Logger log = LoggerFactory.getLogger(WashMachineResource.class);

    private static final String ENTITY_NAME = "washMachine";
        
    private final WashMachineRepository washMachineRepository;

    public WashMachineResource(WashMachineRepository washMachineRepository) {
        this.washMachineRepository = washMachineRepository;
    }

    /**
     * POST  /wash-machines : Create a new washMachine.
     *
     * @param washMachine the washMachine to create
     * @return the ResponseEntity with status 201 (Created) and with body the new washMachine, or with status 400 (Bad Request) if the washMachine has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wash-machines")
    @Timed
    public ResponseEntity<WashMachine> createWashMachine(@Valid @RequestBody WashMachine washMachine) throws URISyntaxException {
        log.debug("REST request to save WashMachine : {}", washMachine);
        if (washMachine.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new washMachine cannot already have an ID")).body(null);
        }
        WashMachine result = washMachineRepository.save(washMachine);
        return ResponseEntity.created(new URI("/api/wash-machines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wash-machines : Updates an existing washMachine.
     *
     * @param washMachine the washMachine to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated washMachine,
     * or with status 400 (Bad Request) if the washMachine is not valid,
     * or with status 500 (Internal Server Error) if the washMachine couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wash-machines")
    @Timed
    public ResponseEntity<WashMachine> updateWashMachine(@Valid @RequestBody WashMachine washMachine) throws URISyntaxException {
        log.debug("REST request to update WashMachine : {}", washMachine);
        if (washMachine.getId() == null) {
            return createWashMachine(washMachine);
        }
        WashMachine result = washMachineRepository.save(washMachine);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, washMachine.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wash-machines : get all the washMachines.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of washMachines in body
     */
    @GetMapping("/wash-machines")
    @Timed
    public List<WashMachine> getAllWashMachines() {
        log.debug("REST request to get all WashMachines");
        List<WashMachine> washMachines = washMachineRepository.findAll();
        return washMachines;
    }

    /**
     * GET  /wash-machines/:id : get the "id" washMachine.
     *
     * @param id the id of the washMachine to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the washMachine, or with status 404 (Not Found)
     */
    @GetMapping("/wash-machines/{id}")
    @Timed
    public ResponseEntity<WashMachine> getWashMachine(@PathVariable Long id) {
        log.debug("REST request to get WashMachine : {}", id);
        WashMachine washMachine = washMachineRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(washMachine));
    }

    /**
     * DELETE  /wash-machines/:id : delete the "id" washMachine.
     *
     * @param id the id of the washMachine to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wash-machines/{id}")
    @Timed
    public ResponseEntity<Void> deleteWashMachine(@PathVariable Long id) {
        log.debug("REST request to delete WashMachine : {}", id);
        washMachineRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

}

package com.laundry.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.laundry.domain.WashPrice;

import com.laundry.repository.WashPriceRepository;
import com.laundry.service.WashPriceService;
import com.laundry.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing WashPrice.
 */
@RestController
@RequestMapping("/api")
public class WashPriceResource {

    private final Logger log = LoggerFactory.getLogger(WashPriceResource.class);

    private static final String ENTITY_NAME = "washPrice";

    private final WashPriceService washPriceService;

    public WashPriceResource(WashPriceService washPriceService) {
        this.washPriceService = washPriceService;
    }

    /**
     * POST  /wash-prices : Create a new washPrice.
     *
     * @param washPrice the washPrice to create
     * @return the ResponseEntity with status 201 (Created) and with body the new washPrice, or with status 400 (Bad Request) if the washPrice has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/wash-prices")
    @Timed
    public ResponseEntity<WashPrice> createWashPrice(@Valid @RequestBody WashPrice washPrice) throws URISyntaxException {
        log.debug("REST request to save WashPrice : {}", washPrice);
        if (washPrice.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new washPrice cannot already have an ID")).body(null);
        }
        WashPrice result = washPriceService.save(washPrice);
        return ResponseEntity.created(new URI("/api/wash-prices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /wash-prices : Updates an existing washPrice.
     *
     * @param washPrice the washPrice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated washPrice,
     * or with status 400 (Bad Request) if the washPrice is not valid,
     * or with status 500 (Internal Server Error) if the washPrice couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/wash-prices")
    @Timed
    public ResponseEntity<WashPrice> updateWashPrice(@Valid @RequestBody WashPrice washPrice) throws URISyntaxException {
        log.debug("REST request to update WashPrice : {}", washPrice);
        if (washPrice.getId() == null) {
            return createWashPrice(washPrice);
        }
        WashPrice result = washPriceService.save(washPrice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, washPrice.getId().toString()))
            .body(result);
    }

    /**
     * GET  /wash-prices : get all the washPrices.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of washPrices in body
     */
    @GetMapping("/wash-prices")
    @Timed
    public List<WashPrice> getAllWashPrices() {
        log.debug("REST request to get all WashPrices");
        return washPriceService.findAll();
    }

    /**
     * GET  /wash-prices/:id : get the "id" washPrice.
     *
     * @param id the id of the washPrice to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the washPrice, or with status 404 (Not Found)
     */
    @GetMapping("/wash-prices/{id}")
    @Timed
    public ResponseEntity<WashPrice> getWashPrice(@PathVariable Long id) {
        log.debug("REST request to get WashPrice : {}", id);
        WashPrice washPrice = washPriceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(washPrice));
    }

    /**
     * DELETE  /wash-prices/:id : delete the "id" washPrice.
     *
     * @param id the id of the washPrice to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/wash-prices/{id}")
    @Timed
    public ResponseEntity<Void> deleteWashPrice(@PathVariable Long id) {
        log.debug("REST request to delete WashPrice : {}", id);
        washPriceService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
    
    /**
     * GET  /current-price : get current wash price per kg per hour.
     * @return the ResponseEntity with status 200 (OK) and with body washPrice as {@link BigDecimal}, or with status 404 (Not Found)
     */
    @GetMapping("/current-price")
    @Timed
    public ResponseEntity<BigDecimal> getCurrentWashPrice() {
        log.debug("REST request to get current WashPrice");
        Optional<BigDecimal> washPrice = washPriceService.getCurrentPrice();
        return ResponseUtil.wrapOrNotFound(washPrice);
    }
    
    /**
     * PUT  /current-price : Updates current wash price.
     *
     * @param washPrice the washPrice to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated washPrice,
     * or with status 400 (Bad Request) if the washPrice is not valid,
     * or with status 500 (Internal Server Error) if the washPrice couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/current-price")
    @Timed
    public ResponseEntity<BigDecimal> updateCurrentWashPrice(@Valid @RequestBody(required=true) BigDecimal washPrice) throws URISyntaxException {
        log.debug("REST request to update current wash price : {}", washPrice);
        washPriceService.setCurrentPrice(washPrice);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, washPrice.toString()))
            .body(washPrice);
    }
    
}

package com.laundry.web.rest;

import static com.laundry.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.laundry.LaundryApp;
import com.laundry.domain.WashPrice;
import com.laundry.service.WashPriceService;
import com.laundry.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the WashPriceResource REST controller.
 *
 * @see WashPriceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaundryApp.class)
public class WashPriceResourceIntTest {

    private final Logger log = LoggerFactory.getLogger(WashPriceResourceIntTest.class);
	
    private static final BigDecimal DEFAULT_PRICE_KG_HOUR = new BigDecimal(0);
    private static final BigDecimal UPDATED_PRICE_KG_HOUR = new BigDecimal(1);

    private static final ZonedDateTime DEFAULT_EFFERCTIVE_TO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EFFERCTIVE_TO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private WashPriceService washPriceService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWashPriceMockMvc;

    private WashPrice washPrice;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WashPriceResource washPriceResource = new WashPriceResource(washPriceService);
        this.restWashPriceMockMvc = MockMvcBuilders.standaloneSetup(washPriceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WashPrice createEntity(EntityManager em) {
        WashPrice washPrice = new WashPrice(DEFAULT_PRICE_KG_HOUR)
            .efferctiveTo(DEFAULT_EFFERCTIVE_TO);
        return washPrice;
    }

    @Before
    public void initTest() {
        washPrice = createEntity(em);
    }

    @Test
    @Transactional
    public void createWashPrice() throws Exception {
        int databaseSizeBeforeCreate = washPriceService.findAll().size();

        // Create the WashPrice
        restWashPriceMockMvc.perform(post("/api/wash-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washPrice)))
            .andExpect(status().isCreated());

        // Validate the WashPrice in the database
        List<WashPrice> washPriceList = washPriceService.findAll();
        assertThat(washPriceList).hasSize(databaseSizeBeforeCreate + 1);
        WashPrice testWashPrice = washPriceList.get(washPriceList.size() - 1);
        assertThat(testWashPrice.getPriceKgHour()).isEqualTo(DEFAULT_PRICE_KG_HOUR);
        assertThat(testWashPrice.getEfferctiveTo()).isEqualTo(DEFAULT_EFFERCTIVE_TO);
    }

    @Test
    @Transactional
    public void createWashPriceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = washPriceService.findAll().size();

        // Create the WashPrice with an existing ID
        washPrice.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWashPriceMockMvc.perform(post("/api/wash-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washPrice)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WashPrice> washPriceList = washPriceService.findAll();
        assertThat(washPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPriceKgHourIsRequired() throws Exception {
        int databaseSizeBeforeTest = washPriceService.findAll().size();
        // set the field null
        washPrice.setPriceKgHour(null);

        // Create the WashPrice, which fails.

        restWashPriceMockMvc.perform(post("/api/wash-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washPrice)))
            .andExpect(status().isBadRequest());

        List<WashPrice> washPriceList = washPriceService.findAll();
        assertThat(washPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWashPrices() throws Exception {
        // Initialize the database
        washPriceService.saveAndFlush(washPrice);

        // Get all the washPriceList
        restWashPriceMockMvc.perform(get("/api/wash-prices?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(washPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].priceKgHour").value(hasItem(DEFAULT_PRICE_KG_HOUR.intValue())))
            .andExpect(jsonPath("$.[*].efferctiveTo").value(hasItem(sameInstant(DEFAULT_EFFERCTIVE_TO))));
    }

    @Test
    @Transactional
    public void getWashPrice() throws Exception {
        // Initialize the database
        washPriceService.saveAndFlush(washPrice);

        // Get the washPrice
        restWashPriceMockMvc.perform(get("/api/wash-prices/{id}", washPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(washPrice.getId().intValue()))
            .andExpect(jsonPath("$.priceKgHour").value(DEFAULT_PRICE_KG_HOUR.intValue()))
            .andExpect(jsonPath("$.efferctiveTo").value(sameInstant(DEFAULT_EFFERCTIVE_TO)));
    }

    @Test
    @Transactional
    public void getNonExistingWashPrice() throws Exception {
        // Get the washPrice
        restWashPriceMockMvc.perform(get("/api/wash-prices/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWashPrice() throws Exception {
        // Initialize the database
        washPriceService.saveAndFlush(washPrice);
        int databaseSizeBeforeUpdate = washPriceService.findAll().size();

        // Update the washPrice
        WashPrice updatedWashPrice = washPriceService.findOne(washPrice.getId());
        updatedWashPrice
            .priceKgHour(UPDATED_PRICE_KG_HOUR)
            .efferctiveTo(UPDATED_EFFERCTIVE_TO);

        restWashPriceMockMvc.perform(put("/api/wash-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWashPrice)))
            .andExpect(status().isOk());

        // Validate the WashPrice in the database
        List<WashPrice> washPriceList = washPriceService.findAll();
        assertThat(washPriceList).hasSize(databaseSizeBeforeUpdate);
        WashPrice testWashPrice = washPriceList.get(washPriceList.size() - 1);
        assertThat(testWashPrice.getPriceKgHour()).isEqualTo(UPDATED_PRICE_KG_HOUR);
        assertThat(testWashPrice.getEfferctiveTo()).isEqualTo(UPDATED_EFFERCTIVE_TO);
    }

    @Test
    @Transactional
    public void updateNonExistingWashPrice() throws Exception {
        int databaseSizeBeforeUpdate = washPriceService.findAll().size();

        // Create the WashPrice

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWashPriceMockMvc.perform(put("/api/wash-prices")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washPrice)))
            .andExpect(status().isCreated());

        // Validate the WashPrice in the database
        List<WashPrice> washPriceList = washPriceService.findAll();
        assertThat(washPriceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWashPrice() throws Exception {
        // Initialize the database
        washPriceService.saveAndFlush(washPrice);
        int databaseSizeBeforeDelete = washPriceService.findAll().size();

        // Get the washPrice
        restWashPriceMockMvc.perform(delete("/api/wash-prices/{id}", washPrice.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WashPrice> washPriceList = washPriceService.findAll();
        assertThat(washPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WashPrice.class);
        WashPrice washPrice1 = new WashPrice(BigDecimal.ZERO);
        washPrice1.setId(1L);
        WashPrice washPrice2 = new WashPrice(BigDecimal.ZERO);
        washPrice2.setId(washPrice1.getId());
        assertThat(washPrice1).isEqualTo(washPrice2);
        washPrice2.setId(2L);
        assertThat(washPrice1).isNotEqualTo(washPrice2);
        washPrice1.setId(null);
        assertThat(washPrice1).isNotEqualTo(washPrice2);
    }
    
    @Test
    @Transactional
    public void getCurrentPrice() throws Exception {
        // Initialize the database
        washPriceService.setCurrentPrice(BigDecimal.valueOf(123.45));

        // Get the washPrice
        restWashPriceMockMvc.perform(get("/api/current-price"))
        	.andDo(result-> log.info(result.getResponse().getContentAsString()))
            .andExpect(status().isOk())
            .andExpect(content().string("123.45"))
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    
}

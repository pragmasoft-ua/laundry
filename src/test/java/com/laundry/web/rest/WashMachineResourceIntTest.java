package com.laundry.web.rest;

import com.laundry.LaundryApp;

import com.laundry.domain.WashMachine;
import com.laundry.repository.WashMachineRepository;
import com.laundry.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the WashMachineResource REST controller.
 *
 * @see WashMachineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaundryApp.class)
public class WashMachineResourceIntTest {

    private static final Integer DEFAULT_CAPACITY = 0;
    private static final Integer UPDATED_CAPACITY = 1;

    private static final Boolean DEFAULT_AVAILABLE = false;
    private static final Boolean UPDATED_AVAILABLE = true;

    @Autowired
    private WashMachineRepository washMachineRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restWashMachineMockMvc;

    private WashMachine washMachine;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        WashMachineResource washMachineResource = new WashMachineResource(washMachineRepository);
        this.restWashMachineMockMvc = MockMvcBuilders.standaloneSetup(washMachineResource)
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
    public static WashMachine createEntity(EntityManager em) {
        WashMachine washMachine = new WashMachine()
            .capacity(DEFAULT_CAPACITY)
            .available(DEFAULT_AVAILABLE);
        return washMachine;
    }

    @Before
    public void initTest() {
        washMachine = createEntity(em);
    }

    @Test
    @Transactional
    public void createWashMachine() throws Exception {
        int databaseSizeBeforeCreate = washMachineRepository.findAll().size();

        // Create the WashMachine
        restWashMachineMockMvc.perform(post("/api/wash-machines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washMachine)))
            .andExpect(status().isCreated());

        // Validate the WashMachine in the database
        List<WashMachine> washMachineList = washMachineRepository.findAll();
        assertThat(washMachineList).hasSize(databaseSizeBeforeCreate + 1);
        WashMachine testWashMachine = washMachineList.get(washMachineList.size() - 1);
        assertThat(testWashMachine.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
        assertThat(testWashMachine.isAvailable()).isEqualTo(DEFAULT_AVAILABLE);
    }

    @Test
    @Transactional
    public void createWashMachineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = washMachineRepository.findAll().size();

        // Create the WashMachine with an existing ID
        washMachine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWashMachineMockMvc.perform(post("/api/wash-machines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washMachine)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<WashMachine> washMachineList = washMachineRepository.findAll();
        assertThat(washMachineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCapacityIsRequired() throws Exception {
        int databaseSizeBeforeTest = washMachineRepository.findAll().size();
        // set the field null
        washMachine.setCapacity(null);

        // Create the WashMachine, which fails.

        restWashMachineMockMvc.perform(post("/api/wash-machines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washMachine)))
            .andExpect(status().isBadRequest());

        List<WashMachine> washMachineList = washMachineRepository.findAll();
        assertThat(washMachineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllWashMachines() throws Exception {
        // Initialize the database
        washMachineRepository.saveAndFlush(washMachine);

        // Get all the washMachineList
        restWashMachineMockMvc.perform(get("/api/wash-machines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(washMachine.getId().intValue())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)))
            .andExpect(jsonPath("$.[*].available").value(hasItem(DEFAULT_AVAILABLE.booleanValue())));
    }

    @Test
    @Transactional
    public void getWashMachine() throws Exception {
        // Initialize the database
        washMachineRepository.saveAndFlush(washMachine);

        // Get the washMachine
        restWashMachineMockMvc.perform(get("/api/wash-machines/{id}", washMachine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(washMachine.getId().intValue()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY))
            .andExpect(jsonPath("$.available").value(DEFAULT_AVAILABLE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingWashMachine() throws Exception {
        // Get the washMachine
        restWashMachineMockMvc.perform(get("/api/wash-machines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWashMachine() throws Exception {
        // Initialize the database
        washMachineRepository.saveAndFlush(washMachine);
        int databaseSizeBeforeUpdate = washMachineRepository.findAll().size();

        // Update the washMachine
        WashMachine updatedWashMachine = washMachineRepository.findOne(washMachine.getId());
        updatedWashMachine
            .capacity(UPDATED_CAPACITY)
            .available(UPDATED_AVAILABLE);

        restWashMachineMockMvc.perform(put("/api/wash-machines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedWashMachine)))
            .andExpect(status().isOk());

        // Validate the WashMachine in the database
        List<WashMachine> washMachineList = washMachineRepository.findAll();
        assertThat(washMachineList).hasSize(databaseSizeBeforeUpdate);
        WashMachine testWashMachine = washMachineList.get(washMachineList.size() - 1);
        assertThat(testWashMachine.getCapacity()).isEqualTo(UPDATED_CAPACITY);
        assertThat(testWashMachine.isAvailable()).isEqualTo(UPDATED_AVAILABLE);
    }

    @Test
    @Transactional
    public void updateNonExistingWashMachine() throws Exception {
        int databaseSizeBeforeUpdate = washMachineRepository.findAll().size();

        // Create the WashMachine

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restWashMachineMockMvc.perform(put("/api/wash-machines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(washMachine)))
            .andExpect(status().isCreated());

        // Validate the WashMachine in the database
        List<WashMachine> washMachineList = washMachineRepository.findAll();
        assertThat(washMachineList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteWashMachine() throws Exception {
        // Initialize the database
        washMachineRepository.saveAndFlush(washMachine);
        int databaseSizeBeforeDelete = washMachineRepository.findAll().size();

        // Get the washMachine
        restWashMachineMockMvc.perform(delete("/api/wash-machines/{id}", washMachine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<WashMachine> washMachineList = washMachineRepository.findAll();
        assertThat(washMachineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WashMachine.class);
        WashMachine washMachine1 = new WashMachine();
        washMachine1.setId(1L);
        WashMachine washMachine2 = new WashMachine();
        washMachine2.setId(washMachine1.getId());
        assertThat(washMachine1).isEqualTo(washMachine2);
        washMachine2.setId(2L);
        assertThat(washMachine1).isNotEqualTo(washMachine2);
        washMachine1.setId(null);
        assertThat(washMachine1).isNotEqualTo(washMachine2);
    }
}

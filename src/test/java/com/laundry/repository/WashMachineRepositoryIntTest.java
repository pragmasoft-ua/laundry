package com.laundry.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.laundry.LaundryApp;
import com.laundry.domain.WashMachine;

/**
 * Test class for the CustomAuditEventRepository customAuditEventRepository class.
 *
 * @see CustomAuditEventRepository
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LaundryApp.class)
@Transactional
public class WashMachineRepositoryIntTest {

	private final Logger log = LoggerFactory.getLogger(WashMachineRepositoryIntTest.class);

    @Autowired
    private WashMachineRepository repository;

    @Test @Ignore
    public void testFindAllAvailableBetween() {
    	ZonedDateTime now = ZonedDateTime.now();
    	ZonedDateTime oneHourLater = now.plus(1, ChronoUnit.HOURS);
    	List<WashMachine> machines = repository.findAllAvailableBetween(now, oneHourLater);
    	log.info("There are following machines available {} between {} and {}", machines, now, oneHourLater);
        assertThat(machines).isNotEmpty();
    }

}

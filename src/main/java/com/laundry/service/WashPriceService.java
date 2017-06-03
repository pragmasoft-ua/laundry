package com.laundry.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundry.domain.WashPrice;
import com.laundry.repository.WashPriceRepository;

/**
 * Service class for managing Current wash price.
 */
@Service
@Transactional
public class WashPriceService {

    private final Logger log = LoggerFactory.getLogger(WashPriceService.class);

    private final WashPriceRepository repository;

    public WashPriceService(WashPriceRepository repository) {
        this.repository = repository;
    }

    public Optional<BigDecimal> getCurrentPrice() {
        log.debug("get current price") ;
        return repository.findOneByEfferctiveToIsNull().map(WashPrice::getPriceKgHour);
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        log.debug("set current price {}", currentPrice) ;
        Optional<WashPrice> old = repository.findOneByEfferctiveToIsNull();
        old.ifPresent( (existing) -> repository.save(existing.expire()));
        WashPrice current = new WashPrice(currentPrice);
        current = repository.save(current);
    }

	public void delete(Long arg0) {
		repository.delete(arg0);
	}

	public List<WashPrice> findAll() {
		return repository.findAll();
	}

	public WashPrice findOne(Long arg0) {
		return repository.findOne(arg0);
	}

	public <S extends WashPrice> S save(S arg0) {
		return repository.save(arg0);
	}

	public <S extends WashPrice> S saveAndFlush(S arg0) {
		return repository.saveAndFlush(arg0);
	}
	
	
    
}

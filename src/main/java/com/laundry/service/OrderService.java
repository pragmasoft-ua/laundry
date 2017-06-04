package com.laundry.service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.laundry.domain.Order;
import com.laundry.domain.User;
import com.laundry.domain.WashMachine;
import com.laundry.domain.WashPrice;
import com.laundry.repository.OrderRepository;
import com.laundry.repository.UserRepository;
import com.laundry.repository.WashMachineRepository;
import com.laundry.repository.WashPriceRepository;
import com.laundry.security.SecurityUtils;

/**
 * Service class for managing Current wash price.
 */
@Service
@Transactional
public class OrderService {

    @SuppressWarnings("unused")
	private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
	private final WashPriceRepository priceRepository;
	private final UserRepository userRepository;
	private final WashMachineRepository washMachineRepository;

    public OrderService(OrderRepository repository, WashPriceRepository priceLookup, UserRepository userRepository, WashMachineRepository washMachineRepository) {
        this.orderRepository = repository;
        this.priceRepository = priceLookup;
        this.userRepository = userRepository;
        this.washMachineRepository = washMachineRepository;
    }

	public void delete(Long arg0) {
		orderRepository.delete(arg0);
	}

	public List<Order> findAll() {
		return orderRepository.findAll();
	}

	public Order findOne(Long arg0) {
		return orderRepository.findOne(arg0);
	}

	public <S extends Order> S save(S arg0) {
		return orderRepository.save(arg0);
	}

	public <S extends Order> S saveAndFlush(S arg0) {
		return orderRepository.saveAndFlush(arg0);
	}

	public List<Order> findAllWithEagerRelationships() {
		return orderRepository.findAllWithEagerRelationships();
	}

	public Order findOneWithEagerRelationships(Long id) {
		return orderRepository.findOneWithEagerRelationships(id);
	}

	public Order create(Order order) {
		final String login = SecurityUtils.getCurrentUserLogin();
		final User user = userRepository.findOneByLogin(login).orElseThrow(() -> new IllegalStateException("User is not found in database by login " + login));
		final WashPrice currentPrice = priceRepository.findOneByEfferctiveToIsNull().orElseThrow(() -> new IllegalStateException("Price is not configured; please ask admin to configure price"));
		final ZonedDateTime from = order.getStartOn();
		final ZonedDateTime to = from.plus(order.getDurationHours(), ChronoUnit.HOURS);
    	List<WashMachine> availableMachines = washMachineRepository.findAllAvailableBetween(from, to);
    	Set<WashMachine> orderMachines = orderMachines(availableMachines, order.getWeightKg());
    	order.setMachines(orderMachines);
		order.setCustomer(user);
		order.setPrice(currentPrice);
		order.updateTotal();
		return orderRepository.save(order);
	}

	public static Set<WashMachine> orderMachines(List<WashMachine> availableMachines, Integer weight) {
		final Set<WashMachine> orderedMachines = new HashSet<>(availableMachines.size());
		for (WashMachine m: availableMachines) {
			weight -= m.getCapacity();
			orderedMachines.add(m);
			if (weight <= 0) break;
		}
		if (weight > 0) throw new IllegalStateException("Not enough total capacity to handle extra " + weight);
		return orderedMachines;
	}
    
}

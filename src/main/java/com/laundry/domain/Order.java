package com.laundry.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Order.
 */
@Entity
@Table(name = "jhi_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "start_on", nullable = false)
    private ZonedDateTime startOn;

    @NotNull
    @Min(value = 0)
    @Max(value = 8)
    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "weight_kg", nullable = false)
    private Integer weightKg;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "total", precision=10, scale=2, nullable = false)
    private BigDecimal total;

    @ManyToOne
    private User customer;

    @ManyToOne
    private WashPrice price;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "order_machine",
               joinColumns = @JoinColumn(name="orders_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="machines_id", referencedColumnName="id"))
    private Set<WashMachine> machines = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getStartOn() {
        return startOn;
    }

    public Order startOn(ZonedDateTime startOn) {
        this.startOn = startOn;
        return this;
    }

    public void setStartOn(ZonedDateTime startOn) {
        this.startOn = startOn;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public Order durationHours(Integer durationHours) {
        this.durationHours = durationHours;
        return this;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }

    public Integer getWeightKg() {
        return weightKg;
    }

    public Order weightKg(Integer weightKg) {
        this.weightKg = weightKg;
        return this;
    }

    public void setWeightKg(Integer weightKg) {
        this.weightKg = weightKg;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Order total(BigDecimal total) {
        this.total = total;
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void updateTotal() {
    	final BigDecimal duration = BigDecimal.valueOf(this.getDurationHours());
    	final BigDecimal weight = BigDecimal.valueOf(this.getWeightKg());
        this.total = this.getPrice().getPriceKgHour().multiply(duration).multiply(weight); 
    }
    
    public User getCustomer() {
        return customer;
    }

    public Order customer(User user) {
        this.customer = user;
        return this;
    }

    public void setCustomer(User user) {
        this.customer = user;
    }

    public WashPrice getPrice() {
        return price;
    }

    public Order price(WashPrice washPrice) {
        this.price = washPrice;
        return this;
    }

    public void setPrice(WashPrice washPrice) {
        this.price = washPrice;
    }

    public Set<WashMachine> getMachines() {
        return machines;
    }

    public Order machines(Set<WashMachine> washMachines) {
        this.machines = washMachines;
        return this;
    }

    public Order addMachine(WashMachine washMachine) {
        this.machines.add(washMachine);
        return this;
    }

    public Order removeMachine(WashMachine washMachine) {
        this.machines.remove(washMachine);
        return this;
    }

    public void setMachines(Set<WashMachine> washMachines) {
        this.machines = washMachines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        if (order.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", startOn='" + getStartOn() + "'" +
            ", durationHours='" + getDurationHours() + "'" +
            ", weightKg='" + getWeightKg() + "'" +
            ", total='" + getTotal() + "'" +
            "}";
    }
}

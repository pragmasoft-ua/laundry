package com.laundry.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A WashPrice.
 */
@Entity
@Table(name = "wash_price")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WashPrice implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @DecimalMin(value = "0")
    @Column(name = "price_kg_hour", precision=10, scale=2, nullable = false)
    private BigDecimal priceKgHour;

    @Column(name = "efferctive_to")
    private ZonedDateTime efferctiveTo;

    public WashPrice() {
	}

    public WashPrice(BigDecimal washPrice) {
    	this.priceKgHour = washPrice;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPriceKgHour() {
        return priceKgHour;
    }

    public WashPrice priceKgHour(BigDecimal priceKgHour) {
        this.priceKgHour = priceKgHour;
        return this;
    }

    public void setPriceKgHour(BigDecimal priceKgHour) {
        this.priceKgHour = priceKgHour;
    }

    public ZonedDateTime getEfferctiveTo() {
        return efferctiveTo;
    }

    public WashPrice efferctiveTo(ZonedDateTime efferctiveTo) {
        this.efferctiveTo = efferctiveTo;
        return this;
    }

    public void setEfferctiveTo(ZonedDateTime efferctiveTo) {
        this.efferctiveTo = efferctiveTo;
    }
    
    public WashPrice expire() {
    	this.setEfferctiveTo(ZonedDateTime.now());
    	return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WashPrice washPrice = (WashPrice) o;
        if (washPrice.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), washPrice.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WashPrice{" +
            "id=" + getId() +
            ", priceKgHour='" + getPriceKgHour() + "'" +
            ", efferctiveTo='" + getEfferctiveTo() + "'" +
            "}";
    }
}

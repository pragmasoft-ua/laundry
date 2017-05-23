package com.laundry.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A WashMachine.
 */
@Entity
@Table(name = "wash_machine")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class WashMachine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "available")
    private Boolean available;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public WashMachine capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Boolean isAvailable() {
        return available;
    }

    public WashMachine available(Boolean available) {
        this.available = available;
        return this;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WashMachine washMachine = (WashMachine) o;
        if (washMachine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), washMachine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WashMachine{" +
            "id=" + getId() +
            ", capacity='" + getCapacity() + "'" +
            ", available='" + isAvailable() + "'" +
            "}";
    }
}

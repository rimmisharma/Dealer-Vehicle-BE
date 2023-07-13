package com.assignment.restapiassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "dealervehicle")
public class DealerVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @ManyToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dealerid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Dealer dealer;
    @NotBlank
    @OneToOne(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "vehicleid", unique = true, nullable = false)
    @JsonIgnore
    private Vehicle vehicle;

    @NotBlank
    @Column(nullable = false)
    @JsonProperty("quantity")
    private Long quantity;


    public DealerVehicle(){

    }


    public DealerVehicle(Long id, Dealer dealer, Vehicle vehicle, Long quantity) {
        this.id = id;
        this.dealer = dealer;
        this.vehicle = vehicle;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public DealerVehicle setId(Long id) {
        this.id = id;
        return this;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public DealerVehicle setDealer(Dealer dealer) {
        this.dealer = dealer;
        return this;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public DealerVehicle setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public Long getQuantity() {
        return quantity;
    }

    public DealerVehicle setQuantity(Long quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "DealerVehicle{" +
                "id=" + id +
                ", dealer=" + dealer +
                ", vehicle=" + vehicle +
                ", quantity=" + quantity +
                '}';
    }
}

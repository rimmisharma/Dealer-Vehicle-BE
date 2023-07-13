package com.assignment.restapiassignment.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "taxes")
public class Taxes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(length = 40, nullable = false)
    private String state;
    @NotBlank
    @Column(length = 50, nullable = false)
    @JsonProperty("name")
    private String name;
    @JsonProperty("amount")
    private Double amount;
    @Column(precision = 4, scale = 2)
    @JsonProperty("percentage")
    private Float percentage;

    public Taxes(){

    }

    public Taxes(String state, String name, Double amount, Float percentage) {
        this.state = state;
        this.name = name;
        this.amount = amount;
        this.percentage = percentage;
    }

    public Long getId() {
        return id;
    }

    public Taxes setId(Long id) {
        this.id = id;
        return this;
    }

    public String getState() {
        return state;
    }

    public Taxes setState(String state) {
        this.state = state;
        return this;
    }

    public String getName() {
        return name;
    }

    public Taxes setName(String name) {
        this.name = name;
        return this;
    }

    public Double getAmount() {
        return amount;
    }

    public Taxes setAmount(Double amount) {
        this.amount = amount;
        return this;
    }

    public Float getPercentage() {
        return percentage;
    }

    public Taxes setPercentage(Float percentage) {
        this.percentage = percentage;
        return this;
    }
}

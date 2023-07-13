package com.assignment.restapiassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(length = 50, nullable = false)
    @JsonProperty("brand")
    private String brand;
    @NotBlank
    @Column(name = "classofvehicle", nullable = false, length = 25)
    @JsonProperty("classofvehicle")
    private String classOfVehicle;
    @NotBlank
    @Column(nullable = false, length = 50)
    @JsonProperty("name")
    private String name;
    @NotBlank
    @Column(name = "modelname", nullable = false, length = 50)
    @JsonProperty("modelname")
    private String modelName;
    @NotBlank
    @Column(nullable = false, length = 10)
    @JsonProperty("color")
    private String color;
    @NotBlank
    @Column(nullable = false, length = 2)
    @JsonProperty("transmission")
    private String transmission;
    @NotBlank
    @Column(nullable = false)
    @JsonProperty("price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dealerid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Dealer dealer;

    public Vehicle(){

    }

    public Vehicle(String brand, String classOfVehicle, String name, String modelName, String color, String transmission, Double price) {
        this.brand = brand;
        this.classOfVehicle = classOfVehicle;
        this.name = name;
        this.modelName = modelName;
        this.color = color;
        this.transmission = transmission;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getClassOfVehicle() {
        return classOfVehicle;
    }

    public void setClassOfVehicle(String classOfVehicle) {
        this.classOfVehicle = classOfVehicle;
    }

    public String getName() {
        return name;
    }

    public Vehicle setName(String name) {
        this.name = name;
        return this;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    @Override
    public String toString() {
        return "Vehicle = [" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", classOfVehicle='" + classOfVehicle + '\'' +
                ", name='" + name + '\'' +
                ", modelName='" + modelName + '\'' +
                ", color='" + color + '\'' +
                ", transmission='" + transmission + '\'' +
                ", price=" + price +
                ']';
    }
}

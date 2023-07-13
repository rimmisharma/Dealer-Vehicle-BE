package com.assignment.restapiassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Entity
@Table(name = "dealerorders")
public class DealerOrders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dealervehicleid", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private DealerVehicle dealerVehicle;

    @NotBlank
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dealerid", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Dealer dealer;
    @NotBlank
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicleid", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Vehicle vehicle;
    @NotBlank
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @NotBlank
    @Column(name = "orderdate", nullable = false)
    @JsonProperty("orderdate")
    @JsonIgnore
    private LocalDate orderDate;
    @NotBlank
    @Column(name = "invoiceno", length = 100, nullable = false, unique = true)
    @JsonProperty("invoiceno")
    @JsonIgnore
    private String invoiceNo;
    @NotBlank
    @Column(name="invoiceamount",nullable = false)
    @JsonProperty("invoiceamount")
    @JsonIgnore
    private Double invoiceAmount;
    @NotBlank
    @Column(name="discountamount", columnDefinition = "default 0",nullable = false)
    @JsonProperty("discountamount")
    private Double discountAmount;
    @NotBlank
    @Column(name="deliverydate", nullable = false)
    @JsonProperty("deliverydate")
    @JsonIgnore
    private LocalDate deliveryDate;


    public DealerOrders(){

    }


    public DealerOrders(Long id, DealerVehicle dealerVehicle, Dealer dealer, Vehicle vehicle,
                        User user, LocalDate orderDate, String invoiceNo, Double invoiceAmount,
                        Double discountAmount, LocalDate deliveryDate) {
        this.id = id;
        this.orderDate = orderDate;
        this.invoiceNo = invoiceNo;
        this.invoiceAmount = invoiceAmount;
        this.discountAmount = discountAmount;
        this.deliveryDate = deliveryDate;
        this.dealerVehicle = dealerVehicle;
        this.dealer = dealer;
        this.vehicle = vehicle;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(Double invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public Double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public DealerVehicle getDealerVehicle() {
        return dealerVehicle;
    }

    public void setDealerVehicle(DealerVehicle dealerVehicle) {
        this.dealerVehicle = dealerVehicle;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @JsonIgnore
    public User getUserDetails() {
        return user;
    }

    public void setUserDetails(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DealerOrders{" +
                "id=" + id +
                ", dealerVehicle=" + dealerVehicle +
                ", dealer=" + dealer +
                ", vehicle=" + vehicle +
                ", user=" + user +
                ", orderDate=" + orderDate +
                ", invoiceNo='" + invoiceNo + '\'' +
                ", invoiceAmount=" + invoiceAmount +
                ", discountAmount=" + discountAmount +
                ", deliveryDate=" + deliveryDate +
                '}';
    }
}

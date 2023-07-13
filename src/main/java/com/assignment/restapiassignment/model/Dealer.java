package com.assignment.restapiassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "dealers")
public class Dealer{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(length = 50, nullable = false)
    @JsonProperty("name")
    private String name;
    @NotBlank
    @Column(name = "addressline1", length = 50, nullable = false)
    @JsonProperty("addressline1")
    private String addressLine1;
    @Column(name = "addressline2", length = 50)
    @JsonProperty("addressline2")
    private String addressLine2;
    @Column(name = "addressline3", length = 50)
    @JsonProperty("addressline3")
    private String addressLine3;
    @NotBlank
    @Column(length = 50, nullable = false)
    @JsonProperty("city")
    private String city;

    @NotBlank
    @Column(length = 50, nullable = false)
    @JsonProperty("state")
    private String state;

    @Column(columnDefinition = "varchar(5) default 'India'", nullable = false)
    @NotBlank
    @JsonProperty("country")
    private String country;
    @NotBlank
    @Column(name = "pincode", length = 6, nullable = false)
    @Pattern(regexp = "^[1-9][0-9]{5}$")
    @JsonProperty("pincode")
    private Integer pinCode;
    @NotBlank
    @Column(name = "mobileno", length = 10, nullable = false, unique = true)
    @JsonProperty("mobileno")
    private Long mobileNo;

    @NotBlank
    @Pattern(regexp = "^[\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @Column(name = "emailid", length = 100, unique = true)
    @JsonProperty("emailid")
    private String emailId;

    public Dealer(){

    }

    public Dealer(String name, String addressLine1, String addressLine2, String addressLine3,
                  String city, String state, String country, Integer pinCode, Long mobileNo, String emailId) {
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JsonIgnore
    public List<String> getStates() {
        return Arrays.asList(
                "ANDHRA PRADESH",
                "ARUNACHAL PRADESH",
                "ASSAM",
                "BIHAR",
                "CHHATTISGARH",
                "GOA",
                "GUJARAT",
                "HARYANA",
                "HIMACHAL PRADESH",
                "JHARKHAND",
                "KARNATAKA",
                "KERALA",
                "MADHYA PRADESH",
                "MAHARASHTRA",
                "MANIPUR",
                "MEGHALAYA",
                "MIZORAM",
                "NAGALAND",
                "ODISHA",
                "PUNJAB",
                "RAJASTHAN",
                "SIKKIM",
                "TAMIL NADU",
                "TELANGANA",
                "TRIPURA",
                "UTTARAKHAND",
                "UTTAR PRADESH",
                "WEST BENGAL",
                "CHANDIGARH",
                "LADAKH",
                "JAMMU & KASHMIR",
                "PUDUCHERRY",
                "LAKSHADWEEP",
                "DELHI",
                "ANDAMAN AND NICOBAR ISLANDS",
                "DADRA AND NAGAR HAVELI AND DAMAN AND DIU"
        );
    }



    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    public Long getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(Long mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCountry() {
        return country;
    }

    public String getEmailId() {
        return emailId;
    }

    public Dealer setEmailId(String emailId) {
        this.emailId = emailId;
        return this;
    }

    public String getState() {
        return state;
    }

    public Dealer setState(String state) {
        this.state = state;
        return this;
    }

    public Dealer setCountry(String country) {
        this.country = country;
        return this;
    }

    @Override
    public String toString() {
        return "Dealer = [" +
                "name='" + name + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", addressLine3='" + addressLine3 + '\'' +
                ", city='" + city + '\'' +
                ", pinCode=" + pinCode +
                ", mobileNo=" + mobileNo +
                ']';
    }
}

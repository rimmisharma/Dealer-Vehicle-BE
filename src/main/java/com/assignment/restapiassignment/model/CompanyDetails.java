package com.assignment.restapiassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "companydetails")
public class CompanyDetails {

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
    @NotBlank
    @Column(name = "addressline2", length = 50, nullable = false)
    @JsonProperty("addressline2")
    private String addressLine2;
    @NotBlank
    @Column(name = "addressline3", length = 50, nullable = false)
    @JsonProperty("addressline3")
    private String addressLine3;
    @NotBlank
    @Column(length = 15, nullable = false)
    @JsonProperty("city")
    private String city;
    @NotBlank
    @Column(length = 15, nullable = false)
    @JsonProperty("state")
    private String state;
    @NotBlank
    @Column(columnDefinition = "varchar(5) default 'India'", nullable = false)
    @JsonProperty("country")
    private String country;
    @NotBlank
    @Column(name = "pincode", length = 6, nullable = false )
    @JsonProperty("pincode")
    private Integer pinCode;
    @NotBlank
    @Column(name = "contactno", length = 10, nullable = false, unique = true)
    @JsonProperty("contactno")
    private Long contactNo;
    @Column(name = "natureofbusiness", length = 50)
    @JsonProperty("natureofbusiness")
    private String natureOfBusiness;
    @Column(name = "ageofbusiness")
    @JsonProperty("ageofbusiness")
    private Integer ageOfBusiness;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    public CompanyDetails(){

    }

    public CompanyDetails(Long id, String name, String addressLine1, String addressLine2,
                          String addressLine3, String city, String state, String country, Integer pinCode,
                          Long contactNo, String natureOfBusiness, Integer ageOfBusiness, User user) {
        this.id = id;
        this.name = name;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.contactNo = contactNo;
        this.natureOfBusiness = natureOfBusiness;
        this.ageOfBusiness = ageOfBusiness;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public CompanyDetails setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CompanyDetails setName(String name) {
        this.name = name;
        return this;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPinCode() {
        return pinCode;
    }

    public void setPinCode(Integer pinCode) {
        this.pinCode = pinCode;
    }

    public Long getContactNo() {
        return contactNo;
    }

    public void setContactNo(Long contactNo) {
        this.contactNo = contactNo;
    }

    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public Integer getAgeOfBusiness() {
        return ageOfBusiness;
    }

    public void setAgeOfBusiness(Integer ageOfBusiness) {
        this.ageOfBusiness = ageOfBusiness;
    }

    @JsonIgnore
    public User getUserDetails() {
        return user;
    }

    public CompanyDetails setUserDetails(User user) {
        this.user = user;
        return this;
    }
}


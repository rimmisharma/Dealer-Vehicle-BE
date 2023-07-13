package com.assignment.restapiassignment.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "userdetails")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;
    @NotBlank
    @Column(name = "firstname", length = 25, nullable = false)
    @JsonProperty("firstname")
    private String firstName;
    @Column(name = "middlename", length = 25)
    @JsonProperty("middlename")
    private String middleName;
    @Column(name = "lastname", length = 25, nullable = false)
    @NotBlank
    @JsonProperty("lastname")
    private String lastName;
    @Column(name = "addressline1", length = 50, nullable = false)
    @NotBlank
    @JsonProperty("addressline1")
    private String addressLine1;
    @Column(name = "addressline2", length = 50)
    @JsonProperty("addressline2")
    private String addressLine2;
    @Column(name = "addressline3", length = 50)
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
    @Column(name = "pincode", length = 6, nullable = false)
    @JsonProperty("pincode")
    private Integer pinCode;
    @NotBlank
    @Column(name = "mobileno",length = 10, nullable = false, unique = true)
    @JsonProperty("mobileno")
    private Long mobileNo;
    @NotBlank
    @Column(name = "emailid", length = 100, nullable = false, unique = true)
    @Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    @JsonProperty("emailid")
    private String emailId;

    @Column(name = "password", length = 100, nullable = false)
    @JsonProperty("password")
    private String password;

    @NotBlank
    @Column(name = "aadharno", length = 12, nullable = false, unique = true)
    @JsonProperty("aadharno")
    private Long aadharNo;
    @NotBlank
    @Column(name = "panno", length = 10, nullable = false)
    @JsonProperty("panno")
    private String panNo;
    @NotBlank
    @Column(name = "isselfbusiness", length = 8, nullable = false, unique = true)
    @JsonProperty("isselfbusiness")
    private boolean isSelfBusiness;
    @NotBlank
    @Column(name = "yearlyincome", nullable = false)
    @JsonProperty("yearlyincome")
    private Long yearlyIncome;


    public User(){

    }

    public User(Long id, String firstName, String middleName, String lastName,
                String addressLine1, String addressLine2, String addressLine3,
                String city, String state, String country, Integer pinCode,
                Long mobileNo, String emailId, String password, Long aadharNo, String panNo, boolean isSelfBusiness, Long yearlyIncome) {
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pinCode = pinCode;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.password = password;
        this.aadharNo = aadharNo;
        this.panNo = panNo;
        this.isSelfBusiness = isSelfBusiness;
        this.yearlyIncome = yearlyIncome;
    }

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPassword() {
        return password;
    }

    public User setPassword(String password) {
        this.password = password;
        return this;
    }

    public Long getAadharNo() {
        return aadharNo;
    }

    public void setAadharNo(Long aadharNo) {
        this.aadharNo = aadharNo;
    }

    public String getPanNo() {
        return panNo;
    }

    public void setPanNo(String panNo) {
        this.panNo = panNo;
    }

    public boolean isSelfBusiness() {
        return isSelfBusiness;
    }

    public void setSelfBusiness(boolean selfBusiness) {
        isSelfBusiness = selfBusiness;
    }

    public Long getYearlyIncome() {
        return yearlyIncome;
    }

    public void setYearlyIncome(Long yearlyIncome) {
        this.yearlyIncome = yearlyIncome;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", addressLine1='" + addressLine1 + '\'' +
                ", addressLine2='" + addressLine2 + '\'' +
                ", addressLine3='" + addressLine3 + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pinCode=" + pinCode +
                ", mobileNo=" + mobileNo +
                ", emailId='" + emailId + '\'' +
                ", aadharNo=" + aadharNo +
                ", panNo='" + panNo + '\'' +
                ", isSelfBusiness=" + isSelfBusiness +
                ", yearlyIncome=" + yearlyIncome +
                '}';
    }

    
}

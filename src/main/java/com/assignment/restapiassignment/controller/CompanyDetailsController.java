package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.model.CompanyDetails;
import com.assignment.restapiassignment.model.User;
import com.assignment.restapiassignment.service.CompanyDetailsService;
import com.assignment.restapiassignment.service.StateService;
import com.assignment.restapiassignment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/company")
public class CompanyDetailsController {
    @Autowired
    private CompanyDetailsService companyDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private StateService stateService;
    private static final Logger logger = LoggerFactory.getLogger(CompanyDetailsController.class);


    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getCompanyByUserId(@RequestParam String userid) {
        logger.info(userid);
        if(ObjectUtils.isEmpty(userid)){
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if(!userid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }

        CompanyDetails companyDetails = companyDetailsService.getCompanyByUserId(Long.valueOf(userid));
        if(ObjectUtils.isEmpty(companyDetails)){
            logger.error("Company was not found for the ID " + userid);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyDetails);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getCompanyById(@PathVariable String id){
        // Handling the special characters in the id
        if(id.matches("^[1-9][0-9]*$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long companyId = Long.valueOf(id);
        if(companyId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        CompanyDetails companyDetails = companyDetailsService.getCompanyById(companyId);
        if(ObjectUtils.isEmpty(companyDetails)){
            logger.error("Company was not found.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(companyDetailsService.getCompanyById(companyId));
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCompanyDetails(){
        List<CompanyDetails> list = companyDetailsService.getAllCompanyDetails();
        if( ObjectUtils.isEmpty(list)){
            logger.error("Companies were not found.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/create")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.POST, allowedHeaders = {"Authorization", "Content-Type"})
    public ResponseEntity<?> createCompanyDetails(@RequestParam String userid, @RequestBody CompanyDetails companyDetails){
        String hello;
        if(ObjectUtils.isEmpty(userid)){
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if(!userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }
        if (ObjectUtils.isEmpty(companyDetails)) {
            return ResponseEntity.badRequest().body("A null company object pas passed.");
        }
        if (ObjectUtils.isEmpty(companyDetails.getName()) || companyDetails.getName().length() > 50) {
            return ResponseEntity.badRequest().body("Name cannot be empty/null, it should only contain alphabets with 50 characters limit.");
        }else if (ObjectUtils.isEmpty(companyDetails.getAddressLine1()) || companyDetails.getAddressLine1().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine1 is null/empty or its length exceeds 50 characters.");
        }else if (!ObjectUtils.isEmpty(companyDetails.getAddressLine2()) && companyDetails.getAddressLine2().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine2 length exceeds 50 characters.");
        }else if (!ObjectUtils.isEmpty(companyDetails.getAddressLine3()) && companyDetails.getAddressLine3().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine3 length exceeds 50 characters.");
        }else if (ObjectUtils.isEmpty(companyDetails.getCountry()) || !companyDetails.getCountry().equalsIgnoreCase("India")) {
            return ResponseEntity.badRequest().body("Country is either null/empty or is not India.");
        }else if (ObjectUtils.isEmpty(companyDetails.getPinCode()) || !companyDetails.getPinCode().toString().matches("^[1-9][0-9]{5}$") ) {
            return ResponseEntity.badRequest().body("Pincode is null/empty or its length is not of 6 digits.");
        }
        if (!ObjectUtils.isEmpty(companyDetails.getCity())) {
            if (stateService.isValidState(companyDetails.getCity().toUpperCase())) {
                if (!Arrays.asList("CHANDIGARH", "DELHI", "DADRA AND NAGAR HAVELI AND DAMAN AND DIU",
                        "LADAKH", "JAMMU AND KASHMIR", "JAMMU AND KASHMIR", "PUDUCHERRY",
                        "LAKSHADWEEP", "ANDAMAN AND NICOBAR ISLANDS").contains(companyDetails.getCity().toUpperCase())) {
                    return ResponseEntity.badRequest().body(companyDetails.getCity() + " is a state. Please enter a valid Indian city.");
                }
            } else if (!companyDetails.getCity().matches("^[A-Za-z]{1,15}$")) {
                return ResponseEntity.badRequest().body("City should only contain alphabets with a 15 characters limit.");
            }
        }else{
            return ResponseEntity.badRequest().body("City should not be empty/null. Please enter a city!");
        }
        if (!ObjectUtils.isEmpty(companyDetails.getContactNo())) {
            if (!companyDetails.getContactNo().toString().matches("^[1-9][0-9]{9}$")) {
                return ResponseEntity.badRequest().body("Please enter a valid 10 digit mobile number!");
            }
        } else {
            logger.error("Contact number can't be null.");
            return ResponseEntity.badRequest().body("Contact number can't be null. Please enter a valid 10 digits contact number!");
        }
        User user = userService.getUserDetails(Long.valueOf(userid));
        if(userService.isUserAvailable(Long.valueOf(userid))){
            if(!ObjectUtils.isEmpty(user.isSelfBusiness()) && user.isSelfBusiness()) {
                if (ObjectUtils.isEmpty(companyDetails.getAgeOfBusiness()) || ObjectUtils.isEmpty(companyDetails.getNatureOfBusiness())) {
                    return ResponseEntity.badRequest().body("Business details are mandatory for this user.");
                }
            }else if (!ObjectUtils.isEmpty(user.isSelfBusiness()) && !user.isSelfBusiness()) {
                if(!ObjectUtils.isEmpty(companyDetails.getAgeOfBusiness()) || !ObjectUtils.isEmpty(companyDetails.getNatureOfBusiness())) {
                    return ResponseEntity.badRequest().body("Business details should be empty for " + user.getFirstName() + "!");

                }
            }else {
                    if (!ObjectUtils.isEmpty(companyDetails.getAgeOfBusiness()) && !companyDetails.getAgeOfBusiness().toString().matches("^[1-9][0-9]*$")) {
                        return ResponseEntity.badRequest().body("Age of business is not in numeric digits.");
                    } else if (!ObjectUtils.isEmpty(companyDetails.getNatureOfBusiness()) && !companyDetails.getNatureOfBusiness().matches("^[A-Za-z]{1,50}$")) {
                        return ResponseEntity.badRequest().body("Nature of business cannot be more than 50 characters.");
                    }
                }
            }
        try {
            CompanyDetails savedCompany = companyDetailsService.createCompanyDetails(Long.valueOf(userid), companyDetails);
            if (ObjectUtils.isEmpty(savedCompany)) {
                return ResponseEntity.internalServerError().body("An internal server error occurred. Please try Again!");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCompanyDetails(@RequestParam String userid,@RequestBody CompanyDetails companyDetails){
        if(ObjectUtils.isEmpty(userid)){
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if(!userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }
        CompanyDetails companyDetailsFetched = companyDetailsService.getCompanyByUserId(Long.valueOf(userid));
        if(ObjectUtils.isEmpty(companyDetailsFetched)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No company found for given ID");
        }
        if (ObjectUtils.isEmpty(companyDetails)) {
            return ResponseEntity.badRequest().body("A null company object pas passed.");
        }
        if (ObjectUtils.isEmpty(companyDetails.getName()) || companyDetails.getName().length() > 50) {
            return ResponseEntity.badRequest().body("Name is null/empty or its length exceeds 50 characters.");
        }else if (ObjectUtils.isEmpty(companyDetails.getAddressLine1()) || companyDetails.getAddressLine1().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine1 is null/empty or its length exceeds 50 characters.");
        }else if (!ObjectUtils.isEmpty(companyDetails.getAddressLine2()) && companyDetails.getAddressLine2().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine2 is null/empty or its length exceeds 50 characters.");
        }else if (!ObjectUtils.isEmpty(companyDetails.getAddressLine3()) && companyDetails.getAddressLine3().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine3 is null/empty or its length exceeds 50 characters.");
        }else if (ObjectUtils.isEmpty(companyDetails.getCity()) || companyDetails.getCity().length() > 15) {
            return ResponseEntity.badRequest().body("City is null/empty or its length exceeds 15 characters.");
        }else if (ObjectUtils.isEmpty(companyDetails.getState()) || companyDetails.getStates().stream().noneMatch(state -> companyDetails.getState().equalsIgnoreCase(state))) {
            return ResponseEntity.badRequest().body("State is null/empty or it is not a valid Indian state.");
        }else if (ObjectUtils.isEmpty(companyDetails.getCountry()) || companyDetails.getCountry().equalsIgnoreCase("India")) {
            return ResponseEntity.badRequest().body("State is null/empty or it is not a valid Indian state.");
        }else if (ObjectUtils.isEmpty(companyDetails.getPinCode()) || companyDetails.getPinCode().toString().length() > 6) {
            return ResponseEntity.badRequest().body("Pincode is null/empty or its length is not of 6 digits.");
        }else if (ObjectUtils.isEmpty(companyDetails.getContactNo()) || companyDetails.getContactNo().toString().matches("^[0-9]{10}$")) {
            return ResponseEntity.badRequest().body("Mobile number is null/empty or its length is not of 10 digits.");
        }else if(!ObjectUtils.isEmpty(companyDetails.getAgeOfBusiness()) && companyDetails.getAgeOfBusiness().toString().matches("^[1-9][0-9]*$")){
            return ResponseEntity.badRequest().body("Age of business is not in numeric digits.");
        }else if(!ObjectUtils.isEmpty(companyDetails.getNatureOfBusiness()) && companyDetails.getNatureOfBusiness().length() > 50 ){
            return ResponseEntity.badRequest().body("Nature of business cannot be more than 50 characters.");
        }

        CompanyDetails updatedCompanyDetails = companyDetailsService.updateCompanyByPost(Long.valueOf(userid), companyDetails);
        if (!ObjectUtils.isEmpty(updatedCompanyDetails)) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedCompanyDetails);
        } else {
            return ResponseEntity.internalServerError().body("Unexpected error occurred at the server, please try again!");
        }
    }
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateCompanyDetailsByPatch(@RequestBody CompanyDetails companyDetails, @PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long companyId = Long.valueOf(id);
        if(companyId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        CompanyDetails companyDetails1 = companyDetailsService.getCompanyById(companyId);
        if(ObjectUtils.isEmpty(companyDetails1)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(companyDetailsService.updateCompanyDetails(companyDetails, companyId));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCompanyDetailsById(@RequestParam String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long companyId = Long.valueOf(id);
        if(companyId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        CompanyDetails companyDetails1 = companyDetailsService.getCompanyById(companyId);
        if(ObjectUtils.isEmpty(companyDetails1)){
            return ResponseEntity.noContent().build();
        }
        companyDetailsService.deleteCompanyDetails(companyId);
        return ResponseEntity.accepted().build();
    }

}

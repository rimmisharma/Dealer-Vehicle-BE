package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.model.Dealer;
import com.assignment.restapiassignment.service.DealerService;
import com.assignment.restapiassignment.service.StateService;
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
@RequestMapping("/api/dealer")
public class DealerController {
    private static final Logger logger = LoggerFactory.getLogger(DealerController.class);
    @Autowired
    private DealerService dealerService;

    @Autowired
    private StateService stateService;

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getAllDealers() {
        List<Dealer> allDealersList = dealerService.getAllDealers();
        if (allDealersList.isEmpty()) {
            logger.error("No Dealers were found in the database.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No dealers available.");
        } else {
            return ResponseEntity.ok(allDealersList);
        }
    }

    @GetMapping("/query")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getDealerByPincodeOrCity(@RequestParam(required = false) String pincode, @RequestParam(required = false) String city) {
        logger.info("Received request_001 for query param " + pincode);
        if (ObjectUtils.isEmpty(pincode) && ObjectUtils.isEmpty(city)) {
            return ResponseEntity.badRequest().body("All search parameters are empty! Please enter a pincode or city name!");
        } else if (!(ObjectUtils.isEmpty(pincode)) && !(ObjectUtils.isEmpty(city))) {
            return ResponseEntity.badRequest().body("Received value for both the parameters. Please send either pincode or city name!");
        } else if (!(ObjectUtils.isEmpty(city))) {
            if(city.matches("^[A-Za-z]{1,15}$")) {
                List<Dealer> listByCity = dealerService.getDealerByCity(city);
                if (listByCity.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No dealerships were found for " + city + "!");
                }
                return ResponseEntity.ok(listByCity);
            }
        } else if (!(ObjectUtils.isEmpty(pincode))) {
            if (pincode.matches("^[1-9][0-9]{5}$")) {
                List<Dealer> listByPincode = dealerService.getDealerByPincode(Integer.valueOf(pincode));
                if (listByPincode.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No dealerships found for pincode " + pincode + "!");
                }
                return ResponseEntity.ok(listByPincode);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter a valid Indian pincode of 6 digits.");
            }

        }
        return ResponseEntity.badRequest().body("All search parameters are empty! Please send either pincode or city!");
    }
    @GetMapping("/{dealerid}")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getDealerById(@PathVariable String dealerid){
        if (ObjectUtils.isEmpty(dealerid)) {
            return ResponseEntity.badRequest().body("dealer ID is null");
        }
        if(!dealerid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID should only be positive Integer!");
        }
        Dealer dealer = dealerService.getDealerById(Long.parseLong(dealerid));
        if(ObjectUtils.isEmpty(dealer)){
            logger.error("Dealer was not found for the dealerid: " + dealerid);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dealer was not found for dealerid: " + dealerid);
        }
        return ResponseEntity.ok(dealer);
    }
    @PostMapping("/create")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.POST, allowedHeaders = "Authorization")
    public ResponseEntity<?> createDealer(@RequestBody Dealer dealerDetails){
        if (ObjectUtils.isEmpty(dealerDetails)) {
            return ResponseEntity.badRequest().body("A null company object pas passed.");
        }
        if (ObjectUtils.isEmpty(dealerDetails.getName()) || dealerDetails.getName().matches("^[A-Za-z]{1,50}$")) {
            return ResponseEntity.badRequest().body("First name cannot be empty/null, it should only contain alphabets with 50 characters limit.");
        }else if (ObjectUtils.isEmpty(dealerDetails.getAddressLine1()) || dealerDetails.getAddressLine1().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine1 is null/empty or its length exceeds 50 characters.");
        }else if (!ObjectUtils.isEmpty(dealerDetails.getAddressLine2()) && dealerDetails.getAddressLine2().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine2 length exceeds 50 characters.");
        }else if (!ObjectUtils.isEmpty(dealerDetails.getAddressLine3()) && dealerDetails.getAddressLine3().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine3 length exceeds 50 characters.");
        }else if (ObjectUtils.isEmpty(dealerDetails.getCountry()) || !dealerDetails.getCountry().equalsIgnoreCase("India")) {
            return ResponseEntity.badRequest().body("Country is either null/empty or is not India.");
        }else if (ObjectUtils.isEmpty(dealerDetails.getPinCode()) || !dealerDetails.getPinCode().toString().matches("^[1-9][0-9]{5}$") ) {
            return ResponseEntity.badRequest().body("Pincode is null/empty or its length is not of 6 digits.");
        }
        if (!ObjectUtils.isEmpty(dealerDetails.getCity())) {
            if (stateService.isValidState(dealerDetails.getCity().toUpperCase())) {
                if (!Arrays.asList("CHANDIGARH", "DELHI", "DADRA AND NAGAR HAVELI AND DAMAN AND DIU",
                        "LADAKH", "JAMMU AND KASHMIR", "JAMMU AND KASHMIR", "PUDUCHERRY",
                        "LAKSHADWEEP", "ANDAMAN AND NICOBAR ISLANDS").contains(dealerDetails.getCity().toUpperCase())) {
                    return ResponseEntity.badRequest().body(dealerDetails.getCity() + " is a state. Please enter a valid Indian city.");
                }
            } else if (!dealerDetails.getCity().matches("^[A-Za-z]{1,15}$")) {
                return ResponseEntity.badRequest().body("City should only contain alphabets with a 15 characters limit.");
            }
        }else{
            return ResponseEntity.badRequest().body("City should not be empty/null. Please enter a city!");
        }
        if(!ObjectUtils.isEmpty(dealerDetails.getMobileNo())) {
            if(!dealerDetails.getMobileNo().toString().matches("^[1-9][0-9]{9}$")){
                return ResponseEntity.badRequest().body("Mobile number is invalid! Please enter a valid 10 digits mobile number.");
            }else if (dealerService.mobileNumberFound(dealerDetails.getMobileNo())) {
                return ResponseEntity.badRequest().body("The user with this mobile number already exists.");
            }
        } else {
            logger.error("Mobile number can't be null.");
            return ResponseEntity.badRequest().body("Mobile number can't be null. Please enter a valid 10 digits mobile number!");
        }
        if(!ObjectUtils.isEmpty(dealerDetails.getEmailId()) ){
            if (!dealerDetails.getEmailId().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.badRequest().body("Email format is not correct. Please enter a valid email-id. For eg. abc@example.com");
            }else if(dealerService.emailFound(dealerDetails.getEmailId())) {
                return ResponseEntity.badRequest().body("The user with this email-id already exists.");
            }
        } else {
            logger.error("Email can't be null.");
            return ResponseEntity.badRequest().body("Email can't be null. Please enter an Email ID!");
        }
        Dealer savedDealer = dealerService.createDealer(dealerDetails);
        if (ObjectUtils.isEmpty(savedDealer)) {
            return ResponseEntity.internalServerError().body("An internal server error occurred. Please try Again!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDealer);
    }

    @PutMapping("/update")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.PUT, allowedHeaders = "Authorization")
    public ResponseEntity<?> updateDealer(@RequestParam String id, @RequestBody Dealer dealer){
        if(!id.matches("^[1-9][0-9]*$")){
            return ResponseEntity.badRequest().body("ID should only be positive Integer.");
        }
        if (dealer == null) {
            return ResponseEntity.badRequest().body("Null dealer object was passed.");
        } else if(ObjectUtils.isEmpty(dealer.getName()) || dealer.getName().length() > 50){
            return ResponseEntity.badRequest().body("Name cannot be empty/null or more than 50 characters.");
        }else if(ObjectUtils.isEmpty(dealer.getAddressLine1()) || dealer.getAddressLine1().length() > 50) {
            ResponseEntity.badRequest().body("Address line 1 cannot be empty/null or more than 50 characters.");
        } else if (!ObjectUtils.isEmpty(dealer.getAddressLine2()) && dealer.getAddressLine2().length() > 50) {
            ResponseEntity.badRequest().body("Address line 2 cannot have more than 50 characters.");
        } else if (!ObjectUtils.isEmpty(dealer.getAddressLine3()) && dealer.getAddressLine3().length() > 50) {
            ResponseEntity.badRequest().body("Address line 3 cannot have more than 50 characters.");
        } else if (ObjectUtils.isEmpty(dealer.getCity()) || dealer.getCity().length() > 50) {
            return ResponseEntity.badRequest().body("City cannot be empty/null or more than 50 characters.");
        } else if(ObjectUtils.isEmpty(dealer.getState()) || dealer.getState().length() > 50){
            return ResponseEntity.badRequest().body("State cannot be empty/null or more than 50 characters.");
        } else if(ObjectUtils.isEmpty(dealer.getPinCode()) || !dealer.getPinCode().toString().matches("^[1-9][0-9]{5}$")){
            return ResponseEntity.badRequest().body("Pincode is empty/null or invalid. Please enter a valid pincode of 6 digits only.");
        } else if(ObjectUtils.isEmpty(dealer.getState()) || dealer.getStates().stream().noneMatch(state -> dealer.getState().equalsIgnoreCase(state))){
            return ResponseEntity.badRequest().body("You have entered an empty/null or invalid Indian state.");
        }
        if(!ObjectUtils.isEmpty(dealer.getEmailId()) ){
            if (!dealer.getEmailId().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.badRequest().body("Email format is not correct. Please enter a valid email-id. For eg. abc@example.com");
            }else if(dealerService.emailFound(dealer.getEmailId())) {
                return ResponseEntity.badRequest().body("The user with this email-id already exists.");
            }
        } else {
            logger.error("Email can't be null.");
            return ResponseEntity.badRequest().body("Email can't be null. Please enter an Email ID!");
        }
        if(!ObjectUtils.isEmpty(dealer.getMobileNo())) {
            if(!dealer.getMobileNo().toString().matches("^[1-9][0-9]{9}$")){
                return ResponseEntity.badRequest().body("Mobile number is invalid! Please enter a valid 10 digits mobile number.");
            }else if (dealerService.mobileNumberFound(dealer.getMobileNo())) {
                return ResponseEntity.badRequest().body("The user with this mobile number already exists.");
            }
        } else {
            logger.error("Mobile number can't be null.");
            return ResponseEntity.badRequest().body("Mobile number can't be null. Please enter a valid 10 digits mobile number!");
        }
        if(!ObjectUtils.isEmpty(dealer.getCountry()) || !dealer.getCountry().equalsIgnoreCase("India")){
            return ResponseEntity.badRequest().body("Only allowed value for country is India.");
        }
        Long dealerId = Long.valueOf(id);
        Dealer dealerFetched = dealerService.getDealerById(dealerId);
        if(ObjectUtils.isEmpty(dealerFetched)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No dealer found for given ID.");
        }
        return ResponseEntity.ok(dealerService.updateDealer(dealer,dealerId));
    }

    @PostMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.POST, allowedHeaders = "Authorization")
    public ResponseEntity<?> updateDealers(@RequestBody Dealer dealer, @PathVariable String id){
        if(!id.matches("^[0-9]*$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should only contain numbers.");
        }
        Long dealerId = Long.valueOf(id);
        if(dealerId <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id cannot be zero or negative! Please enter a valid id.");
        } else if(ObjectUtils.isEmpty(dealerService.getDealerById(dealerId))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such dealer exists with this id: "+dealerId);
        }
        return ResponseEntity.ok(dealerService.updateDealer(dealer,dealerId));
    }

    @PatchMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.PATCH, allowedHeaders = "Authorization")
    public ResponseEntity<?> updateDealersByPatch(@RequestBody Dealer dealer, @PathVariable String id){
        if(!id.matches("^[0-9]*$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should only contain numbers.");
        }
        Long dealerId = Long.valueOf(id);
        if(dealerId <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id cannot be zero or negative! Please enter a valid id.");
        } else if(ObjectUtils.isEmpty(dealerService.getDealerById(dealerId))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such dealer exists with this id: "+dealerId);
        }
        return ResponseEntity.ok(dealerService.updateDealer(dealer,dealerId));
    }
    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.DELETE, allowedHeaders = "Authorization")
    public ResponseEntity<?> deleteDealer(@PathVariable String id){
        if(!id.matches("^[0-9]*$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should only contain numbers.");
        }
        Long dealerId = Long.valueOf(id);
        if(dealerId <= 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id cannot be zero or negative! Please enter a valid id.");
        } else if(ObjectUtils.isEmpty(dealerService.getDealerById(dealerId))){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such dealer exists with this id: "+dealerId);
        }
        dealerService.deleteDealerById(dealerId);
        return ResponseEntity.accepted().build();
    }

}

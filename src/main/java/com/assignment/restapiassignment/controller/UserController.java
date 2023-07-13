package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.model.User;
import com.assignment.restapiassignment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllUserDetails() {
        List<User> allDealersList = userService.getAllUserDetails();
        if (allDealersList.isEmpty()) {
            logger.debug("No users were found in the database.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No users were found.");
        }
        return ResponseEntity.ok(allDealersList);
    }

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getUserDetailsById(@RequestParam String userid) {
        if (ObjectUtils.isEmpty(userid)) {
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if (!userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }
        User user = userService.getUserDetails(Long.valueOf(userid));
        if (ObjectUtils.isEmpty(user)) {
            logger.error("User was not found for the ID " + userid);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{userid}")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getUserById(@PathVariable String userid) {
        if (!userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }
        User user = userService.getUserDetails(Long.valueOf(userid));
        if (ObjectUtils.isEmpty(user)) {
            logger.error("User was not found for the ID " + userid);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/getByEmail")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        if (!ObjectUtils.isEmpty(email)){
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.badRequest().body("Email format is not correct. Please enter a valid email-companyID. For eg. abc@example.com");
            } else if (!userService.emailFound(email)) {
                return ResponseEntity.badRequest().body("The user with this emailID not found!");
            }
        } else {
            logger.error("Email can't be null.");
            return ResponseEntity.badRequest().body("Email can't be null. Please enter an Email ID!");
        }
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping("/isEligible")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> isUserEligible(@RequestParam String email, @RequestParam Double price){
        User user = userService.getUserByEmail(email);
        if (!ObjectUtils.isEmpty(email)){
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.badRequest().body("Email format is not correct. Please enter a valid email-companyID. For eg. abc@example.com");
            } else if (!userService.emailFound(email)) {
                return ResponseEntity.badRequest().body("The user with this emailID not found!");
            }
        } else {
            logger.error("Email can't be null.");
            return ResponseEntity.badRequest().body("Email can't be null. Please enter an Email ID!");
        }
        if(!ObjectUtils.isEmpty(price)){
            if(price.toString().matches("\\d([\\d\\s]*\\d)?")){
                return ResponseEntity.badRequest().body("Not a valid input for price. It should be in digits only.");
            }
        }else{
            logger.error("Price can't be null");
            return ResponseEntity.badRequest().body("Price can't be null. Please enter a price for a vehicle!");
        }
        boolean isEligible = userService.isEligible(email, price);
        return ResponseEntity.ok(isEligible);

    }

    @PostMapping("/register")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.POST, allowedHeaders = {"Authorization", "Content-Type"})
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (ObjectUtils.isEmpty(user)) {
            return ResponseEntity.badRequest().body("A null user object pas passed.");
        }
        logger.info("userobject  \n" + user);
        if (ObjectUtils.isEmpty(user.getFirstName()) || user.getFirstName().length() > 25) {
            return ResponseEntity.badRequest().body("First name is null/empty or its length exceeds 25 characters.");
        } else if (!ObjectUtils.isEmpty(user.getMiddleName()) && user.getMiddleName().length() > 25) {
            return ResponseEntity.badRequest().body("Middle name length exceeds 25 characters");
        } else if (ObjectUtils.isEmpty(user.getLastName()) || user.getLastName().length() > 25) {
            return ResponseEntity.badRequest().body("Last name is null/empty or its length exceeds 25 characters.");
        } else if (ObjectUtils.isEmpty(user.getAddressLine1()) || user.getAddressLine1().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine1 is null/empty or its length exceeds 50 characters.");
        } else if (!ObjectUtils.isEmpty(user.getAddressLine2()) && user.getAddressLine2().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine2 length exceeds 50 characters.");
        } else if (!ObjectUtils.isEmpty(user.getAddressLine3()) && user.getAddressLine3().length() > 50) {
            return ResponseEntity.badRequest().body("AddressLine3 length exceeds 50 characters.");
        } else if (ObjectUtils.isEmpty(user.getCity()) || user.getCity().length() > 15) {
            return ResponseEntity.badRequest().body("City is null/empty or its length exceeds 15 characters.");
        } else if (ObjectUtils.isEmpty(user.getState()) || user.getStates().stream().noneMatch(state -> user.getState().equalsIgnoreCase(state))) {
            return ResponseEntity.badRequest().body("State is null/empty or it is not a valid Indian state.");
        } else if (ObjectUtils.isEmpty(user.getCountry()) || !user.getCountry().equalsIgnoreCase("India")) {
            return ResponseEntity.badRequest().body("Country is either null/empty or is not India.");
        } else if (ObjectUtils.isEmpty(user.getPinCode()) || !user.getPinCode().toString().matches("^[1-9][0-9]{5}$") ) {
            return ResponseEntity.badRequest().body("Pincode is null/empty or its length is not of 6 digits.");
        } else if (ObjectUtils.isEmpty(user.getYearlyIncome()) || !user.getYearlyIncome().toString().matches("\\d([\\d\\s]*\\d)?")) {
            return ResponseEntity.badRequest().body("Yearly income is empty/null or not in proper format.");
        } else if(ObjectUtils.isEmpty(user.getPassword()) || user.getPassword().length() > 100) {
            return ResponseEntity.badRequest().body("Password cant be null.");
        }
        if (!ObjectUtils.isEmpty(user.getMobileNo())) {
            if (user.getMobileNo().toString().length() > 10) {
                return ResponseEntity.badRequest().body("Please enter a valid 10 digit mobile number!");
            } else if (userService.mobileNumberFound(user.getMobileNo())) {
                return ResponseEntity.badRequest().body("The user with this Mobile number already exists.");
            }
        } else {
            logger.error("Mobile number can't be null.");
            return ResponseEntity.badRequest().body("Mobile number can't be null. Please enter a valid 10 digit mobile number!");
        }
        if (!ObjectUtils.isEmpty(user.getEmailId())) {
            if (!user.getEmailId().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.badRequest().body("Email format is not correct. Please enter a valid email-companyID. For eg. abc@example.com");
            } else if (userService.emailFound(user.getEmailId())) {
                return ResponseEntity.badRequest().body("The user with this emailID already exists.");
            }
        } else {
            logger.error("Email can't be null.");
            return ResponseEntity.badRequest().body("Email can't be null. Please enter an Email ID!");
        }
        if (!ObjectUtils.isEmpty(user.getAadharNo())) {
            if (!user.getAadharNo().toString().matches("^[0-9]{12}$")) {
                return ResponseEntity.badRequest().body("Aadhar number is invalid! Please enter a valid 12 digits aadhar number.");
            } else if (userService.aadharNumberFound(user.getAadharNo())) {
                return ResponseEntity.badRequest().body("The user with this aadhar number already exists.");
            }
        } else {
            logger.error("Aadhar number can't be null.");
            return ResponseEntity.badRequest().body("Aadhar number can't be null. Please enter a valid Aadhar number!");
        }

        if (!ObjectUtils.isEmpty(user.getPanNo())) {
            if (!user.getPanNo().matches("^[a-zA-Z0-9]+$")) {
                return ResponseEntity.badRequest().body("Pan number is invalid! Please enter a valid 10 digits pan number.");
            } else if (userService.panNumberFound(user.getPanNo())) {
                return ResponseEntity.badRequest().body("The user with this pan number already exists.");
            }
        } else {
            logger.error("Pan number can't be null.");
            return ResponseEntity.badRequest().body("Pan number can't be null. Please enter a valid pan number!");
        }
        User savedUser = userService.createUserDetails(user);
        if (ObjectUtils.isEmpty(savedUser)) {
            return ResponseEntity.internalServerError().body("An internal server error occurred. Please try Again!");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

//    @PostMapping("/login")
//    public ResponseEntity<?> authenticateUser(@RequestBody User user) {
//        if (!ObjectUtils.isEmpty(user.getEmailId())) {
//            if (!user.getEmailId().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
//                return ResponseEntity.badRequest().body("Email format is not correct. Please enter a valid email-companyID. For eg. abc@example.com");
//            } else if (userService.emailFound(user.getEmailId())) {
//                return ResponseEntity.badRequest().body("The user with this emailID already exists.");
//            }
//        } else {
//            logger.error("Email can't be null.");
//            return ResponseEntity.badRequest().body("Email can't be null. Please enter an Email ID!");
//        }
//        if(ObjectUtils.isEmpty(user.getPassword()) || user.getPassword().length() > 100) {
//            return ResponseEntity.badRequest().body("Password cant be null.");
//        }
//        boolean isLoginSuccessful = userService.authenticateUser(user);
//        return ResponseEntity.ok(isLoginSuccessful);
//    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUserDetails(@RequestParam String userid, @RequestBody User user) {
        if (ObjectUtils.isEmpty(userid)) {
            logger.error("Null query param userID was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if (!userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID should only be positive Integer.");
        }
        User userFetched = userService.getUserDetails(Long.valueOf(userid));
        if (ObjectUtils.isEmpty(userFetched)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found for given ID");
        }
        if (ObjectUtils.isEmpty(user)) {
            return ResponseEntity.badRequest().body("A null user object pas passed.");
        }
        if (ObjectUtils.isEmpty(user.getFirstName()) || user.getFirstName().length() > 25) {
            return ResponseEntity.badRequest().body("First name is null/empty or its length exceeds 25 characters.");
        } else if (!ObjectUtils.isEmpty(user.getMiddleName()) && user.getMiddleName().length() > 25) {
            return ResponseEntity.badRequest().body("Middle name length exceeds 25 characters");
        } else if (ObjectUtils.isEmpty(user.getLastName()) || user.getLastName().length() > 25) {
            return ResponseEntity.badRequest().body("Last name is null/empty or its length exceeds 25 characters.");
        } else if (ObjectUtils.isEmpty(user.getAddressLine1()) || user.getAddressLine1().length() > 50) {
            return ResponseEntity.badRequest().body("Address is null/empty or its length exceeds 50 characters.");
        } else if (!ObjectUtils.isEmpty(user.getAddressLine2()) && user.getAddressLine2().length() > 50) {
            return ResponseEntity.badRequest().body("Address is null/empty or its length exceeds 50 characters.");
        } else if (!ObjectUtils.isEmpty(user.getAddressLine3()) && user.getAddressLine3().length() > 50) {
            return ResponseEntity.badRequest().body("Address is null/empty or its length exceeds 50 characters.");
        } else if (ObjectUtils.isEmpty(user.getCity()) || user.getCity().length() > 15) {
            return ResponseEntity.badRequest().body("City is null/empty or its length exceeds 15 characters.");
        } else if (ObjectUtils.isEmpty(user.getState()) || user.getStates().stream().noneMatch(state -> user.getState().equalsIgnoreCase(state))) {
            return ResponseEntity.badRequest().body("State is null/empty or it is not a valid Indian state.");
        } else if (ObjectUtils.isEmpty(user.getCountry()) || !user.getCountry().equalsIgnoreCase("India")) {
            return ResponseEntity.badRequest().body("Country is either null/empty or is not India.");
        } else if (ObjectUtils.isEmpty(user.getPinCode()) || !user.getPinCode().toString().matches("^[1-9][0-9]{5}$")) {
            return ResponseEntity.badRequest().body("Pincode is null/empty or its length is not of 6 digits.");
        } else if (ObjectUtils.isEmpty(user.getMobileNo()) || user.getMobileNo().toString().length() > 10) {
            return ResponseEntity.badRequest().body("Mobile number is null/empty or its length is not of 10 digits.");
        } else if (ObjectUtils.isEmpty(user.getYearlyIncome()) || !user.getYearlyIncome().toString().matches("\\d([\\d\\s]*\\d)?")) {
            return ResponseEntity.badRequest().body("Yearly income is empty/null or not in proper format.");
        }
        if (!ObjectUtils.isEmpty(user.getMobileNo())) {
            if (user.getMobileNo().toString().length() > 10) {
                return ResponseEntity.badRequest().body("Please enter a valid 10 digit mobile number!");
            } else if (!user.getMobileNo().equals(userFetched.getMobileNo())) {
                if ((userService.mobileNumberFound(user.getMobileNo()))) {
                    return ResponseEntity.badRequest().body("The user with this Mobile number already exists.");
                }
            }
        } else {
            logger.error("Mobile number can't be null.");
            return ResponseEntity.badRequest().body("Mobile number can't be null. Please enter a valid 10 digit mobile number!");
        }
        if (!ObjectUtils.isEmpty(user.getEmailId())) {
            if (!user.getEmailId().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                return ResponseEntity.badRequest().body("Email format is not correct. Please enter a valid email-companyID. For eg. abc@example.com");
            } else if (!user.getEmailId().equalsIgnoreCase(userFetched.getEmailId())) {
                if (userService.emailFound(user.getEmailId())) {
                    return ResponseEntity.badRequest().body("The user with this emailID already exists.");
                }
            }
        } else {
            logger.error("Email can't be null.");
            return ResponseEntity.badRequest().body("Email can't be null. Please enter an Email ID!");
        }
        if (!ObjectUtils.isEmpty(user.getAadharNo())) {
            if (!user.getAadharNo().toString().matches("^[0-9]{12}$")) {
                return ResponseEntity.badRequest().body("Aadhar number is invalid! Please enter a valid 12 digits aadhar number.");
            } else if (!user.getAadharNo().equals(userFetched.getAadharNo())) {
                if (userService.aadharNumberFound(user.getAadharNo())) {
                    return ResponseEntity.badRequest().body("The user with this aadhar number already exists.");
                }
            }
        } else {
            logger.error("Aadhar number can't be null.");
            return ResponseEntity.badRequest().body("Aadhar number can't be null. Please enter a valid Aadhar number!");
        }

        if (!ObjectUtils.isEmpty(user.getPanNo())) {
            if (!user.getPanNo().matches("^[a-zA-Z0-9]+$")) {
                return ResponseEntity.badRequest().body("Pan number is invalid! Please enter a valid 10 digits pan number.");
            } else if (!user.getPanNo().equals(userFetched.getPanNo())) {
                if (userService.panNumberFound(user.getPanNo())) {
                    return ResponseEntity.badRequest().body("The user with this pan number already exists.");
                }
            }
        } else {
            logger.error("Pan number can't be null.");
            return ResponseEntity.badRequest().body("Pan number can't be null. Please enter a valid pan number!");
        }
        User updatedUser = userService.updateUserByPost(Long.valueOf(userid), user);
        if (!ObjectUtils.isEmpty(updatedUser)) {
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } else {
            return ResponseEntity.internalServerError().body("Unexpected error occurred at the server, please try again!");
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateUserDetailsByPatch(@RequestParam String userid, @RequestBody User user) {
        if (ObjectUtils.isEmpty(userid)) {
            logger.error("Null query param userID was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if (!userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID should only be positive Integer.");
        }
        if (ObjectUtils.isEmpty(user)) {
            return ResponseEntity.badRequest().body("A null user object pas passed.");
        }
        User userFetched = userService.getUserDetails(Long.valueOf(userid));
        if (ObjectUtils.isEmpty(userFetched)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found for given ID");
        }
        try {
            User updatedUser = userService.updateUserByPatch(Long.valueOf(userid), user);
            if (!ObjectUtils.isEmpty(updatedUser)) {
                return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
            } else {
                return ResponseEntity.internalServerError().body("Unexpected error occurred at the server, please try again!");
            }
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping("/delete")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.DELETE, allowedHeaders = {"Authorization", "Content-Type"})
    public ResponseEntity<?> deleteUserDetails(@RequestParam String userid) {
        if (ObjectUtils.isEmpty(userid)) {
            logger.error("Null query param userID was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if (!userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID should only be positive Integer.");
        }
        User userFetched = userService.getUserDetails(Long.valueOf(userid));
        if (ObjectUtils.isEmpty(userFetched)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user found for given ID");
        }
        userService.deleteUserDetails(Long.valueOf(userid));
        User userDeleted = userService.getUserDetails(Long.valueOf(userid));
        if (!ObjectUtils.isEmpty(userDeleted)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("User was not deleted due to an internal server error.");
        }
        return ResponseEntity.accepted().body("User successfully deleted!");
    }
}

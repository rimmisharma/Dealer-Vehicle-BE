package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.model.User;
import com.assignment.restapiassignment.repository.CompanyDetailsRepository;
import com.assignment.restapiassignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;



    public List<User> getAllUserDetails() {
        List<User> listOfDealersFetched = userRepository.findAll();
        if (listOfDealersFetched.isEmpty()) {
            return null;
        } else {
            return listOfDealersFetched;
        }
    }
    public User getUserDetails(Long userID) {
        Optional<User> userDetails = userRepository.findById(userID);
        return userDetails.orElse(null);
    }

    public boolean isUserAvailable (Long userID) {
        Optional<User> userDetails = userRepository.findById(userID);
        return userDetails.isPresent();
    }

    public User createUserDetails(User user) {
        User userSaved = userRepository.save(user);
        if (ObjectUtils.isEmpty(userSaved)) {
            return null;
        } else {
            return userSaved;
        }
    }

//    public boolean authenticateUser(User user) {
//        User userFound = userRepository.findByEmailId(user.getEmailId());
//        if (userFound != null) {
//            return passwordEncoder.matches(user.getPassword(), userFound.getPassword());
//        }
//        return false;
//    }

    public User updateUserByPost(Long userID, User user) {
        Optional<User> fetchedUserDetails = userRepository.findById(userID);
        if (fetchedUserDetails.isPresent()) {
            fetchedUserDetails.get().setFirstName(user.getFirstName());
            fetchedUserDetails.get().setMiddleName(user.getMiddleName());
            fetchedUserDetails.get().setLastName(user.getLastName());
            fetchedUserDetails.get().setAddressLine1(user.getAddressLine1());
            fetchedUserDetails.get().setAddressLine2(user.getAddressLine2());
            fetchedUserDetails.get().setAddressLine3(user.getAddressLine3());
            fetchedUserDetails.get().setCity(user.getCity());
            fetchedUserDetails.get().setState(user.getState());
            fetchedUserDetails.get().setCountry(user.getCountry());
            fetchedUserDetails.get().setPinCode(user.getPinCode());
            fetchedUserDetails.get().setMobileNo(user.getMobileNo());
            fetchedUserDetails.get().setEmailId(user.getEmailId());
            fetchedUserDetails.get().setAadharNo(user.getAadharNo());
            fetchedUserDetails.get().setPanNo(user.getPanNo());
            fetchedUserDetails.get().setSelfBusiness(user.isSelfBusiness());
            fetchedUserDetails.get().setYearlyIncome(user.getYearlyIncome());
            User updatedUser = userRepository.save(fetchedUserDetails.get());
            if(ObjectUtils.isEmpty(updatedUser)) {
                return null;
            } else {
                return updatedUser;
            }
        } else {
            return null;
        }
    }

    public User updateUserByPatch(Long userID, User user) throws BadRequestException {
        Optional<User> fetchedUserDetails = userRepository.findById(userID);
        if (fetchedUserDetails.isPresent()) {
            if (!ObjectUtils.isEmpty(user.getFirstName())) {
                if (user.getFirstName().length() > 25) {
                    throw new BadRequestException("First name length should not exceed 25 characters.");
                } else {
                    fetchedUserDetails.get().setFirstName(user.getFirstName());
                }
            }
            if (!ObjectUtils.isEmpty(user.getMiddleName())) {
                if (user.getMiddleName().length() > 25) {
                    throw new BadRequestException("Middle name length should not exceed 25 characters.");
                } else {
                    fetchedUserDetails.get().setMiddleName(user.getMiddleName());
                }
            }
            if (!ObjectUtils.isEmpty(user.getLastName())) {
                if (user.getLastName().length() > 25) {
                    throw new BadRequestException("Last name length should not exceed 25 characters.");
                } else {
                    fetchedUserDetails.get().setLastName(user.getLastName());
                }
            }
            if (!ObjectUtils.isEmpty(user.getAddressLine1())) {
                if (user.getAddressLine1().length() > 50) {
                    throw new BadRequestException("Address line length should not exceed 50 characters.");
                } else {
                    fetchedUserDetails.get().setAddressLine1(user.getAddressLine1());
                }
            }
            if (!ObjectUtils.isEmpty(user.getAddressLine2())) {
                if (user.getAddressLine2().length() > 50) {
                    throw new BadRequestException("Address line length should not exceed 50 characters.");
                } else {
                    fetchedUserDetails.get().setAddressLine2(user.getAddressLine2());
                }
            }
            if (!ObjectUtils.isEmpty(user.getAddressLine3())) {
                if (user.getAddressLine3().length() > 50) {
                    throw new BadRequestException("Address line length should not exceed 50 characters.");
                } else {
                    fetchedUserDetails.get().setAddressLine3(user.getAddressLine3());
                }
            }
            if (!ObjectUtils.isEmpty(user.getCity())) {
                if (user.getCity().length() > 15) {
                    throw new BadRequestException("City length should not exceed 15 characters.");
                } else {
                    fetchedUserDetails.get().setCity(user.getCity());
                }
            }
            if (!ObjectUtils.isEmpty(user.getState())) {
                if (user.getStates().stream().noneMatch(state -> user.getState().equalsIgnoreCase(state))) {
                    throw new BadRequestException("State input is not a valid Indian state.");
                } else {
                    fetchedUserDetails.get().setState(user.getState());
                }
            }
            if (!ObjectUtils.isEmpty(user.getPinCode())) {
                if (!user.getPinCode().toString().matches("^[1-9][0-9]{5}$")) {
                    throw new BadRequestException("Please enter a valid 6 digits pincode!");
                } else {
                    fetchedUserDetails.get().setPinCode(user.getPinCode());
                }
            }
            if (!ObjectUtils.isEmpty(user.getMobileNo())) {
                if (user.getMobileNo().toString().matches("[1-9][0-9]{9}$")) {
                    throw new BadRequestException("Please enter a valid 10 digits mobile number!");
                } else {
                    fetchedUserDetails.get().setMobileNo(user.getMobileNo());
                }
            }
            if (!ObjectUtils.isEmpty(user.getEmailId())) {
                if (user.getEmailId().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    throw new BadRequestException("Email format is not correct. Please enter a valid email-companyID. For eg. abc@example.com");
                } else {
                    fetchedUserDetails.get().setMobileNo(user.getMobileNo());
                }
            }
            if (!ObjectUtils.isEmpty(user.getAadharNo())) {
                if (user.getAadharNo().toString().matches("^[0-9]{12}$")) {
                    throw new BadRequestException("Please enter a valid 12 digits aadhar number!");
                } else {
                    fetchedUserDetails.get().setAadharNo(user.getAadharNo());
                }
            }
            if (!ObjectUtils.isEmpty(user.getPanNo())) {
                if (user.getPanNo().matches("^[a-zA-Z0-9]+$")) {
                    throw new BadRequestException("Pan number is invalid! Please enter a valid 10 alpha-numeric pan number.");
                } else {
                    fetchedUserDetails.get().setAadharNo(user.getAadharNo());
                }
            }
            if (!ObjectUtils.isEmpty(user.getYearlyIncome())) {
                if (user.getYearlyIncome().toString().matches("\\d([\\d\\s]*\\d)?")) {
                    throw new BadRequestException("Yearly income is not in proper format.");
                } else {
                    fetchedUserDetails.get().setYearlyIncome(user.getYearlyIncome());
                }
            }
            User updatedUser = userRepository.save(fetchedUserDetails.get());
            if (ObjectUtils.isEmpty(updatedUser)) {
                return null;
            } else {
                return updatedUser;
            }
        } else {
            return null;
        }
    }
    public void deleteUserDetails(Long userId){
        userRepository.deleteById(userId);
    }

    public boolean emailFound(String email){
        User user = userRepository.findByEmailId(email);
        return user != null;
    }

    public boolean mobileNumberFound(Long mobileNo){
        User user = userRepository.findByMobileNo(mobileNo);
        return user != null;
    }

    public boolean aadharNumberFound(Long aadharNo){
        User user = userRepository.findByAadharNo(aadharNo);
        return user != null;
    }

    public boolean panNumberFound(String panNo){
        User user = userRepository.findByPanNo(panNo);
        return user != null;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmailId(email);
    }

    public boolean isEligible(String email, double price) {
        User user = userRepository.findByEmailId(email);
        if (!ObjectUtils.isEmpty(user)) {
            if ((user.getYearlyIncome() < 50000) || user.getYearlyIncome() * 10 < price) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

}

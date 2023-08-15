package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.model.CompanyDetails;
import com.assignment.restapiassignment.model.User;
import com.assignment.restapiassignment.repository.CompanyDetailsRepository;
import com.assignment.restapiassignment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyDetailsService {
    @Autowired
    private CompanyDetailsRepository companyDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    public CompanyDetails getCompanyById(Long companyId) {
        Optional<CompanyDetails> companyDetails = companyDetailsRepository.findById(companyId);
        return companyDetails.orElse(null);
    }

    public List<CompanyDetails> getAllCompanyDetails() {
        return companyDetailsRepository.findAll();
    }

    public CompanyDetails createCompanyDetails(Long userId, CompanyDetails companyDetails) throws BadRequestException{
        Optional<User> userDetails = userRepository.findById(userId);
        if(userDetails.isPresent()){
            companyDetails.setUserDetails(userDetails.get());
        } else {
            throw new BadRequestException("Wrong user ID passed. User does not exist for id " + userId);
        }
        return companyDetailsRepository.save(companyDetails);
    }

    public CompanyDetails updateCompanyDetails(CompanyDetails companyDetails, Long id){
        CompanyDetails companyDetails1 = companyDetailsRepository.findById(id).get();
        if(!ObjectUtils.isEmpty(companyDetails.getName())){
            companyDetails1.setName(companyDetails.getName());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getAddressLine1())){
            companyDetails1.setAddressLine1(companyDetails.getAddressLine1());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getAddressLine2())){
            companyDetails1.setAddressLine2(companyDetails.getAddressLine2());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getAddressLine3())){
            companyDetails1.setAddressLine3(companyDetails.getAddressLine3());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getCity())){
            companyDetails1.setCity(companyDetails.getCity());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getState())){
            companyDetails1.setState(companyDetails.getState());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getPinCode())){
            companyDetails1.setPinCode(companyDetails.getPinCode());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getContactNo())){
            companyDetails1.setContactNo(companyDetails.getContactNo());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getNatureOfBusiness())){
            companyDetails1.setNatureOfBusiness(companyDetails.getNatureOfBusiness());
        }
        if(!ObjectUtils.isEmpty(companyDetails.getAgeOfBusiness())){
            companyDetails1.setAgeOfBusiness(companyDetails.getAgeOfBusiness());
        }
        return companyDetailsRepository.save(companyDetails1);
    }

    public void deleteCompanyDetails(Long companyId){
        companyDetailsRepository.deleteById(companyId);
    }

    public CompanyDetails getCompanyByUserId(Long userId) {
        Optional <CompanyDetails> userCompanyDetails = companyDetailsRepository.findByUserId(userId);
        return userCompanyDetails.orElse(null);
    }

    public CompanyDetails updateCompanyByPost(Long userID, CompanyDetails companyDetails) {
        Optional<CompanyDetails> fetchedCompanyDetails = companyDetailsRepository.findByUserId(userID);
        if (fetchedCompanyDetails.isPresent()) {
            fetchedCompanyDetails.get().setName(companyDetails.getName());
            fetchedCompanyDetails.get().setAddressLine1(companyDetails.getAddressLine1());
            fetchedCompanyDetails.get().setAddressLine2(companyDetails.getAddressLine2());
            fetchedCompanyDetails.get().setAddressLine3(companyDetails.getAddressLine3());
            fetchedCompanyDetails.get().setCity(companyDetails.getCity());
            fetchedCompanyDetails.get().setState(companyDetails.getState());
            fetchedCompanyDetails.get().setCountry(companyDetails.getCountry());
            fetchedCompanyDetails.get().setPinCode(companyDetails.getPinCode());
            fetchedCompanyDetails.get().setContactNo(companyDetails.getContactNo());
            fetchedCompanyDetails.get().setAgeOfBusiness(companyDetails.getAgeOfBusiness());
            fetchedCompanyDetails.get().setNatureOfBusiness(companyDetails.getNatureOfBusiness());
            CompanyDetails updatedCompany = companyDetailsRepository.save(fetchedCompanyDetails.get());
            if(ObjectUtils.isEmpty(updatedCompany)) {
                return null;
            } else {
                return updatedCompany;
            }
        } else {
            return null;
        }
    }

}

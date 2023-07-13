package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.model.Dealer;
import com.assignment.restapiassignment.repository.DealerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class DealerService {

    @Autowired
    private DealerRepository dealerRepository;

    public Dealer getDealerById(Long dealerId) {
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        return dealer.orElse(null);
    }

    public boolean isDealerAvailable (Long dealerID) {
        Optional<Dealer> dealer = dealerRepository.findById(dealerID);
        return dealer.isPresent();
    }

    public List<Dealer> getAllDealers() {
        return dealerRepository.findAll();
    }

    public Dealer createDealer(Dealer dealer) {
        Dealer dealerSaved = dealerRepository.save(dealer);
        if (ObjectUtils.isEmpty(dealerSaved)) {
            return null;
        } else {
            return dealerSaved;
        }
    }

    public Dealer updateDealer(Dealer dealer, Long dealerId) {
        Dealer dealer1 = dealerRepository.findById(dealerId).get();
            dealer1.setName(dealer.getName());
            dealer1.setAddressLine1(dealer.getAddressLine1());
            dealer1.setAddressLine2(dealer.getAddressLine2());
            dealer1.setAddressLine3(dealer.getAddressLine3());
            dealer1.setCity(dealer.getCity());
        dealer1.setState(dealer.getState());
            dealer1.setPinCode(dealer.getPinCode());
            dealer1.setMobileNo(dealer.getMobileNo());
            dealer1.setEmailId(dealer.getEmailId());
        return dealerRepository.save(dealer1);
    }

    public void deleteDealerById(Long id){
        dealerRepository.deleteById(id);
    }

    public List<Dealer> getDealerByCity(String city) {
        Optional<List<Dealer>> dealersListByCity = dealerRepository.findDealersByCity(city);
        return dealersListByCity.orElse(null);

    }

    public List<Dealer> getDealerByPincode(Integer pinCode) {
        Optional<List<Dealer>> dealerListByPincode = dealerRepository.findDealersByPinCode(pinCode);
        return dealerListByPincode.orElse(null);
    }

    public boolean emailFound(String email){
        Dealer dealer = dealerRepository.findByEmailId(email);
        return dealer != null;
    }

    public boolean mobileNumberFound(Long mobileNumber){
        Dealer dealer = dealerRepository.findByMobileNo(mobileNumber);
        return dealer != null;
    }
}

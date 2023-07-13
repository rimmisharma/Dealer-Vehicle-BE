package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.model.*;
import com.assignment.restapiassignment.repository.DealerRepository;
import com.assignment.restapiassignment.repository.DealerVehicleRepository;
import com.assignment.restapiassignment.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DealerVehicleService {
    @Autowired
    private DealerVehicleRepository dealerVehicleRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    public DealerVehicle getDealerVehicleById(Long dealerVehicleId) {
        Optional<DealerVehicle> dealerVehicle = dealerVehicleRepository.findById(dealerVehicleId);
        return dealerVehicle.orElse(null);
    }

    public boolean isDealerVehicleAvailable (Long dealerVehicleID) {
        Optional<DealerVehicle> dealerVehicle = dealerVehicleRepository.findById(dealerVehicleID);
        return dealerVehicle.isPresent();
    }



    public List<DealerVehicle> getAllDealerVehicles(){
        return dealerVehicleRepository.findAll();
    }

    public DealerVehicle createDealerVehicle(Long id1, Long id2, DealerVehicle dealerVehicle){
        Optional<Dealer> dealer = dealerRepository.findById(id1);
        Optional<Vehicle> vehicle = vehicleRepository.findById(id2);
        dealer.ifPresent(dealerVehicle::setDealer);
        vehicle.ifPresent(dealerVehicle::setVehicle);
        return dealerVehicleRepository.save(dealerVehicle);
    }

    public DealerVehicle updateDealerVehicle(Long id, DealerVehicle dealerVehicle){
        DealerVehicle dealerVehicle1 = dealerVehicleRepository.findById(id).get();
            if (!ObjectUtils.isEmpty(dealerVehicle.getQuantity())) {
                dealerVehicle1.setQuantity(dealerVehicle.getQuantity());
            }

        return dealerVehicleRepository.save(dealerVehicle1);
    }

    public void deleteDealerVehicle(Long id){
        dealerVehicleRepository.deleteById(id);
    }

    public DealerVehicle getDealerVehicleByVehicleId(Long vehicleID) {
        Optional<DealerVehicle> dealerVehicleFound = dealerVehicleRepository.findByVehicleId(vehicleID);
        return dealerVehicleFound.orElse(null);
    }

    public Map<String,Long> getInventoryOfVehiclesByDealerId(Long dealerId){
        List<Vehicle> vehicles = vehicleRepository.findByDealerId(dealerId);
        Map<String, Long> inventoryOfVehicle = new LinkedHashMap<>();
        for(Vehicle v : vehicles){
            inventoryOfVehicle.put(v.getName(), dealerVehicleRepository.findByVehicleId(v.getId()).get().getQuantity());
        }
        return inventoryOfVehicle;
    }

}

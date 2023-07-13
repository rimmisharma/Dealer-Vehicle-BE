package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.exceptions.InternalServerErrorException;
import com.assignment.restapiassignment.model.Dealer;
import com.assignment.restapiassignment.model.DealerVehicle;
import com.assignment.restapiassignment.model.Vehicle;
import com.assignment.restapiassignment.repository.DealerRepository;
import com.assignment.restapiassignment.repository.DealerVehicleRepository;
import com.assignment.restapiassignment.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private DealerVehicleRepository dealerVehicleRepository;

    public Vehicle getVehicleById(Long vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        return vehicle.orElse(null);
    }

    public boolean isVehicleAvailable (Long vehicleID) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleID);
        return vehicle.isPresent();
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle createOrUpdateVehicle(Long dealerid, Vehicle vehicle) throws BadRequestException, InternalServerErrorException {
        Optional<Dealer> dealerPresent = dealerRepository.findById(dealerid);
        if (dealerPresent.isPresent()) {
            Optional<Vehicle> vehicleFound = vehicleRepository.findByDealerIdAndBrandIgnoreCaseAndClassOfVehicleIgnoreCaseAndNameIgnoreCaseAndModelNameIgnoreCaseAndColorIgnoreCaseAndTransmissionIgnoreCase(dealerid,vehicle.getBrand(),
                    vehicle.getClassOfVehicle(), vehicle.getName(), vehicle.getModelName(), vehicle.getColor(), vehicle.getTransmission());
            if (vehicleFound.isPresent()) {
                Optional<DealerVehicle> dealerVehicleFound = dealerVehicleRepository.findByVehicleId(vehicleFound.get().getId());
                if (dealerVehicleFound.isPresent()) {
                    dealerVehicleFound.get().setQuantity(dealerVehicleFound.get().getQuantity() + 1L);
                    System.out.println(dealerVehicleFound.get().toString());
                    DealerVehicle updatedDealerVehicle = dealerVehicleRepository.save(dealerVehicleFound.get());
                    return vehicleFound.get();
                } else {
                    throw new InternalServerErrorException("No dealer vehicle found for vehicle ID " + vehicleFound.get().getId());
                }
            } else {
                vehicle.setDealer(dealerPresent.get());
                Vehicle newVehicle = vehicleRepository.save(vehicle);
                DealerVehicle newDealerVehicle = new DealerVehicle();
                newDealerVehicle.setVehicle(newVehicle);
                newDealerVehicle.setDealer(dealerPresent.get());
                newDealerVehicle.setQuantity(1L);
                dealerVehicleRepository.save(newDealerVehicle);
                return newVehicle;
            }
        } else {
            throw new BadRequestException("No dealer found for dealer ID " + dealerid);
        }
    }

    public Vehicle updateVehicle(Vehicle vehicle, Long id) {
        Vehicle vehicle1 = vehicleRepository.findById(id).get();
        if (!ObjectUtils.isEmpty(vehicle.getName())) {
            vehicle1.setName(vehicle.getName());
        }
        if (!ObjectUtils.isEmpty(vehicle.getBrand())) {
            vehicle1.setBrand(vehicle.getBrand());
        }
        if (!ObjectUtils.isEmpty(vehicle.getClassOfVehicle())) {
            vehicle1.setClassOfVehicle(vehicle.getClassOfVehicle());
        }
        if (!ObjectUtils.isEmpty(vehicle.getName())) {
            vehicle1.setName(vehicle.getName());
        }
        if (!ObjectUtils.isEmpty(vehicle.getModelName())) {
            vehicle1.setModelName(vehicle.getModelName());
        }
        if (!ObjectUtils.isEmpty(vehicle.getColor())) {
            vehicle1.setColor(vehicle.getColor());
        }
        if (!ObjectUtils.isEmpty(vehicle.getTransmission())) {
            vehicle1.setTransmission(vehicle.getTransmission());
        }
        if (!ObjectUtils.isEmpty(vehicle.getPrice())) {
            vehicle1.setPrice(vehicle.getPrice());
        }
        return vehicleRepository.save(vehicle1);
    }

    public void deleteVehicleById(Long id) {
        vehicleRepository.deleteById(id);
    }

    public List<String> getVehicleBrandsByDealer(Long dealerId) {
        List<String> brandsList = new ArrayList<>();
        List<Vehicle> vehicleList = vehicleRepository.findByDealerId(dealerId);
        for (Vehicle aVehicle: vehicleList) {
            aVehicle.setBrand(aVehicle.getBrand().toUpperCase());
            if(!brandsList.contains(aVehicle.getBrand())) {
                brandsList.add(aVehicle.getBrand());
            }
        }
        return brandsList;
    }

    public List<Vehicle> getVehicleModelsByDealerAndBrand(Long dealerId, String brand) {
        return vehicleRepository.findByDealerIdAndBrandIgnoreCase(dealerId, brand);
        //return vehicleRepository.findByDealerIdAndBrand(dealerId, brand).stream().map(Vehicle::getModelName).distinct().collect(Collectors.toList());
    }

    public Vehicle getVehicleByDealerIdAndBrandAndModel(Long dealerId, String brand, String modelName) {
        return vehicleRepository.findByDealerIdAndBrandIgnoreCaseAndModelNameIgnoreCase(dealerId, brand, modelName);
    }

    public List<Vehicle> findByDealerId(Long dealerId) {
        return vehicleRepository.findByDealerId(dealerId);
    }

    public Vehicle getModelDetailsByVehicleId(Long vehicleid) {
        return vehicleRepository.findById(vehicleid).get();
    }
}

package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByDealerId(Long dealerId);

    List<Vehicle> findByDealerIdAndBrandIgnoreCase(Long dealerId, String brand);

    Vehicle findByDealerIdAndBrandIgnoreCaseAndModelNameIgnoreCase(Long dealerId, String brand, String modelName);

    Optional<Vehicle> findByDealerIdAndBrandIgnoreCaseAndClassOfVehicleIgnoreCaseAndNameIgnoreCaseAndModelNameIgnoreCaseAndColorIgnoreCaseAndTransmissionIgnoreCase(Long dealerid, String brand, String classOfVehicle, String name, String modelName, String color, String transmission);
}

package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.Dealer;
import com.assignment.restapiassignment.model.DealerVehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealerVehicleRepository extends JpaRepository<DealerVehicle, Long> {
    Optional<DealerVehicle> findByVehicleId(Long vehicleId);
    List<DealerVehicle> findByDealer(Dealer dealer);
}

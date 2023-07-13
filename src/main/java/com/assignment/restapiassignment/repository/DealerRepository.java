package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DealerRepository extends JpaRepository<Dealer, Long> {
    Optional<List<Dealer>> findDealersByCity(String city);
    Optional<List<Dealer>> findDealersByPinCode(Integer pinCode);

    Dealer findByEmailId(String email);
    Dealer findByMobileNo(Long mobileNo);
}

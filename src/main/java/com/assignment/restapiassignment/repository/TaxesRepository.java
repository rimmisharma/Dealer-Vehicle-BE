package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.Taxes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaxesRepository extends JpaRepository<Taxes, Long> {
    Optional<List<Taxes>> findTaxByState(String state);
}

package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyDetailsRepository extends JpaRepository<CompanyDetails, Long> {
    Optional<CompanyDetails> findByName(String name);
    Optional<CompanyDetails> findByUserId(Long userId);
}

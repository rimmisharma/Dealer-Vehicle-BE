package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmailId(String email);
    User findByMobileNo(Long mobileNo);

    User findByAadharNo(Long aadharNo);
    User findByPanNo(String panNo);
}

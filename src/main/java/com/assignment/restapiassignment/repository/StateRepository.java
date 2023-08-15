package com.assignment.restapiassignment.repository;

import com.assignment.restapiassignment.model.states.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StateRepository extends JpaRepository<State, Long> {

    Optional<State> findStatesByName(String name);
}

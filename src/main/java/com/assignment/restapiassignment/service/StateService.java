package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.model.states.State;
import com.assignment.restapiassignment.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EnumSet;
import java.util.Optional;

@Service
public class StateService {

    @Autowired
    private StateRepository statesRepository;

    public State findById(Long id){
        Optional<State> state = statesRepository.findById(id);
        return state.orElse(null);
    }
    public State findByName(String name){
        Optional<State> state = statesRepository.findStatesByName(name);
        return state.orElse(null);
    }

    public boolean isValidState(String state) {
        return state != null && statesRepository.findAll().stream().anyMatch(stateInList -> stateInList.getName().equalsIgnoreCase(state));
    }
}

package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StateController {

    @Autowired
    private StateService service;

    public ResponseEntity<?> findStateByName(){
        return null;
    }
}

package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.model.Taxes;
import com.assignment.restapiassignment.service.TaxesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tax")
public class TaxesController {
    @Autowired
    private TaxesService taxesService;

    @PostMapping
    public ResponseEntity<?> createTax(@RequestBody Taxes tax){
        return ResponseEntity.ok(taxesService.createTax(tax));
    }

    @PutMapping
    public ResponseEntity<?> updateTax(@RequestBody Taxes tax, @PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long taxId = Long.valueOf(id);
        if(taxId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        Taxes taxes = taxesService.getTaxById(taxId);
        if(ObjectUtils.isEmpty(taxes)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(taxesService.updateTaxes(tax, taxId));
    }

    @PatchMapping
    public ResponseEntity<?> updateTaxByPatch(@RequestBody Taxes tax, @PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long taxId = Long.valueOf(id);
        if(taxId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        Taxes taxes = taxesService.getTaxById(taxId);
        if(ObjectUtils.isEmpty(taxes)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(taxesService.updateTaxes(tax, taxId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTax(@PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long taxId = Long.valueOf(id);
        if(taxId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        Taxes taxes = taxesService.getTaxById(taxId);
        if(ObjectUtils.isEmpty(taxes)){
            return ResponseEntity.noContent().build();
        }
        taxesService.deleteTax(taxId);
        return ResponseEntity.accepted().build();
    }
}

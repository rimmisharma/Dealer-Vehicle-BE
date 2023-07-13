package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.model.Taxes;
import com.assignment.restapiassignment.repository.TaxesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Optional;

@Service
public class TaxesService {
    @Autowired
    private TaxesRepository taxesRepository;

    public Taxes getTaxById(Long taxId) {
        Optional<Taxes> tax = taxesRepository.findById(taxId);
        return tax.orElse(null);
    }

    public List<Taxes> getAllTaxes(){
        return taxesRepository.findAll();
    }

    public Taxes createTax(Taxes taxes){
        return taxesRepository.save(taxes);
    }

    public Taxes updateTaxes(Taxes taxes, Long id){
        Taxes tax = taxesRepository.findById(id).get();
        if(!ObjectUtils.isEmpty(taxes.getName())){
            tax.setName(taxes.getName());
        }
        if(!ObjectUtils.isEmpty(taxes.getAmount())){
            tax.setAmount(taxes.getAmount());
        }
        if(!ObjectUtils.isEmpty(taxes.getPercentage())){
            tax.setPercentage(taxes.getPercentage());
        }
        return taxesRepository.save(tax);
    }

    public void deleteTax(Long id){
        taxesRepository.deleteById(id);
    }
}

package com.assignment.restapiassignment.service;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.model.*;
import com.assignment.restapiassignment.model.User;
import com.assignment.restapiassignment.repository.*;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DealerOrdersService {
    @Autowired
    private DealerOrdersRepository dealerOrdersRepository;
    @Autowired
    private DealerVehicleRepository dealerVehicleRepository;
    @Autowired
    private DealerRepository dealerRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaxesRepository taxesRepository;


    public DealerOrders getDealerOrder(Long orderId) {
        Optional<DealerOrders> dealerOrders = dealerOrdersRepository.findById(orderId);
        return dealerOrders.orElse(null);
    }


    public boolean isOrderAvailable(Long orderID) {
        Optional<DealerOrders> dealerOrders = dealerOrdersRepository.findById(orderID);
        return dealerOrders.isPresent();
    }

    public List<DealerOrders> getAllOrders() {
        return dealerOrdersRepository.findAll();
    }

    public Map<String, Double> getInvoiceAmount(Long dealerId, Long vehicleId, Long userId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        Optional<User> user = userRepository.findById(userId);
        Double vehicleAmount = vehicle.get().getPrice();
        Double discountAmount = 0.0, invoiceAmount = vehicleAmount;


        double userIncome = user.get().getYearlyIncome().doubleValue();

        if(userIncome > 2000000){
            discountAmount = vehicleAmount * (0.0 / 100);
        }else if(userIncome > 1500000){
            discountAmount = vehicleAmount * (2.50 / 100);
        }else if(userIncome > 1000000){
            discountAmount = vehicleAmount * (3.50 / 100);
        }else if(userIncome > 500000){
            discountAmount = vehicleAmount * (4.50 / 100);
        }else if(userIncome > 100000){
            discountAmount = vehicleAmount * (5.00 / 100);
        }else if(userIncome >= 50000){
            discountAmount = vehicleAmount * (5.50 / 100);
        }else if(userIncome < 50000 && userIncome >= 0){
            discountAmount = vehicleAmount * (0.0 / 100);
        }

        Map<String,Double> invoiceDetails = new LinkedHashMap<>();
        invoiceDetails.put("vehicleAmount", vehicleAmount);

       List<Taxes> taxesByState = taxesRepository.findTaxByState(dealer.get().getState()).get();
        for (Taxes tax : taxesByState) {
            Double taxAmount;
            if (tax.getAmount() != null) {
                taxAmount = tax.getAmount();
            } else {
                taxAmount = vehicleAmount * (tax.getPercentage() / 100);
                invoiceAmount += taxAmount;
            }
            invoiceDetails.put(tax.getName(), taxAmount);
        }
        invoiceDetails.put("discountAmount", discountAmount);
        invoiceAmount -= discountAmount;
        invoiceDetails.put("invoiceAmount", invoiceAmount);

        invoiceDetails.keySet().forEach(System.out::println);
        invoiceDetails.values().forEach(System.out::println);
        return invoiceDetails;
    }



    public DealerOrders createOrders(Long dealerVehicleId, Long dealerId, Long vehicleId, Long userId){
        DealerOrders order = new DealerOrders();

        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);
        Optional<User> user = userRepository.findById(userId);
        Optional<DealerVehicle> dealerVehicle = dealerVehicleRepository.findById(dealerVehicleId);
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);

        if(user.isPresent() && vehicle.isPresent() && dealerVehicle.isPresent() && dealer.isPresent()){
            if(user.get().getYearlyIncome() < 50000 || user.get().getYearlyIncome() * 10 < vehicle.get().getPrice()){
                throw new BadRequestException("User is not eligible.");
            }else{
                order.setDealer(dealer.get());
                order.setVehicle(vehicle.get());
                order.setDealerVehicle(dealerVehicle.get());
                order.setUserDetails(user.get());
            }
        }
        dealerVehicle.get().setQuantity(dealerVehicle.get().getQuantity() - 1L);
        order.setOrderDate(LocalDate.now());
        order.setDeliveryDate(LocalDate.now().plusDays(15));
        order.setInvoiceNo("IN/" + dealer.get().getId() + "/" + System.currentTimeMillis());

        Double userIncome = user.get().getYearlyIncome().doubleValue();
        Double vehicleAmount = vehicle.get().getPrice();

        if(userIncome > 2000000){
            order.setDiscountAmount(0.0);
        }else if(userIncome > 1500000){
           order.setDiscountAmount(2.50);
        }else if(userIncome > 1000000){
            order.setDiscountAmount(3.50);
        }else if(userIncome > 500000){
            order.setDiscountAmount(4.50);
        }else if(userIncome > 100000){
            order.setDiscountAmount(5.00);
        }else if(userIncome >= 50000){
            order.setDiscountAmount(5.50);
        }else if(userIncome < 50000 && userIncome >= 0){
            order.setDiscountAmount(0.0);
        }

        Double discountPercentage = order.getDiscountAmount();
        if(discountPercentage < 0 || discountPercentage > 10.0){
            throw new BadRequestException("Discount not valid.");
        }
        Double discountAmount = vehicleAmount * (discountPercentage / 100);

        Optional<List<Taxes>> stateTaxes = taxesRepository.findTaxByState(dealer.get().getState());
        Double taxAmount = 0.0;
        if (stateTaxes.isPresent()) {
            for (Taxes tax : stateTaxes.get()) {
                if (tax.getAmount() != null) {
                    taxAmount += tax.getAmount();
                } else if (tax.getPercentage() != null) {
                    taxAmount += (vehicleAmount * tax.getPercentage() / 100);
                }
            }
        }

        Double invoiceAmount = vehicleAmount + taxAmount - discountAmount;
        order.setInvoiceAmount(invoiceAmount);
        return dealerOrdersRepository.save(order);
    }
    public DealerOrders updateOrders(DealerOrders dealerOrders) {
        DealerOrders dealerOrders1 = dealerOrdersRepository.findById(dealerOrders.getId()).get();
        if(!ObjectUtils.isEmpty(dealerOrders.getOrderDate())){
            dealerOrders1.setOrderDate(dealerOrders.getOrderDate());
        }
        if(!ObjectUtils.isEmpty(dealerOrders.getDeliveryDate())){
            dealerOrders1.setDeliveryDate(dealerOrders.getDeliveryDate());
        }
        if(!ObjectUtils.isEmpty(dealerOrders.getDiscountAmount())){
            dealerOrders1.setDiscountAmount(dealerOrders.getDiscountAmount());
        }
        if(!ObjectUtils.isEmpty(dealerOrders.getInvoiceAmount())){
            dealerOrders1.setInvoiceAmount(dealerOrders.getInvoiceAmount());
        }
        return dealerOrdersRepository.save(dealerOrders);
    }

    public void deleteById(Long orderId) {
        DealerOrders dealerOrders = dealerOrdersRepository.findById(orderId).orElse(null);

         if (dealerOrders != null) {
             Dealer dealer = dealerOrders.getDealer();
             List<DealerVehicle> list = dealerVehicleRepository.findByDealer(dealer);
             dealerVehicleRepository.deleteAll(list);
       Long dealerId = dealerOrders.getDealer().getId();
       Long vehicleId = dealerOrders.getVehicle().getId();

       dealerOrdersRepository.deleteById(orderId);
       vehicleRepository.deleteById(vehicleId);
       dealerRepository.deleteById(dealerId);
}
    }

    public List<DealerOrders> getOrdersByDealerId(Long id){
        return dealerOrdersRepository.findByDealerId(id);
    }

    public DealerOrders getOrdersByInvoiceNumber(String invoiceNo) {
        DealerOrders dealerOrders = dealerOrdersRepository.findByInvoiceNo(invoiceNo);
        System.out.println("order by invoiceNo: "+invoiceNo);
        return dealerOrders;
    }

    public List<DealerOrders> getOrdersInARange(Long dealerId, String fromDate, String toDate) {
       return dealerOrdersRepository.findByDealerId(dealerId).stream().filter(dealerOrders -> dealerOrders.getOrderDate().isAfter(LocalDate.parse(fromDate).minusDays(1))
                        && dealerOrders.getOrderDate().isBefore(LocalDate.parse(toDate).plusDays(1)))
                .collect(Collectors.toList());

    }

    public Map<String, Float> getTaxesInPercentage(Long dealerId, Long userId) {
        Optional<Dealer> dealer = dealerRepository.findById(dealerId);
        Optional<User> user = userRepository.findById(userId);
        Float discountAmount = 0.0f;

        Map<String, Float> taxesInPercentage = new LinkedHashMap<>();

        List<Taxes> taxesByState = taxesRepository.findTaxByState(dealer.get().getState()).get();
        for (Taxes tax : taxesByState) {
            Float taxPercentage = tax.getPercentage();
            taxesInPercentage.put(tax.getName(), taxPercentage);
        }


        Double userIncome = user.get().getYearlyIncome().doubleValue();

        if(userIncome > 2000000){
           discountAmount = 0.0f;
        }else if(userIncome > 1500000){
            discountAmount = 2.50f;
        }else if(userIncome > 1000000){
            discountAmount = 3.50f;
        }else if(userIncome > 500000){
            discountAmount = 4.50f;
        }else if(userIncome > 100000){
            discountAmount = 5.0f;
        }else if(userIncome >= 50000){
            discountAmount = 5.50f;
        }else if(userIncome < 50000 && userIncome >= 0){
            discountAmount = 0.0f;
        }
        taxesInPercentage.put("discountAmount", discountAmount);

        return taxesInPercentage;
    }

}

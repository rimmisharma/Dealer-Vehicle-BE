package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.model.DealerOrders;
import com.assignment.restapiassignment.model.User;
import com.assignment.restapiassignment.model.Vehicle;
import com.assignment.restapiassignment.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dealerOrder")
public class DealerOrdersController {
    private static final Logger logger = LoggerFactory.getLogger(DealerOrdersController.class);
    @Autowired
    private DealerOrdersService dealerOrdersService;
    @Autowired
    private DealerVehicleService dealerVehicleService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private UserService userService;

    @GetMapping("/getByDealer")
    public ResponseEntity<?> getOrdersByDealerId(@RequestParam String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long orderId = Long.valueOf(id);
        if(orderId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative. Please enter a valid id.");
        }
        if(ObjectUtils.isEmpty(orderId)){
            return ResponseEntity.badRequest().build();
        }
        DealerOrders dealerOrders = dealerOrdersService.getDealerOrder(orderId);
        if(ObjectUtils.isEmpty(dealerOrders)){
            logger.error("Order was not found.");
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(dealerOrdersService.getOrdersByDealerId(orderId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDealerOrder(@PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long orderId = Long.valueOf(id);
        if(orderId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative. Please enter a valid id.");
        }
        if(ObjectUtils.isEmpty(orderId)){
            return ResponseEntity.badRequest().build();
        }
        DealerOrders dealerOrders = dealerOrdersService.getDealerOrder(orderId);
        if(ObjectUtils.isEmpty(dealerOrders)){
            logger.error("Order was not found.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dealerOrders);
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(){
        List<DealerOrders> list = dealerOrdersService.getAllOrders();
        if( ObjectUtils.isEmpty(list)){
            logger.error("Orders were not found.");
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/invoice")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getInvoice(@RequestParam String dealerid, @RequestParam String vehicleid, @RequestParam String userid){
        if (ObjectUtils.isEmpty(dealerid) || ObjectUtils.isEmpty(vehicleid) || ObjectUtils.isEmpty(userid)) {
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("A null ID was passed.");
        }
        if (!dealerid.matches("^[1-9][0-9]*$") || !vehicleid.matches("^[1-9][0-9]*$") || !userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }

        boolean isVehicleAvailable = vehicleService.isVehicleAvailable(Long.valueOf(vehicleid));
        boolean isUserAvailable = userService.isUserAvailable(Long.valueOf(userid));
        boolean isDealerAvailable = dealerService.isDealerAvailable(Long.valueOf(dealerid));
        if(!isVehicleAvailable ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle not found for ID: " + vehicleid);
        } else if (!isUserAvailable) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found for ID: " + userid);
        } else if (!isDealerAvailable) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Dealer not found for ID: " + dealerid);
        }
        Map<String, Object> invoiceAmount = dealerOrdersService.getInvoiceDetails(Long.valueOf(dealerid), Long.valueOf(vehicleid), Long.valueOf(userid));
        if (!invoiceAmount.isEmpty()) {
            return ResponseEntity.ok(invoiceAmount);
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/percentage")
    public ResponseEntity<?> getTaxesInPercentage(@RequestParam String dealerid,  @RequestParam String userid){
        logger.info("invoice in percentage for dealerid: " + dealerid +" ,and userid: " + userid);
        if (ObjectUtils.isEmpty(dealerid)|| ObjectUtils.isEmpty(userid)) {
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("A null ID was passed for ....");
        }
        if (!dealerid.matches("^[1-9][0-9]*$")  || !userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }
        boolean isDealerAvailable = dealerService.isDealerAvailable(Long.valueOf(dealerid));
        boolean isUserAvailable = userService.isUserAvailable(Long.valueOf(userid));
        if(!isDealerAvailable || !isUserAvailable){
            return ResponseEntity.noContent().build();
        }

        Map<String, Float> taxesInPercentage = dealerOrdersService.getTaxesInPercentage(Long.valueOf(dealerid), Long.valueOf(userid));
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        for(String key : taxesInPercentage.keySet()){
            taxesInPercentage.put(key, Float.valueOf(decimalFormat.format(taxesInPercentage.get(key))));

        }

        return ResponseEntity.ok(taxesInPercentage);
    }
    @PostMapping("/create")
    @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.POST, RequestMethod.OPTIONS}, allowedHeaders = {"Authorization", "Content-Type"})
    public ResponseEntity<?> createOrders(@RequestParam String dealervehicleid, @RequestParam String dealerid,
                                          @RequestParam String vehicleid, @RequestParam String userid){
        if (ObjectUtils.isEmpty(dealervehicleid) || ObjectUtils.isEmpty(dealerid) || ObjectUtils.isEmpty(vehicleid) || ObjectUtils.isEmpty(userid)) {
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("A null ID was passed for ....");
        }
        if (!dealervehicleid.matches("^[1-9][0-9]*$") || !dealerid.matches("^[1-9][0-9]*$") ||
                !vehicleid.matches("^[1-9][0-9]*$") || !userid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }
        boolean isDealerVehicleAvailable = dealerVehicleService.isDealerVehicleAvailable(Long.valueOf(dealervehicleid));
        boolean isDealerAvailable = dealerService.isDealerAvailable(Long.valueOf(dealerid));
        boolean isVehicleAvailable = vehicleService.isVehicleAvailable(Long.valueOf(vehicleid));
        boolean isUserAvailable = userService.isUserAvailable(Long.valueOf(userid));
        if(!isDealerVehicleAvailable || !isDealerAvailable || !isVehicleAvailable || !isUserAvailable){
            return ResponseEntity.noContent().build();
        }
        User user = userService.getUserDetails(Long.valueOf(userid));
        Vehicle vehicle = vehicleService.getVehicleById(Long.valueOf(vehicleid));
        if(user.getYearlyIncome() < 50000 || user.getYearlyIncome() * 10 < vehicle.getPrice()){
            return  ResponseEntity.badRequest().body("User is not eligible.");
        }
        return ResponseEntity.ok(dealerOrdersService.createOrders(Long.valueOf(dealervehicleid),
                Long.valueOf(dealerid), Long.valueOf(vehicleid), Long.valueOf(userid)));
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrders(@RequestBody DealerOrders dealerOrders, @PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long orderId = Long.valueOf(id);
        if(orderId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        DealerOrders dealerOrders1 = dealerOrdersService.getDealerOrder(orderId);
        if(ObjectUtils.isEmpty(dealerOrders1)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dealerOrdersService.updateOrders(dealerOrders));
    }


    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateOrdersByPatch(@RequestBody DealerOrders dealerOrders, @PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long orderId = Long.valueOf(id);
        if(orderId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        DealerOrders dealerOrders1 = dealerOrdersService.getDealerOrder(orderId);
        if(ObjectUtils.isEmpty(dealerOrders1)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dealerOrdersService.updateOrders(dealerOrders));
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getOrdersByInvoiceNumber(@RequestParam String invoiceNo){
      if(!invoiceNo.matches("^IN\\d+\\d{13}$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid invoice number! Please re-check and enter a valid invoice number.");
        }
        DealerOrders dealerOrders = dealerOrdersService.getOrdersByInvoiceNumber(invoiceNo);
        if(ObjectUtils.isEmpty(dealerOrders)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dealerOrdersService.getOrdersByInvoiceNumber(invoiceNo));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrdersInARange(@PathVariable String id, @RequestParam String fromDate, @RequestParam String toDate){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long dealerId = Long.valueOf(id);
        if(dealerId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        List<DealerOrders> list = dealerOrdersService.getOrdersInARange(dealerId, fromDate, toDate);
        if(ObjectUtils.isEmpty(list)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(list);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrders(@PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long orderId = Long.valueOf(id);
        if(orderId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        DealerOrders dealerOrders = dealerOrdersService.getDealerOrder(orderId);
        if(ObjectUtils.isEmpty(dealerOrders)){
            return ResponseEntity.noContent().build();
        }
        dealerOrdersService.deleteById(orderId);
         return ResponseEntity.accepted().build();
    }

    @GetMapping("/ordersHistoryByDate")
    public ResponseEntity<?> getOrdersListForDealerInGivenRange(@RequestParam String dealerid,
                                                                @RequestParam String fromDate, @RequestParam String toDate){
        if (ObjectUtils.isEmpty(dealerid)) {
            return ResponseEntity.badRequest().body("dealer ID is null");
        }
        if(!dealerid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.badRequest().body("ID should only be a positive integer!");
        }


        if (!fromDate.matches("^\\d{4}-\\d{2}-\\d{2}$") || !toDate.matches("^\\d{4}-\\d{2}-\\d{2}$")){
            return ResponseEntity.badRequest().body("Format for date is not valid! It should match YYYY-MM-DD");
        }

        List<DealerOrders> dealerOrdersList = dealerOrdersService.getOrdersInARange(Long.valueOf(dealerid), fromDate, toDate);

        if(ObjectUtils.isEmpty(dealerOrdersList)){
            logger.error("Order was not found for dealerid: "+dealerid);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dealerOrdersService.getOrdersInARange(Long.valueOf(dealerid), fromDate, toDate));
    }

    @GetMapping("/getOrdersByDealer")
    public ResponseEntity<?> getOrdersByDealer(@RequestParam String dealerid){
        if (ObjectUtils.isEmpty(dealerid)) {
            logger.error("Null query param was passed.");
            return ResponseEntity.badRequest().body("A null ID was passed for ....");
        }
        if (!dealerid.matches("^[1-9][0-9]*$")) {
            return ResponseEntity.badRequest().body("ID can only be positive integer.");
        }
        boolean isDealerAvailable = dealerService.isDealerAvailable(Long.valueOf(dealerid));
        List<DealerOrders> dealerOrdersList = null;
        if(isDealerAvailable) {
            dealerOrdersList = dealerOrdersService.getOrdersByDealerId(Long.valueOf(dealerid));
        }
         if (ObjectUtils.isEmpty(dealerOrdersList)) {
             return ResponseEntity.notFound().build();
         } else {
             return ResponseEntity.ok(dealerOrdersList);
         }
    }

    @GetMapping
    public ResponseEntity<?> getOrderDetailsByIdOrInvoiceNumber(@RequestParam(required = false) String id, @RequestParam(required = false) String invoicenumber){
        if (ObjectUtils.isEmpty(id) && ObjectUtils.isEmpty(invoicenumber)) {
            return ResponseEntity.badRequest().body("All search parameters are empty! Please enter an id or invoice number!");
        } else if (!(ObjectUtils.isEmpty(id)) && !(ObjectUtils.isEmpty(invoicenumber))) {
            return ResponseEntity.badRequest().body("Received value for both the parameters. Please send either an id or an invoice number!");
        } else if (!(ObjectUtils.isEmpty(invoicenumber))) {
            if(invoicenumber.matches("^IN\\/\\d+\\/\\d+$")) // regex pattern is not correct
            {
                DealerOrders dealerOrderByInvoiceNumber = dealerOrdersService.getOrdersByInvoiceNumber(invoicenumber);
                if (ObjectUtils.isEmpty(dealerOrderByInvoiceNumber)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders were found for invoicenumber: " + invoicenumber + "!");
                }
                return ResponseEntity.ok(dealerOrderByInvoiceNumber);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter a valid invoice number.");
            }
        } else if (!(ObjectUtils.isEmpty(id))) {
            if (id.matches("^[1-9][0-9]*$")) {
                DealerOrders dealerOrdersById = dealerOrdersService.getDealerOrder(Long.valueOf(id));
                if (ObjectUtils.isEmpty(dealerOrdersById)) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No orders found for orderid: " + id + "!");
                }
                return ResponseEntity.ok(dealerOrdersById);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Please enter a valid positive integer.");
            }

        }
        return ResponseEntity.badRequest().body("All search parameters are empty! Please send either an id or an invoicenumber!");
    }
}

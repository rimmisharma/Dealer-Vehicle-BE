package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.model.Dealer;
import com.assignment.restapiassignment.model.DealerVehicle;
import com.assignment.restapiassignment.model.Vehicle;
import com.assignment.restapiassignment.service.DealerService;
import com.assignment.restapiassignment.service.DealerVehicleService;
import com.assignment.restapiassignment.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dealervehicle")
public class DealerVehicleController {

    @Autowired
    private DealerVehicleService dealerVehicleService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private VehicleService vehicleService;

    private static final Logger logger = LoggerFactory.getLogger(DealerVehicleController.class);

    @GetMapping
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getAllDealerVehicles(){
        List<DealerVehicle> list = dealerVehicleService.getAllDealerVehicles();
        if( ObjectUtils.isEmpty(list)){
            logger.error("DealerVehicle(s) were not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/getQuantity")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getDealerVehicleById(@RequestParam String vehicleid){
        logger.info("vehicle id" + vehicleid);
        if(ObjectUtils.isEmpty(vehicleid)){
            logger.error("Null query param userID was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if(!vehicleid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.badRequest().body("ID should only be positive Integer.");
        }
        DealerVehicle dealerVehicleDetails = dealerVehicleService.getDealerVehicleByVehicleId(Long.valueOf(vehicleid));
        if(ObjectUtils.isEmpty(dealerVehicleDetails)){
            logger.error("DealerVehicle was not found.");
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dealerVehicleDetails);
    }

    @GetMapping("/inventory")
    public ResponseEntity<?> getInventoryOfVehicleByDealerId(@RequestParam String dealerid){
        if(!dealerid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }

        Map<String, Long> inventoryOfVehicles = dealerVehicleService.getInventoryOfVehiclesByDealerId(Long.valueOf(dealerid));
        if(ObjectUtils.isEmpty(inventoryOfVehicles)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(inventoryOfVehicles);
    }
    @PostMapping("/{dealerId}")
    public ResponseEntity<?> createDealerVehicle(@PathVariable String dealerId, @RequestParam String vehicleId,
                                                 @RequestBody DealerVehicle dealerVehicle){
        if(!dealerId.matches("^[1-9][0-9]*$") || !vehicleId.matches("^[1-9][0-9]*$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long id1 = Long.valueOf(dealerId);
        Long id2 = Long.valueOf(vehicleId);

        if(id1 <= 0 || id2 <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }
        Dealer dealer = dealerService.getDealerById(id1);
        Vehicle vehicle = vehicleService.getVehicleById(id2);
        if(ObjectUtils.isEmpty(dealer) || ObjectUtils.isEmpty(vehicle)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.created(URI.create("/dealervehicle")).body(dealerVehicleService.createDealerVehicle(id1, id2, dealerVehicle));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDealerVehicle(@PathVariable String id, @RequestBody DealerVehicle dealerVehicle){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long dealerVehicleId = Long.valueOf(id);
        if(dealerVehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }
//        DealerVehicle dealerVehicle1 = dealerVehicleService.getDealerVehicleById(dealerVehicleId);
//        if(ObjectUtils.isEmpty(dealerVehicle1)){
//            return ResponseEntity.noContent().build();
//        }
        return ResponseEntity.ok(dealerVehicleService.updateDealerVehicle(dealerVehicleId, dealerVehicle));
    }
    @PatchMapping("update/{id}")
    public ResponseEntity<?> updateDealerVehicleByPatch(@RequestBody DealerVehicle dealerVehicle, @PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long dealerVehicleId = Long.valueOf(id);
        if(dealerVehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }
        DealerVehicle dealerVehicle1 = dealerVehicleService.getDealerVehicleById(dealerVehicleId);
        if(ObjectUtils.isEmpty(dealerVehicle1)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dealerVehicleService.updateDealerVehicle(dealerVehicleId, dealerVehicle));
    }

    /*@GetMapping
    public ResponseEntity<?> getVehicleInventoryByDealerAndVehicle(@RequestParam String dealerid, @RequestParam String vehicleid){
        if(!dealerid.matches("^[0-9]+$") || !vehicleid.matches("^[0-9]+$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }

        return ResponseEntity.ok(dealerVehicleService.getVehicleInventoryByDealerId(id));
    }*/

    /*@GetMapping("/vehicles/{id}")
    public ResponseEntity<?> getInventoryByVehicleId(@PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long vehicleId = Long.valueOf(id);
        if(vehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if(ObjectUtils.isEmpty(vehicle)){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(dealerVehicleService.getInventoryByVehicleId(vehicleId));
    }*/
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDealerVehicleById(@PathVariable String id){
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long dealerVehicleId = Long.valueOf(id);
        if(dealerVehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be negative.");
        }
        DealerVehicle dealerVehicle = dealerVehicleService.getDealerVehicleById(dealerVehicleId);
        if(ObjectUtils.isEmpty(dealerVehicle)){
            return ResponseEntity.noContent().build();
        }
        dealerVehicleService.deleteDealerVehicle(dealerVehicleId);
        return ResponseEntity.accepted().build();
    }
}

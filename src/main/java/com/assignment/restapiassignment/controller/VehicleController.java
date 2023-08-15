package com.assignment.restapiassignment.controller;

import com.assignment.restapiassignment.exceptions.BadRequestException;
import com.assignment.restapiassignment.exceptions.InternalServerErrorException;
import com.assignment.restapiassignment.model.Vehicle;
import com.assignment.restapiassignment.service.DealerService;
import com.assignment.restapiassignment.service.VehicleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicle")
@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE }, allowedHeaders = "Authorization")
public class VehicleController {
    private static final Logger logger = LoggerFactory.getLogger(VehicleController.class);
    @Autowired
    private VehicleService vehicleService;
    @Autowired
    private DealerService dealerService;


    @GetMapping("/all")
    public ResponseEntity<?> getAllVehicles() {
        List<Vehicle> allVehicleslist = vehicleService.getAllVehicles();
        if(ObjectUtils.isEmpty(allVehicleslist)){
            logger.error("No Vehicles were found in the database.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No vehicles available");
        }
        return ResponseEntity.ok(allVehicleslist);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVehicleById(@PathVariable String id) {
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long vehicleId = Long.valueOf(id);
        if(vehicleId <= 0L){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }

        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        if (ObjectUtils.isEmpty(vehicle)) {
            logger.error("Vehicle was not found for id: "+id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle was not found for id: "+id);
        }
        return ResponseEntity.ok(vehicle);
    }

    @GetMapping("/brands")
    @CrossOrigin(origins = "http://localhost:3000", methods = RequestMethod.GET, allowedHeaders = "Authorization")
    public ResponseEntity<?> getBrandsByDealerID(@RequestParam String dealerid){
        if(ObjectUtils.isEmpty(dealerid)){
            return ResponseEntity.badRequest().body("Null query param was passed.");
        }
        if(!dealerid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id can only be a positive integer.");
        }

        List<String> list = vehicleService.getVehicleBrandsByDealer(Long.valueOf(dealerid));
        if (ObjectUtils.isEmpty(list)) {
            logger.error("Vehicle was not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vehicle was not found.");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/models")
    public ResponseEntity<?> getBrandModelsByDealerIDAndBrandName(@RequestParam String dealerid,
                                                                  @RequestParam String brandname){
        logger.info("Received request_002 for query param "+ dealerid);
        if(ObjectUtils.isEmpty(dealerid)){
            logger.error("Null query param userID was passed.");
            return ResponseEntity.badRequest().body("Please provide a user ID.");
        }
        if(!dealerid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.badRequest().body("ID should only be positive Integer.");
        }
        if(ObjectUtils.isEmpty(brandname)){
            logger.error("brandName is null/empty");
            return ResponseEntity.badRequest().body("Please provide a brand name.");
        }
        if(!brandname.matches("^[A-Za-z]{1,50}$")){
            return ResponseEntity.badRequest().body("Brand name should only contain alphabets with 50 characters limit.");
        }
        List<Vehicle> list = vehicleService.getVehicleModelsByDealerAndBrand(Long.valueOf(dealerid), brandname);

        if (ObjectUtils.isEmpty(list)) {
            logger.error("No models found for dealer id, {} and brand name, {}.",dealerid, brandname);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No models found for given dealer and brand.");
        }
        return ResponseEntity.ok(list);
    }

    @GetMapping("/modelPage")
    public ResponseEntity<?> getModelDetailsByVehicleId(@RequestParam String vehicleid){
        if (ObjectUtils.isEmpty(vehicleid)) {
            return ResponseEntity.badRequest().body("dealer ID is null");
        }
        if(!vehicleid.matches("^[1-9][0-9]*$")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID should only be positive Integer!");
        }
        Vehicle vehicle = vehicleService.getVehicleById(Long.valueOf(vehicleid));
        if(ObjectUtils.isEmpty(vehicle)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No model details were found.");
        }
         return ResponseEntity.ok(vehicle);
    }
    @PostMapping("/create")
    public ResponseEntity<?> addOrUpdateVehicle(@RequestParam String dealerid, @RequestBody Vehicle vehicle) {
        if(!dealerid.matches("^[0-9]+$")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric dealerid.");
        }
         if (vehicle == null) {
            return ResponseEntity.badRequest().body("Null vehicle object was passed.");
        }else if(ObjectUtils.isEmpty(vehicle.getBrand()) || !vehicle.getBrand().matches("^[A-Za-z]{1,50}$")){
            return ResponseEntity.badRequest().body("Brand name cannot be empty/null, it should only contain alphabets with 50 characters limit.");
        }else if(ObjectUtils.isEmpty(vehicle.getClassOfVehicle()) || !vehicle.getClassOfVehicle().matches("^[A-Za-z-]{1,25}$")){
            return ResponseEntity.badRequest().body("Class of vehicle cannot be empty/null or more than 25 alphabetic characters.");
        }else if(ObjectUtils.isEmpty(vehicle.getName()) || !vehicle.getName().matches("^[A-Za-z]{1,50}$")){
            return ResponseEntity.badRequest().body("Vehicle name cannot be empty/null, it should only contain alphabets with 50 characters limit.");
        }else if(ObjectUtils.isEmpty(vehicle.getModelName()) || vehicle.getModelName().length() > 50){
            return ResponseEntity.badRequest().body("Model name cannot be more than 50 characters.");
        }else if(ObjectUtils.isEmpty(vehicle.getColor()) || !vehicle.getColor().matches("^[A-Za-z]{1,10}")){
            return ResponseEntity.badRequest().body("Color cannot be more than 10 characters.");
        }else if(ObjectUtils.isEmpty(vehicle.getTransmission()) || !vehicle.getTransmission().matches("^(AT|MT)$")) {
            return ResponseEntity.badRequest().body("Please use AT for auto transmission or MT for manual transmission.");
        }
         try {
             Vehicle createdOrUpdatedvehicle = vehicleService.createOrUpdateVehicle(Long.valueOf(dealerid), vehicle);
             return ResponseEntity.status(HttpStatus.CREATED).body(createdOrUpdatedvehicle);
         } catch (BadRequestException b) {
             return ResponseEntity.badRequest().body(b);
         } catch (InternalServerErrorException i) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(i);
         }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVehicle(@RequestBody Vehicle vehicle, @PathVariable String id) {
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long vehicleId = Long.valueOf(id);

        if(vehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }
        Vehicle vehicle1 = vehicleService.getVehicleById(vehicleId);
        if(ObjectUtils.isEmpty(vehicle1)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle, vehicleId));
    }
    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateVehicles(@RequestBody Vehicle vehicle, @PathVariable String id) {
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long vehicleId = Long.valueOf(id);

        if(vehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }
        Vehicle vehicle1 = vehicleService.getVehicleById(vehicleId);
        if(ObjectUtils.isEmpty(vehicle1)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle, vehicleId));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateVehiclesByPatch(@RequestBody Vehicle vehicle, @PathVariable String id) {
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long vehicleId = Long.valueOf(id);
        if(vehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }

        Vehicle vehicle1 = vehicleService.getVehicleById(vehicleId);
        if(ObjectUtils.isEmpty(vehicle1)){
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data was found to update.");
        }
        return ResponseEntity.ok(vehicleService.updateVehicle(vehicle, vehicleId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable String id) {
        if(id.matches("[~!@#$%^&*()_+{}\\[\\]:;,.<>/?-]+")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Id should not contain any special characters! Please enter a non negative numeric id.");
        }
        Long vehicleId = Long.valueOf(id);
        if(vehicleId <= 0){
            return ResponseEntity.badRequest().body("Id cannot be zero or negative! Please enter a valid id.");
        }
        if(ObjectUtils.isEmpty(vehicleService.getVehicleById(vehicleId))) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No data was found to delete.");
        }
        vehicleService.deleteVehicleById(vehicleId);
        return ResponseEntity.accepted().build();
    }

}

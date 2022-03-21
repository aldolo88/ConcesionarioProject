package com.example.demo.controllers;

import com.example.demo.model.Provider;
import com.example.demo.model.Vehicle;
import com.example.demo.model.VehicleId;
import com.example.demo.model.repositories.VehicleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;


@RestController
class VehicleController {

    private final VehicleRepository repository;

    VehicleController(VehicleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/vehicles")
    ResponseEntity<?> findAllVehicles(){
        try {
            List<Vehicle> vehicles = repository.findAll();
            return new ResponseEntity<>(vehicles, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/vehicles/{providerId}/{model}/{colour}/{horsePower}/{type}")
    ResponseEntity<?> findVehicle(@PathVariable Long providerId, @PathVariable String model, @PathVariable String colour, @PathVariable int horsePower, @PathVariable String type) {
        List<Vehicle> vehicle = repository.findByVehicleId(providerId,model,colour,horsePower,type);
        if(vehicle.size() == 1) {
            return new ResponseEntity<>(vehicle.get(0), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Id not found: " + "ProviderId-" + providerId + " , model-" + model + " colour-" + colour + " horsePower-" + horsePower + " type-" + type, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/vehicles")
    ResponseEntity<?> newVehicle(@RequestBody Vehicle vehicle) {
        System.out.println("post a vehicles: " + vehicle.toString());
        try {
            repository.save(vehicle);
            return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PutMapping("/vehicles/{providerId}/{model}/{colour}/{horsePower}/{type}")
    ResponseEntity<?> replaceVehicle(@RequestBody Vehicle newVehicle, @PathVariable Long providerId, @PathVariable String model, @PathVariable String colour, @PathVariable int horsePower, @PathVariable String type) {
        List<Vehicle> existingVehicle = repository.findByVehicleId(providerId,model,colour,horsePower,type);
        if(existingVehicle.size() == 1) {
            Vehicle modifiedVehicle = existingVehicle.get(0);
            modifiedVehicle.setPvp(newVehicle.getPvp());
            try {
                repository.save(modifiedVehicle);
                return new ResponseEntity<>(modifiedVehicle, HttpStatus.OK);
            } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                System.out.println("bad request");
                return ResponseEntity.badRequest().body("Bad Request");
            } catch (Exception e) {
                System.out.println("internal server error");
                return ResponseEntity.internalServerError().body("Internal Server Error");
            }
        } else {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + "ProviderId-" + providerId + " , model-" + model + " colour-" + colour + " horsePower-" + horsePower + " type-" + type, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/vehicles/{providerId}/{model}/{colour}/{horsePower}/{type}")
    ResponseEntity<?> deleteVehicle(@PathVariable Long providerId, @PathVariable String model, @PathVariable String colour, @PathVariable int horsePower, @PathVariable String type) {
        try {
            repository.deleteByVehicleId(providerId,model,colour,horsePower,type);
            return new ResponseEntity<>("Deleted Id: " + "ProviderId-" + providerId + " , model-" + model + " colour-" + colour + " horsePower-" + horsePower + " type-" + type, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + "ProviderId-" + providerId + " , model-" + model + " colour-" + colour + " horsePower-" + horsePower + " type-" + type, HttpStatus.NOT_FOUND);
        }
    }
}

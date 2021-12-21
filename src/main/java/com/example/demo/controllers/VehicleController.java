package com.example.demo.controllers;

import com.example.demo.model.Vehicle;
import com.example.demo.model.repositories.VehicleRepository;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
class VehicleController {

    private final VehicleRepository repository;

    VehicleController(VehicleRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/vehicles")
    ResponseEntity<?> newVehicle(@RequestBody Vehicle vehicle) {
        System.out.println("post a vehicles: " + vehicle.toString());
        try{
            repository.save(vehicle);
            return new ResponseEntity<>(vehicle, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


}

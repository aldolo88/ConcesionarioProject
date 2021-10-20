package com.example.demo.controllers;

import com.example.demo.model.Provider;
import com.example.demo.model.repositories.ProviderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
class ProviderController {

    private final ProviderRepository repository;

    ProviderController(ProviderRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/providers")
    ResponseEntity<?> newProvider(@RequestBody Provider provider) {
        try{
            repository.save(provider);
            return new ResponseEntity<>(provider, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }

    }


}

package com.example.demo.controllers;

import com.example.demo.model.Provider;
import com.example.demo.model.repositories.ProviderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;


@RestController
class ProviderController {

    private final ProviderRepository repository;

    ProviderController(ProviderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/providers")
    ResponseEntity<?> findAllProviders(){
        try {
            List<Provider> providers = repository.findAll();
            return new ResponseEntity<>(providers, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/providers/{id}")
    ResponseEntity<?> findProvider(@PathVariable Long id) {
        Optional<Provider> provider = repository.findById(id);
        if(provider.isPresent()) {
            return new ResponseEntity<>(provider, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/providers")
    ResponseEntity<?> newProvider(@RequestBody Provider provider) {
        System.out.println("post a providers: " + provider.toString());
        try {
            repository.save(provider);
            return new ResponseEntity<>(provider, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PutMapping("/providers/{id}")
    ResponseEntity<?> replaceProvider(@RequestBody Provider newProvider, @PathVariable Long id) {
        Optional<Provider> existingProvider = repository.findById(id);
        if (existingProvider.isPresent()) {
            Provider modifiedProvider = existingProvider.get();
            modifiedProvider.setProviderName(newProvider.getProviderName());
            try {
                repository.save(modifiedProvider);
                return new ResponseEntity<>(modifiedProvider, HttpStatus.OK);
            } catch (DataIntegrityViolationException | ConstraintViolationException e) {
                System.out.println("bad request");
                return ResponseEntity.badRequest().body("Bad Request");
            } catch (Exception e) {
                System.out.println("internal server error");
                return ResponseEntity.internalServerError().body("Internal Server Error");
            }
        } else {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/providers/{id}")
    ResponseEntity<?> deleteProvider(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>("Deleted Id: " + id, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }
}

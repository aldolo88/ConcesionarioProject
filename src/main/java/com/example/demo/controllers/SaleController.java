package com.example.demo.controllers;

import com.example.demo.model.Sale;
import com.example.demo.model.repositories.SaleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SaleController {

    private final SaleRepository repository;

    SaleController(SaleRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/sales")
    ResponseEntity<?> newSale(@RequestBody Sale sale) {
        System.out.println("post a sales: " + sale.toString());

        try{
            repository.save(sale);
            return new ResponseEntity<>(sale, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


}

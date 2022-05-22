package com.example.demo.controllers;

import com.example.demo.model.Sale;
import com.example.demo.model.repositories.SaleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
class SaleController {

    private final SaleRepository repository;

    SaleController(SaleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/sales")
    ResponseEntity<?> findAllSales(){
        try {
            List<Sale> sales = repository.findAll();
            return new ResponseEntity<>(sales, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/sales/{id}")
    ResponseEntity<?> findSale(@PathVariable Long id) {
        Optional<Sale> sale = repository.findById(id);
        if(sale.isPresent()) {
            return new ResponseEntity<>(sale, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/sales")
    ResponseEntity<?> newSale(@RequestBody Sale sale) {
        System.out.println("post a sales: " + sale.toString());
        try {
            repository.save(sale);
            return new ResponseEntity<>(sale, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PutMapping("/sales/{id}")
    ResponseEntity<?> replaceSale(@RequestBody Sale newSale, @PathVariable Long id) {
        Optional<Sale> existingSale = repository.findById(id);
        if (existingSale.isPresent()) {
            Sale modifiedSale = existingSale.get();
            modifiedSale.setSaleDate(newSale.getSaleDate());
            modifiedSale.setEmployee(newSale.getEmployee());
            modifiedSale.setClient(newSale.getClient());
            try {
                repository.save(modifiedSale);
                return new ResponseEntity<>(modifiedSale, HttpStatus.OK);
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

    @DeleteMapping("/sales/{id}")
    ResponseEntity<?> deleteSale(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>("Deleted Id: " + id, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }
}

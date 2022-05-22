package com.example.demo.controllers;

import com.example.demo.model.SaleDetail;
import com.example.demo.model.repositories.SaleDetailRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
class SaleDetailController {

    private final SaleDetailRepository repository;

    SaleDetailController(SaleDetailRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/saledetails")
    ResponseEntity<?> findAllSaleDetails(){
        try {
            List<SaleDetail> saleDetails = repository.findAll();
            return new ResponseEntity<>(saleDetails, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/saledetails/{id}")
    ResponseEntity<?> findSaleDetail(@PathVariable Long id) {
        Optional<SaleDetail> saleDetail = repository.findById(id);
        if(saleDetail.isPresent()) {
            return new ResponseEntity<>(saleDetail, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/saledetails")
    ResponseEntity<?> newSaleDetail(@RequestBody SaleDetail saleDetail) {
        System.out.println("post a saleDetails: " + saleDetail.toString());
        try {
            repository.save(saleDetail);
            return new ResponseEntity<>(saleDetail, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PutMapping("/saledetails/{id}")
    ResponseEntity<?> replaceSaleDetail(@RequestBody SaleDetail newSaleDetail, @PathVariable Long id) {
        Optional<SaleDetail> existingSaleDetail = repository.findById(id);
        if (existingSaleDetail.isPresent()) {
            SaleDetail modifiedSaleDetail = existingSaleDetail.get();
            modifiedSaleDetail.setSale(newSaleDetail.getSale());
            modifiedSaleDetail.setProduct(newSaleDetail.getProduct());
            modifiedSaleDetail.setPrice(newSaleDetail.getPrice());
            try {
                repository.save(modifiedSaleDetail);
                return new ResponseEntity<>(modifiedSaleDetail, HttpStatus.OK);
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

    @DeleteMapping("/saledetails/{id}")
    ResponseEntity<?> deleteSaleDetail(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>("Deleted Id: " + id, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }
}

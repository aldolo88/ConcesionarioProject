package com.example.demo.controllers;

import com.example.demo.model.SaleDetail;
import com.example.demo.model.repositories.SaleDetailRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class SaleDetailController {

    private final SaleDetailRepository repository;

    SaleDetailController(SaleDetailRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/saledetails")
    ResponseEntity<?> newSaleDetail(@RequestBody SaleDetail saleDetail) {
        System.out.println("post a saledetails: " + saleDetail.toString());

        try{
            repository.save(saleDetail);
            return new ResponseEntity<>(saleDetail, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


}

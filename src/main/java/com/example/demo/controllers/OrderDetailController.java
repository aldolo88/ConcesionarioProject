package com.example.demo.controllers;

import com.example.demo.model.OrderDetail;
import com.example.demo.model.repositories.OrderDetailRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class OrderDetailController {

    private final OrderDetailRepository repository;

    OrderDetailController(OrderDetailRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/orderdetails")
    ResponseEntity<?> newOrderDetail(@RequestBody OrderDetail orderDetail) {
        System.out.println("post a orderdetails: " + orderDetail.toString());

        try{
            repository.save(orderDetail);
            return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


}

package com.example.demo.controllers;

import com.example.demo.model.OrderDetail;
import com.example.demo.model.repositories.OrderDetailRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
class OrderDetailController {

    private final OrderDetailRepository repository;

    OrderDetailController(OrderDetailRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/orderdetails")
    ResponseEntity<?> findAllOrderDetail(){
        try {
            List<OrderDetail> orderDetail = repository.findAll();
            return new ResponseEntity<>(orderDetail, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/orderdetails/{id}")
    ResponseEntity<?> findOrderDetail(@PathVariable Long id) {
        Optional<OrderDetail> orderDetail = repository.findById(id);
        if(orderDetail.isPresent()) {
            return new ResponseEntity<>(orderDetail, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/orderdetails")
    ResponseEntity<?> newOrderDetail(@RequestBody OrderDetail orderDetail) {
        System.out.println("post a orderdetails: " + orderDetail.toString());
        try{
            repository.save(orderDetail);
            return new ResponseEntity<>(orderDetail, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PutMapping("/orderdetails/{id}")
    ResponseEntity<?> replaceOrderDetail(@RequestBody OrderDetail newOrderDetail, @PathVariable Long id) {
        Optional<OrderDetail> existingOrderDetail = repository.findById(id);
        if (existingOrderDetail.isPresent()) {
            OrderDetail modifiedOrderDetail = existingOrderDetail.get();
            modifiedOrderDetail.setOrder(newOrderDetail.getOrder());
            modifiedOrderDetail.setProduct(newOrderDetail.getProduct());
            modifiedOrderDetail.setPrice(newOrderDetail.getPrice());
            try {
                repository.save(modifiedOrderDetail);
                return new ResponseEntity<>(modifiedOrderDetail, HttpStatus.OK);
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

    @DeleteMapping("/orderdetails/{id}")
    ResponseEntity<?> deleteOrderDetail(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>("Deleted Id: " + id, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }
}

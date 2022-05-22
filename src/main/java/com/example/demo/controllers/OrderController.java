package com.example.demo.controllers;

import com.example.demo.model.Order;
import com.example.demo.model.repositories.OrderRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
class OrderController {

    private final OrderRepository repository;

    OrderController(OrderRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/orders")
    ResponseEntity<?> findAllOrders(){
        try {
            List<Order> orders = repository.findAll();
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/orders/{id}")
    ResponseEntity<?> findOrder(@PathVariable Long id) {
        Optional<Order> order = repository.findById(id);
        if(order.isPresent()) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/orders")
    ResponseEntity<?> newOrder(@RequestBody Order order) {
        System.out.println("post a orders: " + order.toString());
        try {
            repository.save(order);
            return new ResponseEntity<>(order, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PutMapping("/orders/{id}")
    ResponseEntity<?> replaceOrder(@RequestBody Order newOrder, @PathVariable Long id) {
        Optional<Order> existingOrder = repository.findById(id);
        if (existingOrder.isPresent()) {
            Order modifiedOrder = existingOrder.get();
            modifiedOrder.setEmployee(newOrder.getEmployee());
            modifiedOrder.setProvider(newOrder.getProvider());
            modifiedOrder.setOrderDate(newOrder.getOrderDate());
            try {
                repository.save(modifiedOrder);
                return new ResponseEntity<>(modifiedOrder, HttpStatus.OK);
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

    @DeleteMapping("/orders/{id}")
    ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>("Deleted Id: " + id, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }
}

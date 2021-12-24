package com.example.demo.controllers;

import com.example.demo.model.Product;
import com.example.demo.model.repositories.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class ProductController {

    private final ProductRepository repository;

    ProductController(ProductRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/products")
    ResponseEntity<?> newProduct(@RequestBody Product product) {
        System.out.println("post a products: " + product.toString());

        try{
            repository.save(product);
            return new ResponseEntity<>(product, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


}

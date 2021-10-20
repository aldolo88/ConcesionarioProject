package com.example.demo.controllers;

import com.example.demo.model.Client;
import com.example.demo.model.repositories.ClientRepository;
import org.hibernate.PropertyValueException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
class ClientController {

    private final ClientRepository repository;

    ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/clients")
    ResponseEntity<?> newClient(@RequestBody Client client) {
        System.out.println("post a clients: " + client.toString());
        try{
            repository.save(client);
            return new ResponseEntity<>(client, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }


}

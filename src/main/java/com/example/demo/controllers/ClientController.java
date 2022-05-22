package com.example.demo.controllers;

import com.example.demo.model.Client;
import com.example.demo.model.repositories.ClientRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@RestController
class ClientController {

    private final ClientRepository repository;

    ClientController(ClientRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/clients")
    ResponseEntity<?> findAllClients(){
        try {
            List<Client> clients = repository.findAll();
            return new ResponseEntity<>(clients, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @GetMapping("/clients/{id}")
    ResponseEntity<?> findClient(@PathVariable Long id) {
        Optional<Client> client = repository.findById(id);
        if(client.isPresent()) {
            return new ResponseEntity<>(client, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/clients")
    ResponseEntity<?> newClient(@RequestBody Client client) {
        System.out.println("post a clients: " + client.toString());
        try{
            repository.save(client);
            return new ResponseEntity<>(client, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException | ConstraintViolationException e) {
            System.out.println("bad request");
            return ResponseEntity.badRequest().body("Bad Request");
        } catch (Exception e) {
            System.out.println("internal server error");
            return ResponseEntity.internalServerError().body("Internal Server Error");
        }
    }

    @PutMapping("/clients/{id}")
    ResponseEntity<?> replaceClient(@RequestBody Client newClient, @PathVariable Long id) {
        Optional<Client> existingClient = repository.findById(id);
        if (existingClient.isPresent()) {
            Client modifiedClient = existingClient.get();
            modifiedClient.setName(newClient.getName());
            modifiedClient.setDni(newClient.getDni());
            modifiedClient.setPhone(newClient.getPhone());
            try {
                repository.save(modifiedClient);
                return new ResponseEntity<>(modifiedClient, HttpStatus.OK);
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

    @DeleteMapping("/clients/{id}")
    ResponseEntity<?> deleteClient(@PathVariable Long id) {
        try {
            repository.deleteById(id);
            return new ResponseEntity<>("Deleted Id: " + id, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            System.out.println("id not found");
            return new ResponseEntity<>("Id not found: " + id, HttpStatus.NOT_FOUND);
        }
    }

}

package com.example.demo.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.Client;
import com.example.demo.model.repositories.ClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ClientRepository clientRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllClients_success() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        List<Client> allClients = List.of(client);
        Mockito.when(clientRepository.findAll()).thenReturn(allClients);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/clients")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].dni",is(client.getDni())))
                .andExpect(jsonPath("$[0].name",is(client.getName())))
                .andExpect(jsonPath("$[0].phone",is(client.getPhone())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllClients_error() throws Exception {
        //Given
        Mockito.when(clientRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/clients")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneClient_success() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        Mockito.when(clientRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(client));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/clients/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.dni",is(client.getDni())))
                .andExpect(jsonPath("$.name",is(client.getName())))
                .andExpect(jsonPath("$.phone",is(client.getPhone())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneClient_notFound() throws Exception {
        //Given
        Mockito.when(clientRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/clients/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newClient_success() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(client);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(client));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.dni",is(client.getDni())))
                .andExpect(jsonPath("$.name",is(client.getName())))
                .andExpect(jsonPath("$.phone",is(client.getPhone())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newClient_ko_null_attribute_JSON() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(client));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newClient_ko_Internal_server_error() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(client));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putClient_success() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        Client modClient = new TestClassConstructors().TestModClient();
        Mockito.when(clientRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(modClient);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modClient));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.dni",is(modClient.getDni())))
                .andExpect(jsonPath("$.name",is(modClient.getName())))
                .andExpect(jsonPath("$.phone",is(modClient.getPhone())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putClient_ko_null_attribute_JSON() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        Mockito.when(clientRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(client));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putClient_ko_Internal_server_error() throws Exception {
        //Given
        Client client = new TestClassConstructors().TestClient();
        Mockito.when(clientRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(client));
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(client));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putClient_notFound() throws Exception {
        //Given
        Client modClient = new TestClassConstructors().TestModClient();
        Mockito.when(clientRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/clients/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modClient));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneClient_success() throws Exception {
        //Given
        Mockito.doNothing().when(clientRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/clients/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneClient_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(clientRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/clients/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }
}

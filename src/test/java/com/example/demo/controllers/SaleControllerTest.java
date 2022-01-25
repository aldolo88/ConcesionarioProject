package com.example.demo.controllers;

import com.example.demo.model.Employee;
import com.example.demo.model.Sale;
import com.example.demo.model.Client;
import com.example.demo.model.repositories.SaleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaleController.class)
public class SaleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SaleRepository saleRepository;


    @Test
    public void newSale_success() throws Exception {
        //Given
        Employee employee = new Employee("Alberto", "Rol1");
        Client client = new Client("0123456789A","Client1",123456789);
        Date date = new Date(0L);
        Sale sale = new Sale(client, employee, date);
        Mockito.when(saleRepository.save(sale)).thenReturn(sale);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(content().json("{\"id\": 0,\"client\": {\"id\": 0,\"dni\": \"0123456789A\",\"name\":\"Client1\",\"phone\":123456789},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"saleDate\":\"1970-01-01T00:00:00.000+00:00\"}"))
                .andExpect(jsonPath("$.id",notNullValue()));
    }
    @Test
    public void newSale_ko_empty_JSON() throws Exception {
        //Given
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newSale_ko_wrong_attribute_JSON() throws Exception {
        //Given
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0,\"campoaleatorio1\": {\"id\": 0,\"dni\": \"0123456789A\",\"name\":\"Client1\",\"phone\":123456789},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"saleDate\":\"1970-01-01T00:00:00.000+00:00\"}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newSale_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenThrow(new NullPointerException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0,\"client\": {\"id\": 0,\"dni\": \"0123456789A\",\"name\":\"Client1\",\"phone\":123456789},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"saleDate\":\"1970-01-01T00:00:00.000+00:00\"}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }
}


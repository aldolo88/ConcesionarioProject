package com.example.demo.controllers;

import com.example.demo.model.Employee;
import com.example.demo.model.Provider;
import com.example.demo.model.Order;
import com.example.demo.model.repositories.OrderRepository;
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

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderRepository orderRepository;


    @Test
    public void newOrder_success() throws Exception {
        //Given
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        Date date = new Date(0L);
        Order order = new Order(provider,employee, date);
        Mockito.when(orderRepository.save(order)).thenReturn(order);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(content().json("{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"}"))
                .andExpect(jsonPath("$.id",notNullValue()));
    }
    @Test
    public void newOrder_ko_empty_JSON() throws Exception {
        //Given
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newOrder_ko_wrong_attribute_JSON() throws Exception {
        //Given
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0,\"campoaleatorio1\": {\"id\": 0,\"providerName\": \"Provider1\"},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newOrder_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new NullPointerException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }
}


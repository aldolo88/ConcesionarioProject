package com.example.demo.controllers;

import com.example.demo.model.*;
import com.example.demo.model.repositories.OrderDetailRepository;
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

@WebMvcTest(OrderDetailController.class)
public class OrderDetailControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderDetailRepository orderDetailRepository;


    @Test
    public void newOrderDetail_success() throws Exception {
        //Given
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        Date date = new Date(0L);
        Order order = new Order(provider,employee, date);
        VehicleId vehicleId = new VehicleId(provider,"modelo","color",100, VehicleId.Type.COCHE);
        Vehicle vehicle = new Vehicle(vehicleId, 15000);
        Product product = new Product(vehicle, "123asd");
        OrderDetail orderDetail = new OrderDetail(order,product,25000.0);
        Mockito.when(orderDetailRepository.save(orderDetail)).thenReturn(orderDetail);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(content().json("{\"id\":0,\"order\":{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"},\"product\":{\"vehicle\":{\"id\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0},\"serialNumber\":\"123asd\"},\"price\":25000.0}"))
                .andExpect(jsonPath("$.id",notNullValue()));
    }
    @Test
    public void newOrderDetail_ko_empty_JSON() throws Exception {
        //Given
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newOrderDetail_ko_wrong_attribute_JSON() throws Exception {
        //Given
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":0,\"campoaleatorio1\":{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"},\"product\":{\"vehicle\":{\"id\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0},\"serialNumber\":\"123asd\"},\"price\":25000.0}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newOrderDetail_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenThrow(new NullPointerException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":0,\"order\":{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"},\"product\":{\"vehicle\":{\"id\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0},\"serialNumber\":\"123asd\"},\"price\":25000.0}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }
}


package com.example.demo.controllers;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.Order;
import com.example.demo.model.repositories.OrderRepository;
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
import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderRepository orderRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllOrders_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Order order = constructor.TestOrder();
        List<Order> allOrders = List.of(order);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orders")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].employee.name",is(order.getEmployee().getName())))
                .andExpect(jsonPath("$[0].employee.role",is(order.getEmployee().getRole())))
                .andExpect(jsonPath("$[0].provider.providerName",is(order.getProvider().getProviderName())))
                .andExpect(jsonPath("$[0].orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllOrders_error() throws Exception {
        //Given
        Mockito.when(orderRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orders")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneOrder_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Order order = constructor.TestOrder();
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.employee.name",is(order.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(order.getEmployee().getRole())))
                .andExpect(jsonPath("$.provider.providerName",is(order.getProvider().getProviderName())))
                .andExpect(jsonPath("$.orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneOrder_notFound() throws Exception {
        //Given
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newOrder_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Order order = constructor.TestOrder();
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.employee.name",is(order.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(order.getEmployee().getRole())))
                .andExpect(jsonPath("$.provider.providerName",is(order.getProvider().getProviderName())))
                .andExpect(jsonPath("$.orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newOrder_ko_null_attribute_JSON() throws Exception {
        //Given
        Order order = new TestClassConstructors().TestOrder();
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }


    @Test
    public void newOrder_ko_Internal_server_error() throws Exception {
        //Given
        Order order = new TestClassConstructors().TestOrder();
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putOrder_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Order order = constructor.TestOrder();
        Order modOrder = constructor.TestModOrder();
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(modOrder);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modOrder));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.employee.name",is(modOrder.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(modOrder.getEmployee().getRole())))
                .andExpect(jsonPath("$.provider.providerName",is(modOrder.getProvider().getProviderName())))
                .andExpect(jsonPath("$.orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putOrder_ko_null_attribute_JSON() throws Exception {
        //Given
        Order order = new TestClassConstructors().TestOrder();
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putOrder_ko_Internal_server_error() throws Exception {
        //Given
        Order order = new TestClassConstructors().TestOrder();
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putOrder_notFound() throws Exception {
        //Given
        Order modOrder = new TestClassConstructors().TestModOrder();
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modOrder));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneOrder_success() throws Exception {
        //Given
        Mockito.doNothing().when(orderRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/orders/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneOrder_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(orderRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/orders/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

}


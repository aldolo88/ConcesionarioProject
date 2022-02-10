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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
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
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        Order order1 = new Order(provider,employee, date);
        List<Order> allOrders = List.of(order1);
        Mockito.when(orderRepository.findAll()).thenReturn(allOrders);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orders")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].employee.name",is(order1.getEmployee().getName())))
                .andExpect(jsonPath("$[0].employee.role",is(order1.getEmployee().getRole())))
                .andExpect(jsonPath("$[0].provider.providerName",is(order1.getProvider().getProviderName())))
                .andExpect(jsonPath("$[0].orderDate",is(sdf.format(date))))
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
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        Order order1 = new Order(provider,employee, date);
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order1));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orders/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.employee.name",is(order1.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(order1.getEmployee().getRole())))
                .andExpect(jsonPath("$.provider.providerName",is(order1.getProvider().getProviderName())))
                .andExpect(jsonPath("$.orderDate",is(sdf.format(date))))
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
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        Order order1 = new Order(provider,employee, date);
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order1);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(order1));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.employee.name",is(order1.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(order1.getEmployee().getRole())))
                .andExpect(jsonPath("$.provider.providerName",is(order1.getProvider().getProviderName())))
                .andExpect(jsonPath("$.orderDate",is(sdf.format(date))))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newOrder_ko_null_attribute_JSON() throws Exception {
        //Given
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new ConstraintViolationException("error", null));
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
    public void newOrder_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"Employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putOrder_success() throws Exception {
        //Given
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        Order order1 = new Order(provider,employee, date);
        Employee modEmployee1 = new Employee("Alber", "Rol2");
        Order modOrder1 = new Order(provider, modEmployee1,date);
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order1));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(modOrder1);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modOrder1));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.employee.name",is(modOrder1.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(modOrder1.getEmployee().getRole())))
                .andExpect(jsonPath("$.provider.providerName",is(modOrder1.getProvider().getProviderName())))
                .andExpect(jsonPath("$.orderDate",is(sdf.format(date))))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putOrder_ko_null_attribute_JSON() throws Exception {
        //Given
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        Order order1 = new Order(provider,employee, date);
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order1));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"Employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putOrder_ko_Internal_server_error() throws Exception {
        //Given
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        Order order1 = new Order(provider,employee, date);
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(order1));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0,\"provider\": {\"id\": 0,\"providerName\": \"Provider1\"},\"Employee\": {\"id\": 0,\"name\": \"Alberto\",\"role\": \"Rol1\"},\"orderDate\":\"1970-01-01T00:00:00.000+00:00\"}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putOrder_notFound() throws Exception {
        //Given
        Employee employee = new Employee("Alberto", "Rol1");
        Provider provider = new Provider("Provider1");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'+00:00'");//Al cambiar a GMT, la zona horaria se representa con Z en lugar de +00:00, por lo que lo añado manual para que coincida con lo esperado
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = new Date();
        Order modOrder1 = new Order(provider,employee, date);
        Mockito.when(orderRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modOrder1));
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


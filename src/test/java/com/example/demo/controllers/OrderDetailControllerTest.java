package com.example.demo.controllers;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.*;
import com.example.demo.model.repositories.OrderDetailRepository;
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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderDetailController.class)
public class OrderDetailControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    OrderDetailRepository orderDetailRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllOrderDetails_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        OrderDetail orderDetail = constructor.TestOrderDetail();
        List<OrderDetail> allOrderDetails = List.of(orderDetail);
        Mockito.when(orderDetailRepository.findAll()).thenReturn(allOrderDetails);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].price",is(orderDetail.getPrice())))
                .andExpect(jsonPath("$[0].order.employee.name",is(orderDetail.getOrder().getEmployee().getName())))
                .andExpect(jsonPath("$[0].order.employee.role",is(orderDetail.getOrder().getEmployee().getRole())))
                .andExpect(jsonPath("$[0].order.provider.providerName",is(orderDetail.getOrder().getProvider().getProviderName())))
                .andExpect(jsonPath("$[0].order.orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$[0].product.serialNumber",is(orderDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$[0].product.vehicle.pvp",is(orderDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$[0].product.vehicle.id.provider.providerName",is(orderDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$[0].product.vehicle.id.model",is(orderDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$[0].product.vehicle.id.colour",is(orderDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$[0].product.vehicle.id.horsePower",is(orderDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$[0].product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllOrderDetails_error() throws Exception {
        //Given
        Mockito.when(orderDetailRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneOrderDetail_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        OrderDetail orderDetail = constructor.TestOrderDetail();
        Mockito.when(orderDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(orderDetail));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.price",is(orderDetail.getPrice())))
                .andExpect(jsonPath("$.order.employee.name",is(orderDetail.getOrder().getEmployee().getName())))
                .andExpect(jsonPath("$.order.employee.role",is(orderDetail.getOrder().getEmployee().getRole())))
                .andExpect(jsonPath("$.order.provider.providerName",is(orderDetail.getOrder().getProvider().getProviderName())))
                .andExpect(jsonPath("$.order.orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.product.serialNumber",is(orderDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$.product.vehicle.pvp",is(orderDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$.product.vehicle.id.provider.providerName",is(orderDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.product.vehicle.id.model",is(orderDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.product.vehicle.id.colour",is(orderDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.product.vehicle.id.horsePower",is(orderDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneOrderDetail_notFound() throws Exception {
        //Given
        Mockito.when(orderDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newOrderDetail_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        OrderDetail orderDetail = constructor.TestOrderDetail();
        Mockito.when(orderDetailRepository.save(orderDetail)).thenReturn(orderDetail);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.price",is(orderDetail.getPrice())))
                .andExpect(jsonPath("$.order.employee.name",is(orderDetail.getOrder().getEmployee().getName())))
                .andExpect(jsonPath("$.order.employee.role",is(orderDetail.getOrder().getEmployee().getRole())))
                .andExpect(jsonPath("$.order.provider.providerName",is(orderDetail.getOrder().getProvider().getProviderName())))
                .andExpect(jsonPath("$.order.orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.product.serialNumber",is(orderDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$.product.vehicle.pvp",is(orderDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$.product.vehicle.id.provider.providerName",is(orderDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.product.vehicle.id.model",is(orderDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.product.vehicle.id.colour",is(orderDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.product.vehicle.id.horsePower",is(orderDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newOrderDetail_ko_null_attribute_JSON() throws Exception {
        //Given
        OrderDetail orderDetail = new TestClassConstructors().TestOrderDetail();
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newOrderDetail_ko_Internal_server_error() throws Exception {
        //Given
        OrderDetail orderDetail = new TestClassConstructors().TestOrderDetail();
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/orderdetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putOrderDetail_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        OrderDetail orderDetail = constructor.TestOrderDetail();
        OrderDetail modOrderDetail = constructor.TestModOrderDetail();
        Mockito.when(orderDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(orderDetail));
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenReturn(modOrderDetail);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modOrderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.price",is(modOrderDetail.getPrice())))
                .andExpect(jsonPath("$.order.employee.name",is(modOrderDetail.getOrder().getEmployee().getName())))
                .andExpect(jsonPath("$.order.employee.role",is(modOrderDetail.getOrder().getEmployee().getRole())))
                .andExpect(jsonPath("$.order.provider.providerName",is(modOrderDetail.getOrder().getProvider().getProviderName())))
                .andExpect(jsonPath("$.order.orderDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.product.serialNumber",is(modOrderDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$.product.vehicle.pvp",is(modOrderDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$.product.vehicle.id.provider.providerName",is(modOrderDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.product.vehicle.id.model",is(modOrderDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.product.vehicle.id.colour",is(modOrderDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.product.vehicle.id.horsePower",is(modOrderDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putOrderDetail_ko_null_attribute_JSON() throws Exception {
        //Given
        OrderDetail orderDetail = new TestClassConstructors().TestOrderDetail();
        Mockito.when(orderDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(orderDetail));
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putOrderDetail_ko_Internal_server_error() throws Exception {
        //Given
        OrderDetail orderDetail = new TestClassConstructors().TestOrderDetail();
        Mockito.when(orderDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(orderDetail));
        Mockito.when(orderDetailRepository.save(Mockito.any(OrderDetail.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putOrderDetail_notFound() throws Exception {
        //Given
        OrderDetail orderDetail = new TestClassConstructors().TestOrderDetail();
        Mockito.when(orderDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(orderDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneOrderDetail_success() throws Exception {
        //Given
        Mockito.doNothing().when(orderDetailRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneOrderDetail_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(orderDetailRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/orderdetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }
}



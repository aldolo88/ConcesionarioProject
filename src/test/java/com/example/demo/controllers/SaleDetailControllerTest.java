package com.example.demo.controllers;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.*;
import com.example.demo.model.repositories.SaleDetailRepository;
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

@WebMvcTest(SaleDetailController.class)
public class SaleDetailControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SaleDetailRepository saleDetailRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllSaleDetails_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        SaleDetail saleDetail = constructor.TestSaleDetail();
        List<SaleDetail> allSaleDetails = List.of(saleDetail);
        Mockito.when(saleDetailRepository.findAll()).thenReturn(allSaleDetails);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/saledetails")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].price",is(saleDetail.getPrice())))
                .andExpect(jsonPath("$[0].sale.employee.name",is(saleDetail.getSale().getEmployee().getName())))
                .andExpect(jsonPath("$[0].sale.employee.role",is(saleDetail.getSale().getEmployee().getRole())))
                .andExpect(jsonPath("$[0].sale.saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$[0].product.serialNumber",is(saleDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$[0].product.vehicle.pvp",is(saleDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$[0].product.vehicle.id.model",is(saleDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$[0].product.vehicle.id.colour",is(saleDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$[0].product.vehicle.id.horsePower",is(saleDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$[0].product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$[0].product.vehicle.id.provider.providerName",is(saleDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllSaleDetails_error() throws Exception {
        //Given
        Mockito.when(saleDetailRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/saledetails")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneSaleDetail_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        SaleDetail saleDetail = constructor.TestSaleDetail();
        Mockito.when(saleDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saleDetail));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.price",is(saleDetail.getPrice())))
                .andExpect(jsonPath("$.sale.employee.name",is(saleDetail.getSale().getEmployee().getName())))
                .andExpect(jsonPath("$.sale.employee.role",is(saleDetail.getSale().getEmployee().getRole())))
                .andExpect(jsonPath("$.sale.saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.product.serialNumber",is(saleDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$.product.vehicle.pvp",is(saleDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$.product.vehicle.id.model",is(saleDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.product.vehicle.id.colour",is(saleDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.product.vehicle.id.horsePower",is(saleDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.product.vehicle.id.provider.providerName",is(saleDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneSaleDetail_notFound() throws Exception {
        //Given
        Mockito.when(saleDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newSaleDetail_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        SaleDetail saleDetail = constructor.TestSaleDetail();
        Mockito.when(saleDetailRepository.save(Mockito.any(SaleDetail.class))).thenReturn(saleDetail);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/saledetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(saleDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.price",is(saleDetail.getPrice())))
                .andExpect(jsonPath("$.sale.employee.name",is(saleDetail.getSale().getEmployee().getName())))
                .andExpect(jsonPath("$.sale.employee.role",is(saleDetail.getSale().getEmployee().getRole())))
                .andExpect(jsonPath("$.sale.saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.product.serialNumber",is(saleDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$.product.vehicle.pvp",is(saleDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$.product.vehicle.id.model",is(saleDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.product.vehicle.id.colour",is(saleDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.product.vehicle.id.horsePower",is(saleDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.product.vehicle.id.provider.providerName",is(saleDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newSaleDetail_ko_null_attribute_JSON() throws Exception {
        //Given
        SaleDetail saleDetail = new TestClassConstructors().TestSaleDetail();
        Mockito.when(saleDetailRepository.save(Mockito.any(SaleDetail.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/saledetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(saleDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newSaleDetail_ko_Internal_server_error() throws Exception {
        //Given
        SaleDetail saleDetail = new TestClassConstructors().TestSaleDetail();
        Mockito.when(saleDetailRepository.save(Mockito.any(SaleDetail.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/saledetails")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(saleDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putSaleDetail_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        SaleDetail saleDetail = constructor.TestSaleDetail();
        SaleDetail modSaleDetail = constructor.TestModSaleDetail();
        Mockito.when(saleDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saleDetail));
        Mockito.when(saleDetailRepository.save(Mockito.any(SaleDetail.class))).thenReturn(modSaleDetail);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modSaleDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.price",is(modSaleDetail.getPrice())))
                .andExpect(jsonPath("$.sale.employee.name",is(modSaleDetail.getSale().getEmployee().getName())))
                .andExpect(jsonPath("$.sale.employee.role",is(modSaleDetail.getSale().getEmployee().getRole())))
                .andExpect(jsonPath("$.sale.saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.product.serialNumber",is(modSaleDetail.getProduct().getSerialNumber())))
                .andExpect(jsonPath("$.product.vehicle.pvp",is(modSaleDetail.getProduct().getVehicle().getPvp())))
                .andExpect(jsonPath("$.product.vehicle.id.model",is(modSaleDetail.getProduct().getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.product.vehicle.id.colour",is(modSaleDetail.getProduct().getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.product.vehicle.id.horsePower",is(modSaleDetail.getProduct().getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.product.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.product.vehicle.id.provider.providerName",is(modSaleDetail.getProduct().getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putSaleDetail_ko_null_attribute_JSON() throws Exception {
        //Given
        SaleDetail saleDetail = new TestClassConstructors().TestSaleDetail();
        Mockito.when(saleDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saleDetail));
        Mockito.when(saleDetailRepository.save(Mockito.any(SaleDetail.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(saleDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putSaleDetail_ko_Internal_server_error() throws Exception {
        //Given
        SaleDetail saleDetail = new TestClassConstructors().TestSaleDetail();
        Mockito.when(saleDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(saleDetail));
        Mockito.when(saleDetailRepository.save(Mockito.any(SaleDetail.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(saleDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putSaleDetail_notFound() throws Exception {
        //Given
        SaleDetail saleDetail = new TestClassConstructors().TestSaleDetail();
        Mockito.when(saleDetailRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(saleDetail));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneSaleDetail_success() throws Exception {
        //Given
        Mockito.doNothing().when(saleDetailRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneSaleDetail_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(saleDetailRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/saledetails/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }
}

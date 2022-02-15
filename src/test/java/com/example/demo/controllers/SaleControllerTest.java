package com.example.demo.controllers;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.Sale;
import com.example.demo.model.repositories.SaleRepository;
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

@WebMvcTest(SaleController.class)
public class SaleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    SaleRepository saleRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllSales_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Sale sale = constructor.TestSale();
        List<Sale> allSales = List.of(sale);
        Mockito.when(saleRepository.findAll()).thenReturn(allSales);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sales")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$[0].client.name",is(sale.getClient().getName())))
                .andExpect(jsonPath("$[0].client.dni",is(sale.getClient().getDni())))
                .andExpect(jsonPath("$[0].client.phone",is(sale.getClient().getPhone())))
                .andExpect(jsonPath("$[0].employee.name",is(sale.getEmployee().getName())))
                .andExpect(jsonPath("$[0].employee.role",is(sale.getEmployee().getRole())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllSales_error() throws Exception {
        //Given
        Mockito.when(saleRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sales")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneSale_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Sale sale = constructor.TestSale();
        Mockito.when(saleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(sale));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sales/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.client.name",is(sale.getClient().getName())))
                .andExpect(jsonPath("$.client.dni",is(sale.getClient().getDni())))
                .andExpect(jsonPath("$.client.phone",is(sale.getClient().getPhone())))
                .andExpect(jsonPath("$.employee.name",is(sale.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(sale.getEmployee().getRole())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneSale_notFound() throws Exception {
        //Given
        Mockito.when(saleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/sales/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newSale_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Sale sale = constructor.TestSale();
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenReturn(sale);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.client.name",is(sale.getClient().getName())))
                .andExpect(jsonPath("$.client.dni",is(sale.getClient().getDni())))
                .andExpect(jsonPath("$.client.phone",is(sale.getClient().getPhone())))
                .andExpect(jsonPath("$.employee.name",is(sale.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(sale.getEmployee().getRole())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newSale_ko_null_attribute_JSON() throws Exception {
        //Given
        Sale sale = new TestClassConstructors().TestSale();
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newSale_ko_Internal_server_error() throws Exception {
        //Given
        Sale sale = new TestClassConstructors().TestSale();
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/sales")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putSale_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Sale sale = constructor.TestSale();
        Sale modSale = constructor.TestModSale();
        Mockito.when(saleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(sale));
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenReturn(modSale);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modSale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.saleDate",is(constructor.getFormattedDate())))
                .andExpect(jsonPath("$.client.name",is(modSale.getClient().getName())))
                .andExpect(jsonPath("$.client.dni",is(modSale.getClient().getDni())))
                .andExpect(jsonPath("$.client.phone",is(modSale.getClient().getPhone())))
                .andExpect(jsonPath("$.employee.name",is(modSale.getEmployee().getName())))
                .andExpect(jsonPath("$.employee.role",is(modSale.getEmployee().getRole())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putSale_ko_null_attribute_JSON() throws Exception {
        //Given
        Sale sale = new TestClassConstructors().TestSale();
        Mockito.when(saleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(sale));
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putSale_ko_Internal_server_error() throws Exception {
        //Given
        Sale sale = new TestClassConstructors().TestSale();
        Mockito.when(saleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(sale));
        Mockito.when(saleRepository.save(Mockito.any(Sale.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putSale_notFound() throws Exception {
        //Given
        Sale sale = new TestClassConstructors().TestSale();
        Mockito.when(saleRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/sales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(sale));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneSale_success() throws Exception {
        //Given
        Mockito.doNothing().when(saleRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/sales/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneSale_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(saleRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/sales/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }
}


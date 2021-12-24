package com.example.demo.controllers;

import com.example.demo.model.Provider;
import com.example.demo.model.Vehicle;
import com.example.demo.model.VehicleId;
import com.example.demo.model.Product;
import com.example.demo.model.repositories.ProductRepository;
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

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductRepository productRepository;


    @Test
    public void newProduct_success() throws Exception {
        //Given
        Provider provider = new Provider("Provider1");
        VehicleId vehicleId = new VehicleId(provider,"modelo","color",100, VehicleId.Type.COCHE);
        Vehicle vehicle = new Vehicle(vehicleId, 15000);
        Product product = new Product(vehicle, "123asd");
        Mockito.when(productRepository.save(product)).thenReturn(product);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(content().json("{\"vehicle\":{\"id\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0},\"serialNumber\":\"123asd\"}"))
                .andExpect(jsonPath("$.id",notNullValue()));
    }
    @Test
    public void newProduct_ko_JSON_vacio() throws Exception {
        //Given
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newProduct_ko_JSON_claves_JSON_no_corresponden_atributos() throws Exception {
        //Given
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"campoaleatorio1\":{\"id\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0},\"serialNumber\":\"123asd\"}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newProduct_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenThrow(new NullPointerException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"vehicle\":{\"id\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0},\"serialNumber\":\"123asd\"}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }
}


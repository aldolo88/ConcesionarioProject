package com.example.demo.controllers;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.*;
import com.example.demo.model.repositories.ProductRepository;
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

@WebMvcTest(ProductController.class)
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProductRepository productRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllProducts_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Product product = constructor.TestProduct();
        List<Product> allProducts = List.of(product);
        Mockito.when(productRepository.findAll()).thenReturn(allProducts);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/products")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].vehicle.pvp",is(product.getVehicle().getPvp())))
                .andExpect(jsonPath("$[0].vehicle.id.model",is(product.getVehicle().getId().getModel())))
                .andExpect(jsonPath("$[0].vehicle.id.colour",is(product.getVehicle().getId().getColour())))
                .andExpect(jsonPath("$[0].vehicle.id.horsePower",is(product.getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$[0].vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$[0].vehicle.id.provider.providerName",is(product.getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$[0].serialNumber",is(product.getSerialNumber())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllProducts_error() throws Exception {
        //Given
        Mockito.when(productRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/products")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneProduct_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Product product = constructor.TestProduct();
        Mockito.when(productRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(product));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/products/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.vehicle.pvp",is(product.getVehicle().getPvp())))
                .andExpect(jsonPath("$.vehicle.id.model",is(product.getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.vehicle.id.colour",is(product.getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.vehicle.id.horsePower",is(product.getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.vehicle.id.provider.providerName",is(product.getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.serialNumber",is(product.getSerialNumber())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneProduct_notFound() throws Exception {
        //Given
        Mockito.when(productRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/products/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newProduct_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Product product = constructor.TestProduct();
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.vehicle.pvp",is(product.getVehicle().getPvp())))
                .andExpect(jsonPath("$.vehicle.id.model",is(product.getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.vehicle.id.colour",is(product.getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.vehicle.id.horsePower",is(product.getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.vehicle.id.provider.providerName",is(product.getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.serialNumber",is(product.getSerialNumber())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newProduct_ko_null_attribute_JSON() throws Exception {
        //Given
        Product product = new TestClassConstructors().TestProduct();
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newProduct_ko_Internal_server_error() throws Exception {
        //Given
        Product product = new TestClassConstructors().TestProduct();
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putProduct_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Product product = constructor.TestProduct();
        Product modProduct = constructor.TestModProduct();
        Mockito.when(productRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(modProduct);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modProduct));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.vehicle.pvp",is(modProduct.getVehicle().getPvp())))
                .andExpect(jsonPath("$.vehicle.id.model",is(modProduct.getVehicle().getId().getModel())))
                .andExpect(jsonPath("$.vehicle.id.colour",is(modProduct.getVehicle().getId().getColour())))
                .andExpect(jsonPath("$.vehicle.id.horsePower",is(modProduct.getVehicle().getId().getHorsePower())))
                .andExpect(jsonPath("$.vehicle.id.type",is(constructor.getStringType())))
                .andExpect(jsonPath("$.vehicle.id.provider.providerName",is(modProduct.getVehicle().getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.serialNumber",is(modProduct.getSerialNumber())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putProduct_ko_null_attribute_JSON() throws Exception {
        //Given
        Product product = new TestClassConstructors().TestProduct();
        Mockito.when(productRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putProduct_ko_Internal_server_error() throws Exception {
        //Given
        Product product = new TestClassConstructors().TestProduct();
        Mockito.when(productRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putProduct_notFound() throws Exception {
        //Given
        Product product = new TestClassConstructors().TestProduct();
        Mockito.when(productRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(product));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneProduct_success() throws Exception {
        //Given
        Mockito.doNothing().when(productRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneProduct_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(productRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/products/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }
}


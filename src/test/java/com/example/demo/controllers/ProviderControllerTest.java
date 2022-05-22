package com.example.demo.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.Provider;
import com.example.demo.model.repositories.ProviderRepository;
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

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ProviderController.class)
public class ProviderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProviderRepository providerRepository;


    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllProviders_success() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        List<Provider> allProviders = List.of(provider);
        Mockito.when(providerRepository.findAll()).thenReturn(allProviders);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/providers")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].providerName",is(provider.getProviderName())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllProviders_error() throws Exception {
        //Given
        Mockito.when(providerRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/providers")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneProvider_success() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Mockito.when(providerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(provider));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/providers/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.providerName",is(provider.getProviderName())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneProvider_notFound() throws Exception {
        //Given
        Mockito.when(providerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/providers/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newProvider_success() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenReturn(provider);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(provider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.providerName",is(provider.getProviderName())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newProvider_ko_null_attribute_JSON() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(provider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newProvider_ko_Internal_server_error() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(provider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putProvider_success() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Provider modProvider = new TestClassConstructors().TestModProvider();
        Mockito.when(providerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(provider));
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenReturn(modProvider);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/providers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modProvider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.providerName",is(modProvider.getProviderName())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putProvider_ko_null_attribute_JSON() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Mockito.when(providerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(provider));
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/providers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(provider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putProvider_ko_Internal_server_error() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Mockito.when(providerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(provider));
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/providers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(provider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putProvider_notFound() throws Exception {
        //Given
        Provider provider = new TestClassConstructors().TestProvider();
        Mockito.when(providerRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/providers/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(provider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneProvider_success() throws Exception {
        //Given
        Mockito.doNothing().when(providerRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/providers/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneProvider_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(providerRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/providers/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }
}
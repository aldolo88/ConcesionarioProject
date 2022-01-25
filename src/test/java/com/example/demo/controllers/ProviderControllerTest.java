package com.example.demo.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
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

@WebMvcTest(ProviderController.class)
public class ProviderControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ProviderRepository providerRepository;


    @Test
    public void newProvider_success() throws Exception {
        //Given
        Provider provider = new Provider("Nombre1");
        Mockito.when(providerRepository.save(provider)).thenReturn(provider);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(provider));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.providerName",is("Nombre1")))
                .andExpect(jsonPath("$.id",notNullValue()));
    }
    @Test
    public void newProvider_ko_empty_JSON() throws Exception {
        //Given
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newProvider_ko_wrong_attribute_JSON() throws Exception {
        //Given
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"campoaleatorio\":\"Nombre1\"}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }
    @Test
    public void newProvider_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(providerRepository.save(Mockito.any(Provider.class))).thenThrow(new NullPointerException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/providers")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Nombre1\"}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }
}

package com.example.demo.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.example.demo.model.Vehicle;
import com.example.demo.model.VehicleId;
import com.example.demo.model.Provider;
import com.example.demo.model.repositories.VehicleRepository;
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

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    VehicleRepository vehicleRepository;


    @Test
    public void newVehicle_success() throws Exception {
        //Given
        Provider provider = new Provider("Provider1");
        VehicleId vehicleId = new VehicleId(provider,"modelo","color",100, VehicleId.Type.COCHE);
        Vehicle vehicle = new Vehicle(vehicleId, 15000);
        Mockito.when(vehicleRepository.save(vehicle)).thenReturn(vehicle);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(vehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(content().json("{\"id\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0}"))
                .andExpect(jsonPath("$.id",notNullValue()));
    }
    @Test
    public void newVehicle_ko_empty_JSON() throws Exception {
        //Given
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newVehicle_ko_wrong_attribute_JSON() throws Exception {
        //Given
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"campoaleatorio1\":{\"provider\":{\"id\":0,\"providerName\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newVehicle_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenThrow(new NullPointerException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"id\":{\"provider\":{\"id\":0,\"name\":\"Provider1\"},\"model\":\"modelo\",\"colour\":\"color\",\"horsePower\":100,\"type\":\"COCHE\"},\"pvp\":15000.0}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }
}


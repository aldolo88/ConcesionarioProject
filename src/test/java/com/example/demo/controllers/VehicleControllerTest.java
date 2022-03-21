package com.example.demo.controllers;

import static java.util.Collections.emptyList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.Vehicle;
import com.example.demo.model.VehicleId;
import com.example.demo.model.repositories.VehicleRepository;
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

@WebMvcTest(VehicleController.class)
public class VehicleControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    VehicleRepository vehicleRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllVehicles_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        List<Vehicle> allVehicles = List.of(vehicle);
        Mockito.when(vehicleRepository.findAll()).thenReturn(allVehicles);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/vehicles")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].pvp",is(vehicle.getPvp())))
                .andExpect(jsonPath("$[0].id.provider.providerName",is(vehicle.getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$[0].id.model",is(vehicle.getId().getModel())))
                .andExpect(jsonPath("$[0].id.colour",is(vehicle.getId().getColour())))
                .andExpect(jsonPath("$[0].id.horsePower",is(vehicle.getId().getHorsePower())))
                .andExpect(jsonPath("$[0].id.type",is(constructor.getStringType())));
    }

    @Test
    public void getAllVehicles_error() throws Exception {
        //Given
        Mockito.when(vehicleRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/vehicles")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneVehicle_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.when(vehicleRepository.findByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(), vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(), vehicle.getId().getStringType())).thenReturn(List.of(vehicle));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.pvp", is(vehicle.getPvp())))
                .andExpect(jsonPath("$.id.provider.providerName", is(vehicle.getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.id.model", is(vehicle.getId().getModel())))
                .andExpect(jsonPath("$.id.colour", is(vehicle.getId().getColour())))
                .andExpect(jsonPath("$.id.horsePower", is(vehicle.getId().getHorsePower())))
                .andExpect(jsonPath("$.id.type", is(constructor.getStringType())));
    }

    @Test
    public void getOneVehicle_notFound() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.when(vehicleRepository.findByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(),vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(),vehicle.getId().getStringType())).thenReturn(emptyList());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: " + "ProviderId-" + vehicle.getId().getProvider().getId() + " , model-" + vehicle.getId().getModel() + " colour-" + vehicle.getId().getColour() + " horsePower-" + vehicle.getId().getHorsePower() + " type-" + vehicle.getId().getStringType()));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newVehicle_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenReturn(vehicle);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(vehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.pvp", is(vehicle.getPvp())))
                .andExpect(jsonPath("$.id.provider.providerName", is(vehicle.getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.id.model", is(vehicle.getId().getModel())))
                .andExpect(jsonPath("$.id.colour", is(vehicle.getId().getColour())))
                .andExpect(jsonPath("$.id.horsePower", is(vehicle.getId().getHorsePower())))
                .andExpect(jsonPath("$.id.type", is(constructor.getStringType())));
    }

    @Test
    public void newVehicle_ko_null_attribute_JSON() throws Exception {
        //Given
        Vehicle vehicle = new TestClassConstructors().TestVehicle();
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(vehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newVehicle_ko_Internal_server_error() throws Exception {
        //Given
        Vehicle vehicle = new TestClassConstructors().TestVehicle();
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(vehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putVehicle_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Vehicle modVehicle = constructor.TestModVehicle();
        Mockito.when(vehicleRepository.findByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(), vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(), vehicle.getId().getStringType())).thenReturn(List.of(vehicle));
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenReturn(modVehicle);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modVehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.pvp", is(modVehicle.getPvp())))
                .andExpect(jsonPath("$.id.provider.providerName", is(modVehicle.getId().getProvider().getProviderName())))
                .andExpect(jsonPath("$.id.model", is(modVehicle.getId().getModel())))
                .andExpect(jsonPath("$.id.colour", is(modVehicle.getId().getColour())))
                .andExpect(jsonPath("$.id.horsePower", is(modVehicle.getId().getHorsePower())))
                .andExpect(jsonPath("$.id.type", is(constructor.getStringType())));
    }

    @Test
    public void putVehicle_ko_null_attribute_JSON() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.when(vehicleRepository.findByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(),vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(),vehicle.getId().getStringType())).thenReturn(List.of(vehicle));
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(vehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putVehicle_ko_Internal_server_error() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.when(vehicleRepository.findByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(),vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(),vehicle.getId().getStringType())).thenReturn(List.of(vehicle));
        Mockito.when(vehicleRepository.save(Mockito.any(Vehicle.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(vehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putVehicle_notFound() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.when(vehicleRepository.findByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(),vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(),vehicle.getId().getStringType())).thenReturn(emptyList());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(vehicle));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: " + "ProviderId-" + vehicle.getId().getProvider().getId() + " , model-" + vehicle.getId().getModel() + " colour-" + vehicle.getId().getColour() + " horsePower-" + vehicle.getId().getHorsePower() + " type-" + vehicle.getId().getStringType()));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneVehicle_success() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.doNothing().when(vehicleRepository).deleteByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(),vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(),vehicle.getId().getStringType());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: " + "ProviderId-" + vehicle.getId().getProvider().getId() + " , model-" + vehicle.getId().getModel() + " colour-" + vehicle.getId().getColour() + " horsePower-" + vehicle.getId().getHorsePower() + " type-" + vehicle.getId().getStringType()));
    }

    @Test
    public void deleteOneVehicle_error() throws Exception {
        //Given
        TestClassConstructors constructor = new TestClassConstructors();
        Vehicle vehicle = constructor.TestVehicle();
        Mockito.doThrow(new IllegalArgumentException("error")).when(vehicleRepository).deleteByVehicleId(vehicle.getId().getProvider().getId(),
                vehicle.getId().getModel(),vehicle.getId().getColour(),
                vehicle.getId().getHorsePower(),vehicle.getId().getStringType());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/vehicles/"+vehicle.getId().getProvider().getId()+"/"+vehicle.getId().getModel()+"/"+vehicle.getId().getColour()+"/"+vehicle.getId().getHorsePower()+"/"+vehicle.getId().getStringType())
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: " + "ProviderId-" + vehicle.getId().getProvider().getId() + " , model-" + vehicle.getId().getModel() + " colour-" + vehicle.getId().getColour() + " horsePower-" + vehicle.getId().getHorsePower() + " type-" + vehicle.getId().getStringType()));
    }
}
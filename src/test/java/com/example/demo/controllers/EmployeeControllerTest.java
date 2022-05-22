package com.example.demo.controllers;

import com.example.demo.aux.TestClassConstructors;
import com.example.demo.model.Employee;
import com.example.demo.model.repositories.EmployeeRepository;
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
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    EmployeeRepository employeeRepository;

    //GET--------------------------------------------------------------------------------------------------------------
    @Test
    public void getAllEmployees_success() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        List<Employee> allEmployees = List.of(employee);
        Mockito.when(employeeRepository.findAll()).thenReturn(allEmployees);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name",is(employee.getName())))
                .andExpect(jsonPath("$[0].role",is(employee.getRole())))
                .andExpect(jsonPath("$[0].id",notNullValue()));
    }

    @Test
    public void getAllEmployees_error() throws Exception {
        //Given
        Mockito.when(employeeRepository.findAll()).thenThrow(new NullPointerException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/employees")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void getOneEmployee_success() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Mockito.when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(employee));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is(employee.getName())))
                .andExpect(jsonPath("$.role",is(employee.getRole())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void getOneEmployee_notFound() throws Exception {
        //Given
        Mockito.when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/employees/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //POST-------------------------------------------------------------------------------------------------------------
    @Test
    public void newEmployee_success() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(employee);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(employee));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is(employee.getName())))
                .andExpect(jsonPath("$.role",is(employee.getRole())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void newEmployee_ko_null_attribute_JSON() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(employee));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void newEmployee_ko_Internal_server_error() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(employee));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    //PUT--------------------------------------------------------------------------------------------------------------
    @Test
    public void putEmployee_success() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Employee modEmployee = new TestClassConstructors().TestModEmployee();
        Mockito.when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenReturn(modEmployee);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(modEmployee));
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.name",is(modEmployee.getName())))
                .andExpect(jsonPath("$.role",is(modEmployee.getRole())))
                .andExpect(jsonPath("$.id",notNullValue()));
    }

    @Test
    public void putEmployee_ko_null_attribute_JSON() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Mockito.when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenThrow(new ConstraintViolationException("error", null));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(employee));
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }

    @Test
    public void putEmployee_ko_Internal_server_error() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Mockito.when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.of(employee));
        Mockito.when(employeeRepository.save(Mockito.any(Employee.class))).thenThrow(new IllegalArgumentException("error"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(employee));
        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }

    @Test
    public void putEmployee_notFound() throws Exception {
        //Given
        Employee employee = new TestClassConstructors().TestEmployee();
        Mockito.when(employeeRepository.findById(Mockito.any(Long.class))).thenReturn(Optional.empty());
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(employee));
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }

    //DELETE-----------------------------------------------------------------------------------------------------------
    @Test
    public void deleteOneEmployee_success() throws Exception {
        //Given
        Mockito.doNothing().when(employeeRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/employees/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isAccepted())
                .andExpect(content().string("Deleted Id: 1"));
    }

    @Test
    public void deleteOneEmployee_error() throws Exception {
        //Given
        Mockito.doThrow(new IllegalArgumentException("error")).when(employeeRepository).deleteById(Mockito.any(Long.class));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.delete("/employees/1")
                .contentType(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isNotFound())
                .andExpect(content().string("Id not found: 1"));
    }
}

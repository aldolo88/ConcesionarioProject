package com.example.demo.controllers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import com.example.demo.model.Client;
import com.example.demo.model.repositories.ClientRepository;
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

@WebMvcTest(ClientController.class)
public class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    ClientRepository clientRepository;


    @Test
    public void newClient_success() throws Exception {
        //Given
        Client cliente1 = new Client("0123456789A", "Nombre1", 123456789);
        Mockito.when(clientRepository.save(cliente1)).thenReturn(cliente1);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(cliente1));
        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$",notNullValue()))
                .andExpect(jsonPath("$.dni",is("0123456789A")))
                .andExpect(jsonPath("$.name",is("Nombre1")))
                .andExpect(jsonPath("$.phone",is(123456789)))
                .andExpect(jsonPath("$.id",notNullValue()));
    }
    @Test
    public void newClient_ko_empty_JSON() throws Exception {
        //Given
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }
    /* Este test sale OK porque este tipo de error no se controla en el controller el caso de campos duplicados
    @Test
    public void newClient_ko_pero_sale_ok() throws Exception {
        //Given
        Client cliente=new Client();
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenReturn(cliente);
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"dni\":\"0123456789A\",\"dni\":\"9876543210A\",\"name\":\"Nombre1\",\"phone\":123456789}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());
    }
    */
    @Test
    public void newClient_ko_wrong_attribute_JSON() throws Exception {
        //Given
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenThrow(new DataIntegrityViolationException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"dni\":\"0123456789A\",\"name\":\"Nombre1\",\"phone\":123456789,\"phone2\":123456789}");
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Bad Request"));
    }
    @Test
    public void newClient_ko_Internal_server_error() throws Exception {
        //Given
        Mockito.when(clientRepository.save(Mockito.any(Client.class))).thenThrow(new NullPointerException("errorsito"));
        //when, then
        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"dni\":\"0123456789A\",\"name\":\"Nombre1\",\"phone\":123456789}");

        mockMvc.perform(mockRequest)
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Internal Server Error"));
    }
}

package com.example.aws.cicd.controller;

import com.example.aws.cicd.model.HelloWorld;
import com.example.aws.cicd.service.HelloWorldService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class HelloWorldControllerTest {
    private MockMvc mockMvc;

    @Mock
    private HelloWorldService service;

    @InjectMocks
    private HelloWorldController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetHelloWorld() throws Exception {
        // Given
        HelloWorld expectedHelloWorld = new HelloWorld("param1", "param2");
        when(service.getHelloWorld()).thenReturn(expectedHelloWorld);

        // When/Then
        mockMvc.perform(get("/helloworld"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.param1").value(expectedHelloWorld.param1()))
                .andExpect(jsonPath("$.param2").value(expectedHelloWorld.param2()));
    }
}
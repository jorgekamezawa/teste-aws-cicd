package com.example.aws.cicd.service;

import com.example.aws.cicd.model.HelloWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloWorldServiceTest {
    private HelloWorldService service;

    @BeforeEach
    void setup() {
        service = new HelloWorldService();
    }

    @Test
    void testGetHelloWorld() {
        // Given
        HelloWorld expectedHelloWorld = new HelloWorld("Hello", "New World 2");

        // When
        HelloWorld actualHelloWorld = service.getHelloWorld();

        // Then
        assertEquals(expectedHelloWorld, actualHelloWorld);
    }
}
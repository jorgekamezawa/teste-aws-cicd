package com.example.aws.cicd.service;

import com.example.aws.cicd.model.HelloWorld;
import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {

    public HelloWorld getHelloWorld() {
        return new HelloWorld("Hello", "New World 2");
    }
}

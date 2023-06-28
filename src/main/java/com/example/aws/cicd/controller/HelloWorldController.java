package com.example.aws.cicd.controller;

import com.example.aws.cicd.model.HelloWorld;
import com.example.aws.cicd.service.HelloWorldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/helloworld")
@RequiredArgsConstructor
public class HelloWorldController {

    private final HelloWorldService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public HelloWorld getHelloWrold() {
        return service.getHelloWorld();
    }
}

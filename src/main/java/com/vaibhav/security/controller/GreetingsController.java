package com.vaibhav.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class GreetingsController {

    @GetMapping("/hello")
    public String greetings(){
        log.info("Login Success| Hitting Hello endpoint");
        return "Hello! Greetings from Spring Security";
    }
}

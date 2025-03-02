package com.vaibhav.security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class GreetingsController {

    @GetMapping("/hello")
    public String greetings(){
        log.info("Login Success| Hitting Hello endpoint");
        return "Hello! Greetings from Spring Security";
    }

    @PreAuthorize("hasRole('ROLE_READONLY')")
    @GetMapping("/user")
    public String userEndpoint(){
        return "Hello, User!";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String acessAdminPage(){
        log.info("Accessing Admin URL");
        return "Hello Admin! Greeting from Spring Security";
    }
}

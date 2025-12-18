package com.deeba.botpersona.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationController {
    @GetMapping("/hello")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}

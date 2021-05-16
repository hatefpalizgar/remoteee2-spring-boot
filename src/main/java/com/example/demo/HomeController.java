package com.example.demo;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/home")
public class HomeController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Welcome";
    }

    @PostMapping("/new")
    public String createNewHome(@RequestParam(name = "name") String name) {
        return "New home created with name " + name;
    }
}

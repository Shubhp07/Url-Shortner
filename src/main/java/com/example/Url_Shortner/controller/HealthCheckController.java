package com.example.Url_Shortner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class HealthCheckController {

    

    @GetMapping("/ping")
    @ResponseBody
    public String ping() {
        return "pong";
    }
    
}

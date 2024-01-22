package com.example.Triple_clone.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GoogleMapController {
    @GetMapping("/showMap")
    public String index() {
        return "map";
    }
}

package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
public class ControllerTest {
    
    @GetMapping("/")
    public String getMethodName() {
        return "index";
    }
    

    
}

package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexAdmController {
    
    @GetMapping("/indexAdm")
    public ModelAndView indexAdm (HttpSession session) {
        return new ModelAndView("indexAdm");
    }

}
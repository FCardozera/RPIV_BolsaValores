package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexLogadoController {
    
    @GetMapping("/indexLogado")
    public ModelAndView indexLogado (HttpSession session) {
        ModelAndView mv = new ModelAndView("indexLogado");

        return mv;
    }

}
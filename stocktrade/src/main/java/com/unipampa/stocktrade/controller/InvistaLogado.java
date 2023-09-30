package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/invistaLogado")
public class InvistaLogado {
    
    @GetMapping
    public ModelAndView invistaLogado () {
        ModelAndView mv = new ModelAndView("invistaLogado");
        return mv;
    }

}

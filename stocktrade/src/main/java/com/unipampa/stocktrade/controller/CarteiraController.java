package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/carteira")
public class CarteiraController {
    
    @GetMapping
    public ModelAndView carteiraPagina(HttpSession session) {
        ModelAndView mv = new ModelAndView("/carteira");
        return mv;
    }

}
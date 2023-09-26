package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/perfil")
public class PerfilController {
    
    @GetMapping
    public ModelAndView perfilPagina(HttpSession session) {
        ModelAndView mv = new ModelAndView("/perfil");
        return mv;
    }

}

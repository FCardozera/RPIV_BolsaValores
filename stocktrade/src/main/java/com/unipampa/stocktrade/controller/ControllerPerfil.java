package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/perfil")
public class ControllerPerfil {
    
    @GetMapping
    public ModelAndView perfilPagina() {
        ModelAndView mv = new ModelAndView("/perfil");
        return mv;
    }

}

package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/contatoLogado")
public class ContatoLogadoController {
    
    @GetMapping
    public ModelAndView contatoPagina(HttpSession session) {
        return new ModelAndView("/contatoLogado");
    }

}

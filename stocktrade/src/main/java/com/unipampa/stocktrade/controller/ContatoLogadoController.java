package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/contatoLogado")
public class ContatoLogadoController {
    
    @GetMapping
    public ModelAndView contatoPagina() {
        ModelAndView mv = new ModelAndView("/contatoLogado");
        return mv;
    }

}

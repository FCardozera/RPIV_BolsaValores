package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ControllerContato {
    
    @GetMapping("/contato")
    public ModelAndView contatoPagina() {
        ModelAndView mv = new ModelAndView("/contato");
        return mv;
    }

}

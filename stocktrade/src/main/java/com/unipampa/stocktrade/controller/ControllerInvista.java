package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ControllerInvista {
    
    @GetMapping("/invista")
    public ModelAndView invistaPagina() {
        ModelAndView mv = new ModelAndView("/invista");
        return mv;
    }

}

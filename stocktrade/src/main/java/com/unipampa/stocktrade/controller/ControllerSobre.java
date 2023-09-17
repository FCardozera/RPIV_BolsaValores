package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class ControllerSobre {
    
    @GetMapping("/sobre")
    public ModelAndView sobrePagina() {
        ModelAndView mv = new ModelAndView("/sobre");
        return mv;
    }

}

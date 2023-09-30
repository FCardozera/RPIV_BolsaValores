package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.service.ServiceInvistaLogado;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/invistaLogado")
public class InvistaLogado {

    @Autowired
    private ServiceInvistaLogado service;

    @GetMapping
    public ModelAndView invistaLogado (HttpSession session) {
        ModelAndView mv = new ModelAndView("invistaLogado");
        mv.addObject("acoes", service.getAcoesSiglaPreco());
        return mv;
    }

}

package com.unipampa.stocktrade.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.service.ServiceIndex;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/error404")
public class Controller404 {

    @Autowired
    private ServiceIndex service;
    
    @GetMapping
    public ModelAndView error404(HttpSession session) {
        ModelAndView mv = new ModelAndView("404");

        List<Acao> acoes = service.buscaAcoes();
        mv.addObject("acoes", acoes);

        return mv;
    }

}


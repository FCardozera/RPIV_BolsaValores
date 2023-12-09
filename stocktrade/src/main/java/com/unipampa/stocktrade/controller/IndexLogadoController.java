package com.unipampa.stocktrade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.service.IndexLogadoService;

import jakarta.servlet.http.HttpSession;

@Controller
public class IndexLogadoController {


    @Autowired
    private IndexLogadoService indexLogadoService;

    @GetMapping("/indexLogado")
    public ModelAndView indexLogado(HttpSession session) {

        ModelAndView mv = new ModelAndView("/indexLogado");

        List<String[]> acoesUsuario = indexLogadoService.getAcoesUser(session);
        mv.addObject("acoesUsuario", acoesUsuario);

        return mv;

    }

}
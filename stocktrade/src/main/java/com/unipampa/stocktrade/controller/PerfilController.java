package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.service.PerfilService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PerfilService service;

    @GetMapping
    public ModelAndView perfilPagina(HttpSession session) {
        ModelAndView mv = new ModelAndView("/perfil");
        return mv;
    }

    @DeleteMapping
    public ModelAndView deleteConta(HttpSession session, @RequestBody ClienteRequestDTO dados) {
        ModelAndView mv = new ModelAndView("/index");
        service.deleteConta(session, dados);
        return mv;
    }

    @PutMapping("/trocar-email")
    public ModelAndView trocarEmail(HttpSession session, @RequestBody ClienteRequestDTO dados) {
         
        service.trocarEmail(session, dados);
        return null;   
    }

    @PutMapping("/trocar-senha")
    public ModelAndView trocarSenha(HttpSession session, @RequestBody ClienteRequestDTO dados) {
         
        service.trocarSenha(session, dados);
        return null;   
    }

}

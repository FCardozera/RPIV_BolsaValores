package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return new ModelAndView("/perfil");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteConta(HttpSession session, @RequestBody ClienteRequestDTO dados) {
        return service.deleteConta(session, dados);
    }

    @PutMapping("/trocar-email")
    public ResponseEntity<String> trocarEmail(HttpSession session, @RequestBody ClienteRequestDTO dados) {
        return service.trocarEmail(session, dados); 
    }

    @PutMapping("/trocar-senha")
    public ResponseEntity<String> trocarSenha(HttpSession session, @RequestBody ClienteRequestDTO dados) {
        return service.trocarSenha(session, dados); 
    }

}

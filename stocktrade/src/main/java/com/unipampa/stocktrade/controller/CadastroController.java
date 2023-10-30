package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.service.CadastroService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cadastro")
public class CadastroController {

    @Autowired
    private CadastroService serviceCadastro;

    @PostMapping
    public ModelAndView cadastrarUsuario(@RequestBody ClienteRequestDTO dados) {
        ModelAndView mv = new ModelAndView("/cadastro");
        try {
            serviceCadastro.salvarCliente(dados);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return mv;
    }

    @GetMapping
    public ModelAndView cadastroPagina(HttpSession session) {
        return new ModelAndView("cadastro");
    }
}

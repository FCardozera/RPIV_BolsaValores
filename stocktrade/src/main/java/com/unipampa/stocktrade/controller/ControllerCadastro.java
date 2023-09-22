package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.service.ServiceCadastro;

@Controller
@RequestMapping("/cadastro")
public class ControllerCadastro {

    @Autowired
    private ServiceCadastro serviceCadastro;

    @PostMapping
    public ModelAndView cadastrarUsuario(@RequestBody UsuarioRequestDTO dados) throws Exception {
        ModelAndView mv = new ModelAndView("/cadastro");
        serviceCadastro.salvarUsuario(dados);
        return mv;
    }

    @GetMapping
    public ModelAndView cadastroPagina() {
        ModelAndView mv = new ModelAndView("/cadastro");
        return mv;
    }
}

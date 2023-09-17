package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.service.ServiceLogin;

@Controller
public class ControllerLogin {
    
    @Autowired
    private ServiceLogin serviceLogin;

    @PostMapping("/login")
    public ModelAndView login(@RequestBody UsuarioRequestDTO dados) throws Exception {
        ModelAndView mv = new ModelAndView("/login");
        serviceLogin.login(dados);
        return mv;
    }

    @GetMapping("/login")
    public ModelAndView loginPagina() {
        ModelAndView mv = new ModelAndView("/login");
        return mv;
    }

}

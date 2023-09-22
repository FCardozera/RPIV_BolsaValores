package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.service.ServiceLogin;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class ControllerLogin {
    
    @Autowired
    private ServiceLogin serviceLogin;

    @PostMapping
    public ModelAndView login(@RequestBody UsuarioRequestDTO dados, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView("/login");
        Usuario usuario = serviceLogin.login(dados);
        session.setAttribute("usuarioLogado", usuario);
        return mv;
    }

    @GetMapping
    public ModelAndView loginPagina() {
        ModelAndView mv = new ModelAndView("/login");
        return mv;
    }

}

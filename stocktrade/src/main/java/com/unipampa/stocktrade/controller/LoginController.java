package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/login")
public class LoginController {
    
    @Autowired
    private LoginService serviceLogin;

    @PostMapping
    public ModelAndView login(@RequestBody UsuarioRequestDTO dados, HttpSession session) throws Exception {
        ModelAndView mv = new ModelAndView();

        try {
            Usuario userLogin = serviceLogin.login(dados, session);
            mv.addObject("usuario", userLogin);
            mv.setViewName("indexLogado");
        } catch (Exception e) {
            throw e;
        }
        
        return mv;
    }

    @PutMapping
    public ModelAndView logout(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        serviceLogin.logout(session);
        mv.setViewName("index");
        return mv;
    }

    @GetMapping
    public ModelAndView loginPagina(HttpSession session) {
        ModelAndView mv = new ModelAndView("login");
        return mv;
    }

}

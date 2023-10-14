package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.padroes.CPFFormatDecorator;
import com.unipampa.stocktrade.model.entity.usuario.padroes.interfaces.UsuarioDecorator;


import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/infoPerfil")
public class InfoPerfilController {


    @GetMapping
    public ModelAndView index(HttpSession session) {
        ModelAndView mv = new ModelAndView("infoPerfil");


        try {
            Usuario usuario = (Usuario)session.getAttribute("usuarioLogado");
            UsuarioDecorator usuarioDecorado = new CPFFormatDecorator(usuario);

            mv.addObject("nome", usuarioDecorado.getNome());
            mv.addObject("email", usuarioDecorado.getEmail());
            mv.addObject("cpf", usuarioDecorado.getCPF());

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return mv;
    }
}

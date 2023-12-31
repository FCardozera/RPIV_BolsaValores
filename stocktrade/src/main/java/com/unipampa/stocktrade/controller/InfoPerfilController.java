package com.unipampa.stocktrade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.decorator.UsuarioDecorator;
import com.unipampa.stocktrade.model.entity.usuario.decorator.interfaces.IUsuarioDecorator;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/infoPerfil")
public class InfoPerfilController {


    @GetMapping
    public ModelAndView index(HttpSession session) {
        ModelAndView mv = new ModelAndView("infoPerfil");
        
        try {
            Usuario usuario = (Usuario)session.getAttribute("usuarioLogado");
            Cliente cliente = (Cliente) usuario;
            IUsuarioDecorator usuarioDecorado = new UsuarioDecorator(cliente);

            mv.addObject("nome", usuarioDecorado.getNome());
            mv.addObject("email", usuarioDecorado.getEmail());
            mv.addObject("cpf", usuarioDecorado.getCPF());

        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        return mv;
    }
}

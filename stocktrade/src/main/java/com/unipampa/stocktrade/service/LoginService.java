package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(UsuarioRequestDTO dados, HttpSession session) {
        Usuario usuario = usuarioRepository.findByEmail(dados.email());
            
        if (usuario == null || !usuario.isSenhaCorreta(dados.senha())) {
            throw new RuntimeException("Email e/ou senha incorretos");
        }

        session.setAttribute("usuarioLogado", usuario);

        return usuario;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}

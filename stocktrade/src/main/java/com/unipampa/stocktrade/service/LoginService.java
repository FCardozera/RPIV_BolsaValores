package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.exception.usuario.SenhaIncorretaException;
import com.unipampa.stocktrade.model.repository.usuario.AdminRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AdminRepository adminRepository;

    public Usuario login(ClienteRequestDTO dados, HttpSession session) {
        Usuario usuario = null;
    
        try {
            usuario = clienteRepository.findByEmail(dados.email());

            if (usuario == null || !usuario.isSenhaCorreta(dados.senha())) {
                throw new SenhaIncorretaException("Email e/ou senha incorretos");
            }

            session.setAttribute("usuarioLogado", (Cliente) usuario);
        } catch (Exception e) {
            usuario = adminRepository.findByEmail(dados.email());

            if (usuario == null || !usuario.isSenhaCorreta(dados.senha())) {
                throw e;
            }

            session.setAttribute("usuarioLogado", usuario);
        }
    
        return usuario;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}

package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class LoginService {
    
    @Autowired
    private ClienteRepository clienteRepository;

    public Usuario login(ClienteRequestDTO dados, HttpSession session) {
        Cliente cliente = clienteRepository.findByEmail(dados.email());
            
        if (cliente == null || !cliente.isSenhaCorreta(dados.senha())) {
            throw new RuntimeException("Email e/ou senha incorretos");
        }

        session.setAttribute("usuarioLogado", cliente);

        return cliente;
    }

    public void logout(HttpSession session) {
        session.invalidate();
    }
}

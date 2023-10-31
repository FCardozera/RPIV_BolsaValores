package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.FactoryMethod.UsuarioFactory;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

@Service
public class CadastroService {

    @Autowired
    private ClienteRepository clienteRepository;

    public void salvarCliente(ClienteRequestDTO dados) {

        Cliente cliente = clienteRepository.findByEmail(dados.email());

        if (cliente != null) {
            throw new RuntimeException("Já existe um usuário cadastrado para o e-mail: " + dados.email());
        }

        cliente = (Cliente) UsuarioFactory.novoUsuario(dados);
        clienteRepository.save(cliente);
    }
    
}

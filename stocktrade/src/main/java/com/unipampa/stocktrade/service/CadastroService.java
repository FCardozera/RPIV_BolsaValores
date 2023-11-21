package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.FactoryMethod.UsuarioFactory;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;
import com.unipampa.stocktrade.service.exception_handler.ExceptionHandlerChain;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

@Service
public class CadastroService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RegistroRepository registroRepository;

    public ResponseEntity<String> salvarCliente(ClienteRequestDTO dados) {
        Cliente cliente = clienteRepository.findByEmail(dados.email());

        if (cliente != null) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.USUARIO_JA_EXISTENTE, null, registroRepository));
        }

        cliente = (Cliente) UsuarioFactory.novoUsuario(dados);
        clienteRepository.save(cliente);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
    
}

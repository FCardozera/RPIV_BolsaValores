package com.unipampa.stocktrade.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

@Service
public class ServiceLogin {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void login(UsuarioRequestDTO dados) throws Exception{
        try {
            if (usuarioRepository.findByEmail(dados.email()) == null) {
                throw new Exception("Não existe um usuário cadastrado para o e-mail: " + dados.email());
            }
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Erro na criptografia da senha");
        }
    }

}

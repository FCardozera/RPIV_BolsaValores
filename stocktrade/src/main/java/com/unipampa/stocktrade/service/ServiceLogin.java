package com.unipampa.stocktrade.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

@Service
public class ServiceLogin {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(UsuarioRequestDTO dados) throws Exception{
        try {

            Usuario usuario = usuarioRepository.findByEmail(dados.email());
            
            if (usuario == null || !usuario.isSenhaCorreta(dados.senha())) {
                throw new Exception("Email e/ou senha incorretos");
            }

            return usuario;

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Erro na criptografia da senha");
        }
    }

}

package com.unipampa.stocktrade.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;
import com.unipampa.stocktrade.util.Util;

@Service
public class ServiceLogin {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario login(UsuarioRequestDTO dados) throws Exception{
        try {
            
            if (usuarioRepository.findByEmail(dados.email()) == null) {
                throw new Exception("Não existe um usuário cadastrado para o e-mail: " + dados.email());
            }

            Usuario usuario = usuarioRepository.findByEmail(dados.email());
            String hashSenha = Util.sha256(dados.senha(), usuario.getSaltSenha());

            if (!usuario.getHashSenha().equals(hashSenha)) {
                throw new Exception("Email e/ou senha incorretos");
            }

            return usuario;

        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Erro na criptografia da senha");
        }
    }

}

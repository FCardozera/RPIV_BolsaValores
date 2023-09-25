package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

@Service
public class CadastroService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void salvarUsuario(UsuarioRequestDTO dados) {

        Usuario usuario = usuarioRepository.findByEmail(dados.email());

        if (usuario != null) {
            throw new RuntimeException("Já existe um usuário cadastrado para o e-mail: " + dados.email());
        }

        usuario = new Usuario(dados);
        usuarioRepository.save(usuario);
    }
    
}

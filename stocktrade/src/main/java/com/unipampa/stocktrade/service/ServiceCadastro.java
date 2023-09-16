package com.unipampa.stocktrade.service;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

@Service
public class ServiceCadastro {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void salvarUsuario(UsuarioRequestDTO dados) throws Exception{
        try {
            if (usuarioRepository.findByEmail(dados.email()) != null) {
                throw new Exception("Já existe um usuário cadastrado para o e-mail: " + dados.email());
            }
            UsuarioRequestDTO novosDados = new UsuarioRequestDTO(dados.nome(), dados.cpf(), dados.email(), dados.senha(), dados.senhaAutenticacao());
            Usuario user = new Usuario(novosDados);
            usuarioRepository.save(user);
        } catch (NoSuchAlgorithmException e) {
            throw new Exception("Erro na criptografia da senha");
        }
    }
}

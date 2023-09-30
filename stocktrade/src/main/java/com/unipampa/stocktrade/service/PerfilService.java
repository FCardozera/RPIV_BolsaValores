package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class PerfilService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void deleteConta(HttpSession session, UsuarioRequestDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }

        if (!usuario.isSenhaCorreta(dados.senha())) {
            throw new RuntimeException("Senha incorreta");
        }

        if (!usuario.getAcoes().isEmpty()) {
            throw new RuntimeException("Não é possível deletar a conta pois existem ações vinculadas a ela");
        }

        usuarioRepository.delete(usuario);
        session.invalidate();

        System.out.println("Usuário deletado com sucesso");
    }
    
}
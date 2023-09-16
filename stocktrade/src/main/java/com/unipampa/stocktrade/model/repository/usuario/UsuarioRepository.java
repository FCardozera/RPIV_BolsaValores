package com.unipampa.stocktrade.model.repository.usuario;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unipampa.stocktrade.model.entity.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    @Query("select i from usuario i where i.email = :email")
    public Usuario findByEmail(String email);

    @Query("select j from usuario j where j.email = :email and j.hashSenha = :hashSenha")
    public Usuario buscarLogin(String email, String hashSenha);
}
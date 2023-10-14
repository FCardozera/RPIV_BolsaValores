package com.unipampa.stocktrade.model.repository.usuario;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    @Query("select i from cliente i where i.email = :email")
    public Cliente findByEmail(String email);

    @Query("select j from cliente j where j.email = :email and j.hashSenha = :hashSenha")
    public Cliente buscarLogin(String email, String hashSenha);
}
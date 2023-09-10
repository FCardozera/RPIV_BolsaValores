package com.unipampa.stocktrade.model.repository.cliente;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unipampa.stocktrade.model.entity.usuario.cliente.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    @Query("select i from cliente i where i.email = :email")
    public Cliente findByEmail(String email);

    @Query("select j from cliente j where j.email = :email and j.senha = :senha")
    public Cliente buscarLogin(String email, String senha);
}
package com.unipampa.stocktrade.model.repository.usuario;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.model.entity.usuario.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

}
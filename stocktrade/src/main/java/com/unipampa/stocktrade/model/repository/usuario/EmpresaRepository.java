package com.unipampa.stocktrade.model.repository.usuario;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.model.entity.usuario.Empresa;


public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

}
package com.unipampa.stocktrade.model.repository.empresa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.model.entity.empresa.Empresa;


public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

}
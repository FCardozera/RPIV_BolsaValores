package com.unipampa.stocktrade.repository.empresa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unipampa.stocktrade.domain.entity.empresa.Empresa;


public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {

}
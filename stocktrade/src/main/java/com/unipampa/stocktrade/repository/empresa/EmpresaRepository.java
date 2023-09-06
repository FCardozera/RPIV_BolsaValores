package com.unipampa.stocktrade.repository.empresa;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unipampa.stocktrade.domain.entity.empresa.Empresa;


public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

}
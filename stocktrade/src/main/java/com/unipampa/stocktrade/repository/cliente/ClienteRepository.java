package com.unipampa.stocktrade.repository.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unipampa.stocktrade.domain.entity.usuario.cliente.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
package com.unipampa.stocktrade.repository.cliente;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unipampa.stocktrade.domain.entity.usuario.cliente.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

}
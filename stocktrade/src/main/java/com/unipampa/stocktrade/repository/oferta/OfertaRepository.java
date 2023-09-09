package com.unipampa.stocktrade.repository.oferta;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.domain.entity.oferta.Oferta;

public interface OfertaRepository extends JpaRepository<Oferta, UUID> {

}
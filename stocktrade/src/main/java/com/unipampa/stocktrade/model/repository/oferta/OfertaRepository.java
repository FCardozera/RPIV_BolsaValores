package com.unipampa.stocktrade.model.repository.oferta;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.model.entity.oferta.Oferta;

public interface OfertaRepository extends JpaRepository<Oferta, UUID> {

}
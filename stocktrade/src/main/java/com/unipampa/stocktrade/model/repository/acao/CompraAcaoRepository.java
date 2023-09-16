package com.unipampa.stocktrade.model.repository.acao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.model.entity.acao.CompraAcao;

public interface CompraAcaoRepository extends JpaRepository<CompraAcao, UUID> {

}
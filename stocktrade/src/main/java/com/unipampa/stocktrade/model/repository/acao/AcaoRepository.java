package com.unipampa.stocktrade.model.repository.acao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public interface AcaoRepository extends JpaRepository<Acao, UUID> {

}
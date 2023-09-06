package com.unipampa.stocktrade.repository.acao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.domain.entity.acao.Acao;

public interface AcaoRepository extends JpaRepository<Acao, UUID> {

}
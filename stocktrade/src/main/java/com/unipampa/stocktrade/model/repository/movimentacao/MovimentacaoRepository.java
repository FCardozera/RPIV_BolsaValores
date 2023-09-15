package com.unipampa.stocktrade.model.repository.movimentacao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, UUID> {

}
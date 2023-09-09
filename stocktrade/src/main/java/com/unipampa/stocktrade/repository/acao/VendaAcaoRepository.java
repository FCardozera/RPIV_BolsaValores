package com.unipampa.stocktrade.repository.acao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unipampa.stocktrade.domain.entity.acao.VendaAcao;

public interface VendaAcaoRepository extends JpaRepository<VendaAcao, UUID> {

}
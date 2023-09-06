package com.unipampa.stocktrade.repository.movimentacao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.unipampa.stocktrade.domain.entity.movimentacao.Movimentacao;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, UUID> {

}
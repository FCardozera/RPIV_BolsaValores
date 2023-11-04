package com.unipampa.stocktrade.model.repository.acao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unipampa.stocktrade.model.entity.acao.CompraAcao;

public interface CompraAcaoRepository extends JpaRepository<CompraAcao, UUID> {
    @Query("SELECT ca FROM compraAcao ca WHERE ca.acao.id = :acaoId")
    public CompraAcao findByAcaoId(@Param("acaoId") UUID acaoId);
}
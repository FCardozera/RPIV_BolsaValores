package com.unipampa.stocktrade.model.repository.acao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unipampa.stocktrade.model.entity.acao.VendaAcao;

public interface VendaAcaoRepository extends JpaRepository<VendaAcao, UUID> {
    @Query("SELECT va FROM vendaAcao va WHERE va.acao.id = :acaoId")
    public VendaAcao findByAcaoId(@Param("acaoId") UUID acaoId);
}
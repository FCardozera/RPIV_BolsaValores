package com.unipampa.stocktrade.model.repository.acao;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public interface AcaoRepository extends JpaRepository<Acao, UUID> {

    @Query("SELECT sigla, MIN(valor) FROM acao GROUP BY sigla")
    public List<String[]> findAcoesSiglaPreco();

}
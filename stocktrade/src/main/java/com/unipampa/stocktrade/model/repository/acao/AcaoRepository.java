package com.unipampa.stocktrade.model.repository.acao;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public interface AcaoRepository extends JpaRepository<Acao, UUID> {

    @Query("SELECT sigla, MIN(valor) FROM acao GROUP BY sigla")
    public List<String[]> findAcoesSiglaPreco();

    @Query("SELECT a.sigla, COUNT(a.sigla), MIN(a.valor), ROUND(AVG(ca.valorCompra), 2) FROM acao a INNER JOIN compraAcao ca WHERE ca.cliente.id = :cliente_id GROUP BY a.sigla")
    public List<String[]> findAcoesCliente(@Param("cliente_id") UUID cliente_id);
}
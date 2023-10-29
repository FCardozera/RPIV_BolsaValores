package com.unipampa.stocktrade.model.repository.acao;

import java.util.UUID;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public interface AcaoRepository extends JpaRepository<Acao, UUID> {

    @Query("SELECT COUNT(*) FROM acao WHERE sigla = :siglaAcao GROUP BY sigla")
    public Integer findAcoesSigla(@Param("siglaAcao") String siglaAcao);

    @Query("SELECT a.sigla, COUNT(a.sigla), ROUND(AVG(ca.valorCompra), 2) FROM acao a JOIN a.compraAcao ca WHERE ca.cliente.id = :cliente_id GROUP BY a.sigla")
    public List<String[]> findAcoesCliente(@Param("cliente_id") UUID cliente_id);

    @Query("SELECT COUNT(*) FROM acao a JOIN a.cliente c WHERE sigla = :siglaAcao AND c.id = :clienteId GROUP BY sigla")
    public Integer findQntAcoesBySiglaClienteId(@Param("siglaAcao") String siglaAcao, @Param("clienteId") UUID clienteId);

    @Query("SELECT a FROM acao a JOIN a.cliente c WHERE sigla = :siglaAcao AND c.id = :clienteId")
    public List<Acao> findAcoesClienteByClienteIdSigla(@Param("clienteId") UUID clienteId, @Param("siglaAcao") String siglaAcao);
}
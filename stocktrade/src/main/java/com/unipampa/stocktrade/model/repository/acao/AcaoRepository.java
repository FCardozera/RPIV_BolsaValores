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

    @Query("SELECT a.sigla, COUNT(a.sigla) FROM acao a WHERE a.cliente.id = :clienteId GROUP BY a.sigla")
    public List<String[]> findAcoesCliente2(@Param("clienteId") UUID clienteId);

    @Query("SELECT COUNT(a.sigla) FROM acao a WHERE a.cliente.id = :clienteId AND a.sigla = :sigla GROUP BY a.sigla")
    public String findQtdAcoesClienteSigla(@Param("clienteId") UUID clienteId, @Param("sigla") String sigla);

    @Query("SELECT COUNT(*) FROM acao a JOIN a.cliente c WHERE sigla = :siglaAcao AND c.id = :clienteId GROUP BY sigla")
    public Integer findQntAcoesBySiglaClienteId(@Param("siglaAcao") String siglaAcao, @Param("clienteId") UUID clienteId);

    @Query("SELECT a FROM acao a JOIN a.cliente c LEFT JOIN a.vendaOferta o WHERE sigla = :siglaAcao AND c.id = :clienteId AND o.acao.id IS NULL")
    public List<Acao> findAcoesClienteByClienteIdSigla(@Param("clienteId") UUID clienteId, @Param("siglaAcao") String siglaAcao);

    // @Query("SELECT a.empresa.nome, a.sigla, COUNT(a.sigla), ROUND(AVG(ca.valorCompra), 2) FROM acao a INNER JOIN compraAcao ca WHERE ca.cliente.id = :clienteId AND a.cliente.id = :clienteId GROUP BY a.empresa.nome, a.sigla")
    // public List<String[]> findAcoesClientePrecoMedio(@Param("clienteId") UUID clienteId);

    @Query("SELECT a.empresa.nome, a.sigla, COUNT(a.sigla), ROUND(AVG(ca.valorCompra), 2) FROM acao a INNER JOIN compraAcao ca WHERE ca.cliente.id = :clienteId AND a.cliente.id = :clienteId GROUP BY a.empresa.nome, a.sigla")
    public List<String[]> findAcoesClientePrecoMedio(@Param("clienteId") UUID clienteId);

    @Query("SELECT ca.dataCompra, a.empresa.nome, a.sigla, COUNT(a.sigla), ROUND(AVG(ca.valorCompra), 2), SUM(ca.valorCompra) FROM acao a INNER JOIN compraAcao ca WHERE ca.cliente.id = :clienteId AND a.cliente.id = :clienteId GROUP BY ca.dataCompra, a.empresa.nome, a.sigla")
    public List<Object[]> findCompraAcoesCliente(@Param("clienteId") UUID clienteId);

    @Query("SELECT va.dataVenda, a.empresa.nome, a.sigla, COUNT(a.sigla), ROUND(AVG(va.valorVenda), 2), SUM(va.valorVenda) FROM acao a INNER JOIN vendaAcao va WHERE va.cliente.id = :clienteId AND a.cliente.id = :clienteId GROUP BY va.dataVenda, a.empresa.nome, a.sigla")
    public List<Object[]> findVendaAcoesCliente(@Param("clienteId") UUID clienteId);

    @Query("SELECT c.id, COUNT(a.sigla) FROM acao a JOIN a.cliente c WHERE a.sigla = :sigla GROUP BY c.id")
    public List<String[]> findClienteQuantidadeBySigla(String sigla);
}
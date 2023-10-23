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

    @Query("SELECT sigla, MIN(valor) FROM acao GROUP BY sigla")
    public List<String[]> findAcoesSiglaPreco();

    @Query("SELECT a.sigla, MIN(a.valor), COUNT(a.sigla), ROUND(AVG(ca.valorCompra), 2) FROM acao a INNER JOIN compraAcao ca WHERE ca.cliente.id = :cliente_id GROUP BY a.sigla")
    public List<String[]> findAcoesCliente(@Param("cliente_id") UUID cliente_id);

    @Query("SELECT sigla, MIN(valor), COUNT(sigla) FROM acao a WHERE a.cliente IS NULL GROUP BY sigla")
    public List<String[]> findAcoesSiglaPrecoQuantidadeDisponivel();

    @Query("SELECT sigla, MIN(valor), COUNT(sigla) FROM acao a WHERE a.cliente IS NULL AND (LOWER(a.sigla) LIKE LOWER(CONCAT('%', :busca, '%')) OR LOWER(a.empresa.nome) LIKE LOWER(CONCAT('%', :busca, '%'))) GROUP BY sigla")
    public List<String[]> findAcoesBySiglaOrEmpresaNome(@Param("busca") String busca);
}
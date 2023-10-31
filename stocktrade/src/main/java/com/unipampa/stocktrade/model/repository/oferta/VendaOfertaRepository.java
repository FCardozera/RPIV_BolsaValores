package com.unipampa.stocktrade.model.repository.oferta;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;

public interface VendaOfertaRepository extends JpaRepository<VendaOferta, UUID> {

    @Query("SELECT o FROM vendaOferta o JOIN o.acao a WHERE a.sigla = :siglaAcao AND o.valorOferta <= :precoAcao ORDER BY o.valorOferta ASC")
    public List<VendaOferta> findOfertasVendaBySiglaAndPreco(@Param("siglaAcao") String siglaAcao, Pageable pageable, Double precoAcao);

    @Query("SELECT o FROM vendaOferta o JOIN o.acao a WHERE a.sigla = :siglaAcao")
    public List<VendaOferta> findOfertasVendaBySigla(@Param("siglaAcao") String siglaAcao, Pageable pageable);

    @Query("SELECT a.sigla, MIN(o.valorOferta), COUNT(a.sigla) FROM vendaOferta o JOIN o.acao a GROUP BY a.sigla")
    public List<String[]> findOfertasVendaBySiglaAndPreco();

    @Query("SELECT a.sigla, MIN(o.valorOferta), COUNT(a.sigla) FROM vendaOferta o JOIN o.acao a WHERE (LOWER(a.sigla) LIKE LOWER(CONCAT('%', :busca, '%')) OR LOWER(a.empresa.nome) LIKE LOWER(CONCAT('%', :busca, '%'))) GROUP BY a.sigla")
    public List<String[]> findOfertaBySiglaOrEmpresaNome(@Param("busca") String busca);

    @Query("Select MIN(o.valorOferta), COUNT(a.sigla) FROM vendaOferta o JOIN o.acao a WHERE a.sigla = :siglaAcao")
    public String findMenorPrecoOfertaVendaEQuantidadeDisponivelBySigla(@Param("siglaAcao") String siglaAcao);
}
package com.unipampa.stocktrade.model.repository.oferta;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unipampa.stocktrade.model.entity.oferta.Oferta;

public interface OfertaRepository extends JpaRepository<Oferta, UUID> {

    @Query("SELECT o FROM oferta o JOIN o.acao a WHERE o.tipoOferta = 0 AND a.sigla = :siglaAcao AND o.valorOferta <= :precoAcao ORDER BY o.valorOferta ASC")
    public List<Oferta> findOfertasVendaBySiglaAndPreco(@Param("siglaAcao") String siglaAcao, Pageable pageable, Double precoAcao);

    @Query("SELECT o FROM oferta o JOIN o.acao a WHERE o.tipoOferta = 0 AND a.sigla = :siglaAcao")
    public List<Oferta> findOfertasVendaBySigla(@Param("siglaAcao") String siglaAcao, Pageable pageable);

    @Query("SELECT a.sigla, MIN(o.valorOferta), COUNT(a.sigla) FROM oferta o JOIN o.acao a WHERE o.tipoOferta = 0 GROUP BY a.sigla")
    public List<String[]> findOfertasVendaBySiglaAndPreco();

}
package com.unipampa.stocktrade.model.repository.oferta;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.unipampa.stocktrade.model.entity.oferta.CompraOferta;

public interface CompraOfertaRepository extends JpaRepository<CompraOferta, UUID> {

    @Query("SELECT o FROM compraOferta o WHERE o.sigla = :siglaAcao AND o.valorOferta >= :precoAcao ORDER BY o.valorOferta ASC")
    public List<CompraOferta> findOfertasCompraBySiglaAndPreco(@Param("siglaAcao") String siglaAcao, Pageable pageable, Double precoAcao);

    @Query("SELECT o FROM compraOferta o WHERE o.sigla = :siglaAcao")
    public List<CompraOferta> findOfertasCompraBySigla(@Param("siglaAcao") String siglaAcao, Pageable pageable);

    @Query("SELECT o.sigla, MIN(o.valorOferta), COUNT(o.sigla) FROM compraOferta o GROUP BY o.sigla")
    public List<String[]> findOfertasCompraBySiglaAndPreco();

    @Query("SELECT o.sigla, o.valorOferta, COUNT(*) FROM compraOferta o WHERE o.cliente.id = :clienteId GROUP BY o.sigla, o.valorOferta")
    public List<String[]> findOfertasCompraByClienteId(@Param("clienteId") UUID clienteId);

    @Query("SELECT o FROM compraOferta o WHERE o.sigla = :sigla AND o.valorOferta = :preco AND o.cliente.id = :id")
    public List<CompraOferta> findOfertasCompraBySiglaAndPrecoAndIdUser(String sigla, Double preco, UUID id);

}
package com.unipampa.stocktrade.model.entity.oferta.factoryMethod;

import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.oferta.CompraOferta;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;


public class OfertaFactory {
    public static Oferta novaOferta(UUID id, Cliente cliente, Double valorOferta, Instant dataOferta, String sigla, Empresa empresa, Acao acao, TipoOferta tipoOferta) {
        switch (tipoOferta) {
            case COMPRA:
                return new CompraOferta(id, cliente, valorOferta, dataOferta, sigla);

            case VENDA:
                return new VendaOferta(id, cliente, valorOferta, dataOferta, empresa, acao);

            default:
                throw new IllegalArgumentException("TipoOferta não existe!");
        }
    }
}

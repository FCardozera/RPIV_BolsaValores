package com.unipampa.stocktrade.model.entity.oferta;

import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "compraOferta")
@Table(name = "tb_compra_oferta")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompraOferta extends Oferta {
    private String sigla;

    public CompraOferta(UUID id, Cliente cliente, Double valorOferta, Instant dataOferta, String sigla) {
        super(id, cliente, valorOferta, dataOferta);
        this.sigla = sigla;
    }
}

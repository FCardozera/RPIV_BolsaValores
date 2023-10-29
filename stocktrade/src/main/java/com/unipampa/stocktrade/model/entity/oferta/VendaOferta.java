package com.unipampa.stocktrade.model.entity.oferta;

import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "vendaOferta")
@Table(name = "tb_venda_oferta")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class VendaOferta extends Oferta {
    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @OneToOne
    @JoinColumn(name="acao_id", referencedColumnName = "id")
    private Acao acao;


    public VendaOferta(UUID id, Cliente cliente, Double valorOferta, Instant dataOferta, Empresa empresa, Acao acao) {
        super(id, cliente, valorOferta, dataOferta);
        this.acao = acao;
        this.empresa = empresa;
    }
}
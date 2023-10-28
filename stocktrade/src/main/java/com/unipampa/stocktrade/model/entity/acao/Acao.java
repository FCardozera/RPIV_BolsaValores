package com.unipampa.stocktrade.model.entity.acao;

import java.io.Serializable;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "acao")
@Table(name = "tb_acao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Acao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String sigla;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToOne(mappedBy = "acao", cascade = CascadeType.ALL)
    private VendaOferta vendaOferta;

    @OneToOne(mappedBy = "acao", cascade = CascadeType.ALL)
    private CompraAcao compraAcao;

    @OneToOne(mappedBy = "acao", cascade = CascadeType.ALL)
    private VendaAcao vendaAcao;

    public Acao(String sigla) {
        this.sigla = sigla;
    }
}
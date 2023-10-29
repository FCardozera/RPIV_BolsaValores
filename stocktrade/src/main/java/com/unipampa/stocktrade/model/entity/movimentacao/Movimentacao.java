package com.unipampa.stocktrade.model.entity.movimentacao;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.movimentacao.enums.TipoMovimentacao;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "movimentacao")
@Table(name = "tb_movimentacao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Movimentacao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private Double valor;

    private Instant data;

    private TipoMovimentacao tipo;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
package com.unipampa.stocktrade.domain.entity.acao;

import java.util.UUID;

import com.unipampa.stocktrade.domain.entity.empresa.Empresa;
import com.unipampa.stocktrade.domain.entity.oferta.Oferta;
import com.unipampa.stocktrade.domain.entity.usuario.cliente.Cliente;

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

@Entity(name = "acao")
@Table(name = "tb_acao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Acao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String sigla;

    private Double valor;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private Oferta oferta;
}
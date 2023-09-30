package com.unipampa.stocktrade.model.entity.acao;

import java.io.Serializable;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.empresa.Empresa;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;

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
public class Acao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String sigla;

    private Double valor;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "oferta_id")
    private Oferta oferta;

    public Acao(String sigla, Double valor) {
        this.sigla = sigla;
        this.valor = valor;
    }
}
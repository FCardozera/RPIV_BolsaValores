package com.unipampa.stocktrade.model.entity.acao;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "compraAcao")
@Table(name = "tb_compra_acao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CompraAcao implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name="acao_id", referencedColumnName = "id")
    private Acao acao;

    @ManyToOne(cascade = CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name="usuario_id", referencedColumnName="id")
    private Usuario usuario;

    @Column(name = "valor_compra")
    private Double valorCompra;

    @Column(name = "data_compra")
    private Instant dataCompra;
}

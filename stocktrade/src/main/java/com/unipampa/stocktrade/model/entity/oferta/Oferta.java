package com.unipampa.stocktrade.model.entity.oferta;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;

import jakarta.persistence.Column;
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

@Entity(name = "oferta")
@Table(name = "tb_oferta")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Oferta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name="acao_id", referencedColumnName = "id")
    private Acao acao;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "valor_oferta")
    private Double valorOferta;

    @Column(name = "data_oferta")
    private Instant dataOferta;

    @Column(name = "tipo_oferta")
    private TipoOferta tipoOferta;

    public String customToString() {
        return "Oferta [id=" + id + ", valorOferta=" + valorOferta + ", dataOferta=" + dataOferta + ", tipoOferta=" + tipoOferta + "]";
    }

}

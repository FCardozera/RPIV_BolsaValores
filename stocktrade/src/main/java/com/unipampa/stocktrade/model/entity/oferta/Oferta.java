package com.unipampa.stocktrade.model.entity.oferta;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "oferta")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class Oferta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Column(name = "valor_oferta")
    private Double valorOferta;

    @Column(name = "data_oferta")
    private Instant dataOferta;

    public String customToString() {
        return "Oferta [id=" + id + ", valorOferta=" + valorOferta + ", dataOferta=" + dataOferta + "]";
    }

    public Oferta(UUID id, Cliente cliente, Double valorOferta, Instant dataOferta) {
        this.id = id;
        this.cliente = cliente;
        this.valorOferta = valorOferta;
        this.dataOferta = dataOferta;
    }
}

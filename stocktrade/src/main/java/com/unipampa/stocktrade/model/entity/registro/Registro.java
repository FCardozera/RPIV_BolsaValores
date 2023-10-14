package com.unipampa.stocktrade.model.entity.registro;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "registro")
@Table(name = "tb_registro")
@Getter
@Setter
@AllArgsConstructor
public class Registro {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private UUID id;

    @Column
    private UUID clienteId;

    private String atividade;

    @Column
    private Instant instant;

    public Registro() {
        this.id = null;
        this.instant = Instant.now();
    }

    public Registro(String atividade) {
        this();
        this.atividade = atividade;
    }

}

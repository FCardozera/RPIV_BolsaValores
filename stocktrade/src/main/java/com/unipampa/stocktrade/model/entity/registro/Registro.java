package com.unipampa.stocktrade.model.entity.registro;

import java.time.Instant;
import java.util.UUID;

import com.unipampa.stocktrade.controller.DTO.registro.RegistroRequestDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name = "registro")
@Table(name = "tb_registro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Registro {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) 
    private UUID id;

    @Column
    private UUID usuarioId;

    @Column(length = 64)
    private String atividade;

    @Column
    private Instant instant;

    public Registro(RegistroRequestDTO data) {
        this.id = data.id();
        this.usuarioId = data.usuarioId();
        this.atividade = data.atividade();
        this.instant = data.instant();
    }

}

package com.unipampa.stocktrade.controller.DTO.registro;

import java.time.Instant;
import java.util.UUID;
import com.unipampa.stocktrade.model.entity.registro.Registro;

public record RegistroResponseDTO(
    UUID id,
    UUID usuarioId,
    String atividade,
    Instant instant
) { 
    public RegistroResponseDTO (Registro registro){
        this(registro.getId(), registro.getUsuarioId(), registro.getAtividade(), registro.getInstant());
    }

}
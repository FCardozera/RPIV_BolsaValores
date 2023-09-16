package com.unipampa.stocktrade.controller.DTO.registro;

import java.time.Instant;
import java.util.UUID;

public record RegistroRequestDTO (UUID id, UUID usuarioId, String atividade, Instant instant) {
    
}
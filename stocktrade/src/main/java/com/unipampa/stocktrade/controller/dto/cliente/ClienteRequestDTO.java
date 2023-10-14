package com.unipampa.stocktrade.controller.dto.cliente;

public record ClienteRequestDTO(String nome, String cpf, String email, String senha, String senhaAutenticacao, String newEmail, String newPassword) {
    
}

package com.unipampa.stocktrade.model.entity.usuario.FactoryMethod;

import java.util.UUID;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Admin;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

public class UsuarioFactory {
    public static Usuario novoUsuario(UUID id, String nome, String cpfCnpj, String email, String senha, String senhaAutenticacao, TipoUsuario tipoUsuario) {
        switch (tipoUsuario) {
            case CLIENTE:
                return new Cliente(id, nome, cpfCnpj, email, senha, senhaAutenticacao);

            case ADMIN:
                return new Admin(id, nome, email, senha, senhaAutenticacao);

            case EMPRESA:
                return new Empresa(id, nome, cpfCnpj, email, senha, senhaAutenticacao);

            default:
                throw new IllegalArgumentException("TipoUsuario não existe!");
        }
    }

    public static Usuario novoUsuario(ClienteRequestDTO dados) {
        return new Cliente(dados);
    }
}

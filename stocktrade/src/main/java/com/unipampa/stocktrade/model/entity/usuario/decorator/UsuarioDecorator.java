package com.unipampa.stocktrade.model.entity.usuario.decorator;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.decorator.interfaces.IUsuarioDecorator;

public class UsuarioDecorator implements IUsuarioDecorator {
    private Cliente cliente;

    public UsuarioDecorator(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String getNome() {
        return cliente.getNome();
    }

    @Override
    public String getEmail() {
        return cliente.getEmail();
    }

    @Override
    public String getCPF() {
        String cpf = cliente.getCpf();
        return String.format("%s.%s.%s-%s", cpf.substring(0, 3), cpf.substring(3, 6),
                cpf.substring(6, 9), cpf.substring(9));
    }
}

package com.unipampa.stocktrade.model.entity.usuario.padroes;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.padroes.interfaces.UsuarioDecorator;

public class CPFFormatDecorator implements UsuarioDecorator {
    private Cliente cliente;

    public CPFFormatDecorator(Cliente cliente) {
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

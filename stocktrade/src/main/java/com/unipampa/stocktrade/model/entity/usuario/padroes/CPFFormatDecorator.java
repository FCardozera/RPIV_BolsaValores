package com.unipampa.stocktrade.model.entity.usuario.padroes;

import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.padroes.interfaces.UsuarioDecorator;

public class CPFFormatDecorator implements UsuarioDecorator {
    private Usuario usuario;

    public CPFFormatDecorator(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String getNome() {
        return usuario.getNome();
    }

    @Override
    public String getEmail() {
        return usuario.getEmail();
    }

    @Override
    public String getCPF() {
        String cpf = usuario.getCpf();
        return String.format("%s.%s.%s-%s", cpf.substring(0, 3), cpf.substring(3, 6),
                cpf.substring(6, 9), cpf.substring(9));
    }
}

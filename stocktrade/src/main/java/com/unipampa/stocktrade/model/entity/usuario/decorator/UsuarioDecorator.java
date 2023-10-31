package com.unipampa.stocktrade.model.entity.usuario.decorator;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.decorator.interfaces.IUsuarioDecorator;

public class UsuarioDecorator implements IUsuarioDecorator {
    private Usuario usuario;

    public UsuarioDecorator(Usuario usuario) {
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
        try {
            if (!(usuario instanceof Cliente)) {
                throw new Exception(usuario.getNome() + " não é um cliente, não possui CPF cadastrado.");
            }
            String cpf = ((Cliente) usuario).getCpf();
            return String.format("%s.%s.%s-%s", cpf.substring(0, 3), cpf.substring(3, 6),
                cpf.substring(6, 9), cpf.substring(9));
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

    @Override
    public String getCNPJ() {
        try {
            if (!(usuario instanceof Empresa)) {
                throw new Exception(usuario.getNome() + " não é uma empresa, não possui CNPJ cadastrado.");
            }
            String cnpj = ((Empresa) usuario).getCnpj();
            return String.format("%s.%s.%s/%s-%s", cnpj.substring(0, 2), cnpj.substring(2, 5),
                cnpj.substring(5, 8), cnpj.substring(8, 12), cnpj.substring(12));
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }

}

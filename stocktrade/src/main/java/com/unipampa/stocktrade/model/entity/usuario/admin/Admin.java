package com.unipampa.stocktrade.model.entity.usuario.admin;

import com.unipampa.stocktrade.model.entity.usuario.Usuario;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "admin")
@Table(name = "tb_admin")
@Getter
@Setter
@ToString
public class Admin extends Usuario {
    
    private static final long serialVersionUID = 1L;

    public Admin(String email, String cpf, String nome, String senha, String senhaAutenticacao) {
        super(null, senhaAutenticacao, email, cpf, nome, senha);
    }
}

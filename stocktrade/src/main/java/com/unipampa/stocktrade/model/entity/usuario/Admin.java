package com.unipampa.stocktrade.model.entity.usuario;

import java.util.UUID;

import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "admin")
@Table(name = "tb_admin")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Admin extends Usuario {

    private static final long serialVersionUID = 1L;

    public Admin(UUID id, String nome, String email, String senha, String senhaAutenticacao) {
        super(id, nome, email, senha, senhaAutenticacao, TipoUsuario.ADMIN);
    }
}

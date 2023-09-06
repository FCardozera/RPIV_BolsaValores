package com.unipampa.stocktrade.domain.entity.usuario.admin;

import com.unipampa.stocktrade.domain.entity.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "admin")
@Table(name = "tb_admin")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Admin extends Usuario {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "senha_admin")
    private String senhaAdmin;
}

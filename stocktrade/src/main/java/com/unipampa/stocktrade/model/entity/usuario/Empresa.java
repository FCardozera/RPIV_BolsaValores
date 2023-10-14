package com.unipampa.stocktrade.model.entity.usuario;

import java.util.Set;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "empresa")
@Table(name = "tb_empresa")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Empresa extends Usuario {

    private static final long serialVersionUID = 1L;

    @Column(unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "empresa")
    private Set<Acao> acoes;

    public Empresa(UUID id, String nome, String cnpj, String email, String senha, String senhaAutenticacao) {
        super(id, nome, email, senha, senhaAutenticacao, TipoUsuario.EMPRESA);
        this.cnpj = cnpj;
    }
}
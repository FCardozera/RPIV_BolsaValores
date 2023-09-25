package com.unipampa.stocktrade.model.entity.usuario;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;
import com.unipampa.stocktrade.util.Util;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "usuario")
@Table(name = "tb_usuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true)
    private String email;

    @Column(name = "saldo")
    private Double saldo;

    @Column(name = "conta_ativa")
    private Boolean contaAtiva;

    private TipoUsuario tipo;

    @Column(name = "hash_senha")
    private String hashSenha;

    @Column(name = "salt_senha")
    private byte[] saltSenha;

    @Column(name = "hash_senha_autenticacao")
    private String hashSenhaAutenticacao;

    @Column(name = "salt_senha_autenticacao")
    private byte[] saltSenhaAutenticacao;

    @OneToMany(mappedBy = "usuario")
    private Set<Movimentacao> movimentacoes;

    @OneToMany(mappedBy = "usuario")
    private Set<Acao> acoes;

    @OneToMany(mappedBy = "usuario")
    private Set<Oferta> ofertas;

    public Usuario(UUID id, String nome, String cpf, String email, String senha, String senhaAutenticacao) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.contaAtiva = true;
        this.saldo = 0.0;
        this.tipo = TipoUsuario.CLIENTE;

        this.saltSenha = Util.salt();
        this.hashSenha = Util.sha256(senha, this.saltSenha);  
        
        this.saltSenhaAutenticacao = Util.salt();
        this.hashSenhaAutenticacao = Util.sha256(senhaAutenticacao, this.saltSenhaAutenticacao);
    }

    public Usuario(UUID id, String nome, String cpf, String email, String senha, String senhaAutenticacao, TipoUsuario tipo) {
        this(id, nome, cpf, email, senha, senhaAutenticacao);
        this.tipo = tipo;
    }

    public Usuario(UsuarioRequestDTO data) {
        this(null, data.nome(), data.cpf(), data.email(), data.senha(), data.senhaAutenticacao());
    }

    public boolean isSenhaCorreta(String senha) {
        String hashSenha = Util.sha256(senha, this.saltSenha);

        return this.hashSenha.equals(hashSenha);
    }
}

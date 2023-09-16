package com.unipampa.stocktrade.model.entity.usuario;

import java.io.Serializable;
import java.util.UUID;

import com.unipampa.stocktrade.util.Util;

import jakarta.persistence.InheritanceType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "usuario")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@ToString
public abstract class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String cpf;

    private String nome;

    @Column(unique = true)
    private String email;

    @Column(name = "hash_senha")
    private String hashSenha;

    private byte[] saltSenha;

    @Column(name = "hash_senha_autenticacao")
    private String hashSenhaAutenticacao;

    private byte[] saltSenhaAutenticacao;

    public Usuario(UUID id, String senhaAutenticacao, String cpf, String nome, String email, String senha) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;

        this.saltSenha = Util.salt();
        this.hashSenha = Util.sha256(senha, this.saltSenha);  
        
        this.saltSenhaAutenticacao = Util.salt();
        this.hashSenhaAutenticacao = Util.sha256(senhaAutenticacao, this.saltSenhaAutenticacao);
    }
}

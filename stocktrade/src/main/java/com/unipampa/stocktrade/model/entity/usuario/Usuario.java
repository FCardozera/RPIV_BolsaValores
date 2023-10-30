package com.unipampa.stocktrade.model.entity.usuario;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "usuario")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String email;

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

    protected Usuario(UUID id, String nome, String email, String senha, String senhaAutenticacao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.contaAtiva = true;

        this.saltSenha = salt();
        this.hashSenha = sha256(senha, this.saltSenha);

        this.saltSenhaAutenticacao = salt();
        this.hashSenhaAutenticacao = sha256(senhaAutenticacao, this.saltSenhaAutenticacao);
    }

    protected Usuario(UUID id, String nome, String email, String senha, String senhaAutenticacao, TipoUsuario tipo) {
        this(id, nome, email, senha, senhaAutenticacao);
        this.tipo = tipo;
    }

    protected Usuario(ClienteRequestDTO data) {
        this(null, data.nome(), data.email(), data.senha(), data.senhaAutenticacao());
    }

    public void trocarEmail(String novoEmail) {
        this.email = novoEmail;
    }

    public void trocarSenha(String novaSenha) {
        this.saltSenha = salt();
        this.hashSenha = sha256(novaSenha, this.saltSenha);
    }

    public boolean isSenhaCorreta(String senha) {
        String senhaHash = sha256(senha, this.saltSenha);

        return this.hashSenha.equals(senhaHash);
    }

    public boolean isSenhaAutenticacaoCorreta(String senhaAutenticacao) {
        String senhaHashAutenticacao = sha256(senhaAutenticacao, this.saltSenhaAutenticacao);

        return this.hashSenhaAutenticacao.equals(senhaHashAutenticacao);
    }

    private String sha256(String senha, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            String saltString = new String(salt);
            String senhaSalt = senha.concat(saltString);

            byte[] hashBytes = md.digest(senhaSalt.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao gerar hash da senha");
        }
    }

    private byte[] salt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return salt;
    }
}

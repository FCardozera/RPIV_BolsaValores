package com.unipampa.stocktrade.model.entity.usuario;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Set;
import java.util.UUID;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

    @OneToMany(mappedBy = "usuario", fetch = FetchType.EAGER)
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

        this.saltSenha = salt();
        this.hashSenha = sha256(senha, this.saltSenha);  
        
        this.saltSenhaAutenticacao = salt();
        this.hashSenhaAutenticacao = sha256(senhaAutenticacao, this.saltSenhaAutenticacao);
    }

    public Usuario(UUID id, String nome, String cpf, String email, String senha, String senhaAutenticacao, TipoUsuario tipo) {
        this(id, nome, cpf, email, senha, senhaAutenticacao);
        this.tipo = tipo;
    }

    public Usuario(UsuarioRequestDTO data) {
        this(null, data.nome(), data.cpf(), data.email(), data.senha(), data.senhaAutenticacao());
    }

    public boolean isSenhaCorreta(String senha) {
        String hashSenha = sha256(senha, this.saltSenha);

        return this.hashSenha.equals(hashSenha);
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

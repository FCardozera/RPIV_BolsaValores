package com.unipampa.stocktrade.model.entity.usuario.cliente;

import java.util.Set;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "cliente")
@Table(name = "tb_cliente")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cliente extends Usuario {
    
    private static final long serialVersionUID = 1L;

    @Column(name = "saldo_conta")
    private Double saldoConta;

    @Column(name = "conta_ativa")
    private Boolean contaAtiva;

    @OneToMany(mappedBy = "cliente")
    private Set<Movimentacao> movimentacoes;

    @OneToMany(mappedBy = "cliente")
    private Set<Acao> acoes;

    @OneToMany(mappedBy = "cliente")
    private Set<Oferta> ofertas;

    public Cliente(UUID id, String nome, String cpf, String email, String senha, String senhaAutenticacao, Double saldoConta, Boolean contaAtiva) {
        super(id, senhaAutenticacao, cpf, nome, email, senha);
        this.saldoConta = saldoConta;
        this.contaAtiva = contaAtiva;
    }

    public Cliente(UUID id, String nome, String cpf, String email, String senha, String senhaAutenticacao, Double saldoConta, Boolean contaAtiva, Set<Movimentacao> movimentacoes) {
        super(id, senhaAutenticacao, cpf, nome, email, senha);
        this.saldoConta = saldoConta;
        this.contaAtiva = contaAtiva;
        this.movimentacoes = movimentacoes;
    }
}

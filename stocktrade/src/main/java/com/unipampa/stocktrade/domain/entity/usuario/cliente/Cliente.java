package com.unipampa.stocktrade.domain.entity.usuario.cliente;

import java.util.Set;

import com.unipampa.stocktrade.domain.entity.acao.Acao;
import com.unipampa.stocktrade.domain.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.domain.entity.oferta.Oferta;
import com.unipampa.stocktrade.domain.entity.usuario.Usuario;

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

    public Cliente(String nome, String cpf, String email, String senha, String senhaAutenticacao, Double saldoConta, Boolean contaAtiva) {
        super(null, senhaAutenticacao, email, cpf, nome, senha);
        this.saldoConta = saldoConta;
        this.contaAtiva = contaAtiva;
    }

    public Cliente(String email, String cpf, String nome, String senha, String senhaAutenticacao, Double saldoConta, Boolean contaAtiva, Set<Movimentacao> movimentacoes) {
        super(null, senhaAutenticacao, email, cpf, nome, senha);
        this.saldoConta = saldoConta;
        this.contaAtiva = contaAtiva;
        this.movimentacoes = movimentacoes;
    }
}

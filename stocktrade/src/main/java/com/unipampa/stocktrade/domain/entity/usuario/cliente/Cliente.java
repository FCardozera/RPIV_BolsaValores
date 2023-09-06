package com.unipampa.stocktrade.domain.entity.usuario.cliente;

import java.util.Set;

import com.unipampa.stocktrade.domain.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.domain.entity.usuario.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "cliente")
@Table(name = "tb_cliente")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cliente extends Usuario {
    
    private static final long serialVersionUID = 1L;

    private String senha_autenticacao;

    private Long saldo_conta;

    private boolean conta_ativa;

    @OneToMany(mappedBy = "cliente")
    private Set<Movimentacao> movimentacoes;
}

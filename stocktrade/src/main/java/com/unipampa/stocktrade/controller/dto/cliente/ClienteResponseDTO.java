package com.unipampa.stocktrade.controller.dto.cliente;

import java.util.Set;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.oferta.CompraOferta;
import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

public record ClienteResponseDTO(UUID id, String nome, String cpf, String email, Double saldo, Boolean contaAtiva, TipoUsuario tipo, Set<Movimentacao> movimentacoes, Set<Acao> acoes, Set<CompraOferta> compraOfertas, Set<VendaOferta> vendaOfertas, String hashSenha, byte[] saltSenha, String hashSenhaAutenticacao, byte[] saltSenhaAutenticacao) {
    public ClienteResponseDTO(Cliente cliente) {
        this(cliente.getId(), cliente.getNome(), cliente.getCpf(), cliente.getEmail(), cliente.getSaldo(), cliente.getContaAtiva(), cliente.getTipo(), cliente.getMovimentacoes(), cliente.getAcoes(), cliente.getCompraOfertas(), cliente.getVendaOfertas(), cliente.getHashSenha(), cliente.getSaltSenha(), cliente.getHashSenhaAutenticacao(), cliente.getSaltSenhaAutenticacao());
    }
}

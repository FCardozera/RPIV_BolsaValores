package com.unipampa.stocktrade.controller.dto.usuario;

import java.util.Set;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

public record UsuarioResponseDTO(UUID id, String nome, String cpf, String email, Double saldo, Boolean contaAtiva, TipoUsuario tipo, Set<Movimentacao> movimentacoes, Set<Acao> acoes, Set<Oferta> ofertas, String hashSenha, byte[] saltSenha, String hashSenhaAutenticacao, byte[] saltSenhaAutenticacao) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getCpf(), usuario.getEmail(), usuario.getSaldo(), usuario.getContaAtiva(), usuario.getTipo(), usuario.getMovimentacoes(), usuario.getAcoes(), usuario.getOfertas(), usuario.getHashSenha(), usuario.getSaltSenha(), usuario.getHashSenhaAutenticacao(), usuario.getSaltSenhaAutenticacao());
    }
}

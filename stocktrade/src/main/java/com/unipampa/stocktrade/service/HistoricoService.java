package com.unipampa.stocktrade.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class HistoricoService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Set<Movimentacao> getMovimentacoes(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        Cliente cliente = clienteRepository.findById(usuario.getId()).get();

        if (cliente == null) {
            throw new NullPointerException("Cliente não encontrado");
        }

        return cliente.getMovimentacoes();
    }
}

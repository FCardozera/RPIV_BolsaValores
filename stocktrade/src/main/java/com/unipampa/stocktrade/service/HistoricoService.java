package com.unipampa.stocktrade.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class HistoricoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AcaoRepository acaoRepository;

    public Set<Movimentacao> getMovimentacoes(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        Cliente cliente = clienteRepository.findById(usuario.getId()).get();

        if (cliente == null) {
            throw new NullPointerException("Cliente não encontrado");
        }

        return cliente.getMovimentacoes();
    }

    public List<Object[]> getNegociacoes(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        Cliente cliente = clienteRepository.findById(usuario.getId()).get();

        if (cliente == null) {
            throw new NullPointerException("Cliente não encontrado");
        }

        List<Object[]> compraAcoes = acaoRepository.findCompraAcoesCliente(cliente.getId());
        List<Object[]> vendaAcoes = acaoRepository.findVendaAcoesCliente(cliente.getId());
        List<Object[]> negociacoes = new ArrayList<>();
        for (Object[] compraAcao : compraAcoes) {
            Object[] negociacao = new Object[7];
            negociacao[0] = compraAcao[0]; // DATA
            negociacao[1] = compraAcao[1]; // EMPRESA
            negociacao[2] = compraAcao[2]; // ACAO
            negociacao[3] = "Compra"; // TIPO
            negociacao[4] = compraAcao[3]; // QTD DE AÇÕES
            negociacao[5] = compraAcao[4]; // PREÇO MEDIO
            negociacao[6] = compraAcao[5]; // VALOR TOTAL
            negociacoes.add(negociacao);
        }

        for (Object[] vendaAcao : vendaAcoes) {
            Object[] negociacao = new Object[7];
            negociacao[0] = vendaAcao[0]; // DATA
            negociacao[1] = vendaAcao[1]; // EMPRESA
            negociacao[2] = vendaAcao[2]; // ACAO
            negociacao[3] = "Venda"; // TIPO
            negociacao[4] = vendaAcao[3]; // QTD DE AÇÕES
            negociacao[5] = vendaAcao[4]; // PREÇO MEDIO
            negociacao[6] = vendaAcao[5]; // VALOR TOTAL
            negociacoes.add(negociacao);
        }
        return ordenaListaPorData(negociacoes);
    }

    private List<Object[]> ordenaListaPorData(List<Object[]> lista) {
        Collections.sort(lista, new Comparator<Object[]>() {
            @Override
            public int compare(Object[] o1, Object[] o2) {
                Instant instant1 = (Instant) o1[0];
                Instant instant2 = (Instant) o2[0];
                return instant2.compareTo(instant1);
            }
        });
        return lista;
    }
}

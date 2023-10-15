package com.unipampa.stocktrade.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.acao.CompraAcoesDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CarteiraService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private CompraAcaoRepository compraAcaoRepository;

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = clienteRepository.findByEmail(usuarioLogado.getEmail());
        session.setAttribute("usuarioLogado", usuario);
        return session;
    }

    public Double variacaoSaldoUsuario24H(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        Double variacaoSaldo = cliente.variacaoSaldo24h();
        return variacaoSaldo;
    }

    public List<String> mesesMovimentacoes1AnoUsuario(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        LinkedHashMap<String, Double> movimentacoesMensais = cliente.movimentacoesMensais1Ano();
        LinkedList<String> listaMeses = new LinkedList<String>();
        for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
            listaMeses.add(entry.getKey());
        }
        return listaMeses;
    }

    public List<Double> saldosFinaisMovimentacoes1AnoUsuario(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        LinkedHashMap<String, Double> movimentacoesMensais = cliente.movimentacoesMensais1Ano();
        LinkedList<Double> listaSaldosFinais = new LinkedList<Double>();
        for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
            listaSaldosFinais.add(entry.getValue());
        }
        return listaSaldosFinais;
    }

    public List<String[]> getAcoesUser (HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        List<String[]> acoesString = acaoRepository.findAcoesCliente(cliente.getId());
        List<String[]> acoes = new ArrayList<>();

        for (String[] acaoQueryBanco : acoesString) {
            String[] acaoFinal = new String[5];
            for (int i = 0; i < acaoQueryBanco.length; i++) {
                acaoFinal[i] = acaoQueryBanco[i];
            }
            Double valorAtual = Double.parseDouble(acaoQueryBanco[2]);
            Double precoMedio = Double.parseDouble(acaoQueryBanco[3]);

            Double variacao = (((valorAtual-precoMedio)*100)/precoMedio);
            String variacaoFormatada = String.format("%.2f", variacao);
            acaoFinal[4] = variacaoFormatada;
            acoes.add(acaoFinal);
        }

        return acoes;
    }

    public void comprarAcoes(HttpSession session, CompraAcoesDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }

        List<Acao> acoesSemCliente = acaoRepository.findAcoesClienteNull(dados.siglaAcao());

        if (acoesSemCliente.isEmpty()) {
            throw new RuntimeException("Não existem ações disponíveis para compra");
        }

        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());

        for (int i = 0; i < dados.quantidadeAcoes(); i++) {
            Acao acao = acoesSemCliente.get(i);

            cliente = (Cliente) usuario;
            acao.setCliente(cliente);
            cliente.reduzirSaldo(acao.getValor());

            acaoRepository.save(acao);

            CompraAcao compraAcao = new CompraAcao(null, acao, cliente, acao.getValor(), Instant.now());
            compraAcaoRepository.save(compraAcao);
        }

        clienteRepository.save(cliente);
    }
}

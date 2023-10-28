package com.unipampa.stocktrade.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.acao.CompraAcoesDTO;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.oferta.CompraOferta;
import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
import com.unipampa.stocktrade.model.entity.oferta.factoryMethod.OfertaFactory;
import com.unipampa.stocktrade.model.entity.oferta.iterator.vendaOferta.VendaOfertaIterator;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;

import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.model.repository.oferta.CompraOfertaRepository;
import com.unipampa.stocktrade.model.repository.oferta.VendaOfertaRepository;
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

    @Autowired
    private VendaOfertaRepository vendaOfertaRepository;

    @Autowired
    private CompraOfertaRepository compraOfertaRepository;

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = clienteRepository.findByEmail(usuarioLogado.getEmail());
        session.setAttribute("usuarioLogado", usuario);
        return session;
    }

    public Double variacaoSaldoUsuario24H(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            Double variacaoSaldo = cliente.variacaoSaldo24h();
            return variacaoSaldo;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<String> mesesMovimentacoes1AnoUsuario(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            LinkedHashMap<String, Double> movimentacoesMensais = cliente.movimentacoesMensais1Ano();
            LinkedList<String> listaMeses = new LinkedList<String>();
            for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
                listaMeses.add(entry.getKey());
            }
            return listaMeses;
        } catch (Exception e) {
            return new ArrayList<String>();
        }
    }

    public List<Double> saldosFinaisMovimentacoes1AnoUsuario(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            LinkedHashMap<String, Double> movimentacoesMensais = cliente.movimentacoesMensais1Ano();
            LinkedList<Double> listaSaldosFinais = new LinkedList<Double>();
            for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
                listaSaldosFinais.add(entry.getValue());
            }
            return listaSaldosFinais;
        } catch (Exception e) {
            return new ArrayList<Double>();
        }
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

    public ResponseEntity<String> comprarAcoes(HttpSession session, CompraAcoesDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }

        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());

        if (!cliente.isSenhaAutenticacaoCorreta(dados.senhaAutenticacao())) {
            throw new RuntimeException("Senha incorreta");
        }

        if (acaoRepository.findAcoesSigla(dados.siglaAcao()) < 0) {
            throw new RuntimeException("Sigla de ação inválida");
        }

        List<VendaOferta> ofertasVenda = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco(dados.siglaAcao(), PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
        if (ofertasVenda.isEmpty()) {
            for (int i = 0; i < dados.quantidadeAcoes(); i++) {
                CompraOferta oferta = (CompraOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), null, null, TipoOferta.COMPRA);
                compraOfertaRepository.save(oferta);
            }
            return ResponseEntity.ok("Aguardando ofertas");
        }

        for (int i = 0; i < dados.quantidadeAcoes() - ofertasVenda.size(); i++) {
            CompraOferta oferta = (CompraOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), null, null, TipoOferta.COMPRA);
            compraOfertaRepository.save(oferta);
        }

        Iterator<VendaOferta> ofertaIterator = new VendaOfertaIterator(ofertasVenda.iterator());
        try {
            while (ofertaIterator.hasNext()) {
                VendaOferta vendaOferta = ofertaIterator.next();
                Acao acao = vendaOferta.getAcao();

                CompraAcao compraAcao = cliente.comprarAcao(vendaOferta);
                compraAcaoRepository.save(compraAcao);

                acaoRepository.save(acao);
                
                vendaOfertaRepository.save(vendaOferta);
                vendaOfertaRepository.deleteById(vendaOferta.getId());
            }
        } catch (Exception e) {
            throw e;
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações compradas");
    }
}

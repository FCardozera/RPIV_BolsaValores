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
import com.unipampa.stocktrade.controller.dto.acao.VendaAcoesDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.acao.VendaAcao;
// import com.unipampa.stocktrade.model.entity.acao.VendaAcao;
import com.unipampa.stocktrade.model.entity.oferta.CompraOferta;
import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
import com.unipampa.stocktrade.model.entity.oferta.factoryMethod.OfertaFactory;
import com.unipampa.stocktrade.model.entity.oferta.iterator.vendaOferta.VendaOfertaIterator;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;

import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.VendaAcaoRepository;
// import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
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
    private VendaOfertaRepository vendaOfertaRepository;

    @Autowired
    private CompraOfertaRepository compraOfertaRepository;

    @Autowired 
    private CompraAcaoRepository compraAcaoRepository;

    @Autowired 
    private VendaAcaoRepository vendaAcaoRepository;

    private static final String USUARIO_LOGADO = "usuarioLogado";

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Usuario usuario = clienteRepository.findByEmail(usuarioLogado.getEmail());
        session.setAttribute(USUARIO_LOGADO, usuario);
        return session;
    }

    public Double variacaoSaldoUsuario24H(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            return cliente.variacaoSaldo24h();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<String> mesesMovimentacoes1AnoUsuario(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            LinkedHashMap<String, Double> movimentacoesMensais = (LinkedHashMap<String, Double>) cliente.movimentacoesMensais1Ano();
            LinkedList<String> listaMeses = new LinkedList<>();
            for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
                listaMeses.add(entry.getKey());
            }
            return listaMeses;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Double> saldosFinaisMovimentacoes1AnoUsuario(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            LinkedHashMap<String, Double> movimentacoesMensais = (LinkedHashMap<String, Double>) cliente.movimentacoesMensais1Ano();
            LinkedList<Double> listaSaldosFinais = new LinkedList<>();
            for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
                listaSaldosFinais.add(entry.getValue());
            }
            return listaSaldosFinais;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String[]> getAcoesUser (HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        List<String[]> acoesString = acaoRepository.findAcoesClientePrecoMedio(cliente.getId());
        List<String[]> acoes = new ArrayList<>();

        for (String[] acaoQueryBanco : acoesString) {
            String[] acaoFinal = new String[6];

            acaoFinal[0] = acaoQueryBanco[0]; //SIGLA
            String[] valorAtualEQuantidadeDisponivel = vendaOfertaRepository.findMenorPrecoOfertaVendaEQuantidadeDisponivelBySigla(acaoQueryBanco[0]).split(",");
            acaoFinal[1] = valorAtualEQuantidadeDisponivel[0];
            acaoFinal[2] = acaoQueryBanco[1];
            acaoFinal[3] = acaoQueryBanco[2];

            Double valorAtualDouble = Double.parseDouble(valorAtualEQuantidadeDisponivel[0]);
            Double precoMedio = Double.parseDouble(acaoFinal[3]);

            Double variacao = ((valorAtualDouble*100)/precoMedio)-100;
            String variacaoFormatada = String.format("%.2f", variacao);
            acaoFinal[4] = variacaoFormatada;
            acaoFinal[5] = valorAtualEQuantidadeDisponivel[1];
            acoes.add(acaoFinal);
        }

        return acoes;
    }

    public List<String[]> getCompraOfertasUser(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        
        List<String[]> ofertas = compraOfertaRepository.findOfertasCompraByClienteId(cliente.getId());
        return ofertas;
    }

    public ResponseEntity<String> comprarAcoes(HttpSession session, CompraAcoesDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

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
            agendarCompraOferta(dados.quantidadeAcoes(), cliente, dados);
            return ResponseEntity.ok("Aguardando ofertas");
        }

        if (quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(ofertasVenda.size(), dados.quantidadeAcoes())) {
            agendarCompraOferta(dados.quantidadeAcoes() - ofertasVenda.size(), cliente, dados);
        }

        Iterator<VendaOferta> ofertaIterator = new VendaOfertaIterator(ofertasVenda.iterator());
        try {
            while (ofertaIterator.hasNext()) {
                VendaOferta vendaOferta = ofertaIterator.next();
                Acao acao = vendaOferta.getAcao();

                CompraAcao compraAcao = cliente.comprarAcao(vendaOferta);    

                compraAcaoRepository.save(compraAcao);
                clienteRepository.save(cliente);

                acaoRepository.save(acao);
                
                vendaOfertaRepository.save(vendaOferta);
                vendaOfertaRepository.deleteById(vendaOferta.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações compradas");
    }

    public ResponseEntity<String> venderAcoes(HttpSession session, VendaAcoesDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
    
        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }
    
        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());
    
        if (!cliente.isSenhaAutenticacaoCorreta(dados.senhaAutenticacao())) {
            throw new RuntimeException("Senha incorreta");
        }
    
        verificarAcoesParaVenda(cliente, dados.quantidadeAcoes(), dados.siglaAcao());

        List<CompraOferta> ofertasCompra = compraOfertaRepository.findOfertasCompraBySiglaAndPreco(dados.siglaAcao(), PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
        List<Acao> acoesCliente = acaoRepository.findAcoesClienteByClienteIdSigla(cliente.getId(), dados.siglaAcao());

        Iterator<Acao> acoesIteratorAgendarVendaOferta = acoesCliente.iterator();
        if (ofertasCompra.isEmpty()) {
            agendarVendaOferta(dados.quantidadeAcoes(), cliente, dados, acoesIteratorAgendarVendaOferta);
            return ResponseEntity.ok("Aguardando ofertas de compra");
        }

        Iterator<Acao> acoesIteratorAgendarParcialmenteVendaOferta = acoesCliente.iterator();
        if (quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(ofertasCompra.size(), dados.quantidadeAcoes())) {
            agendarVendaOferta(dados.quantidadeAcoes() - ofertasCompra.size(), cliente, dados, acoesIteratorAgendarParcialmenteVendaOferta);
        }
        
        Iterator<CompraOferta> ofertaIterator = ofertasCompra.iterator();
        Iterator<Acao> acoesIterator = acoesCliente.iterator();
        try {
            while (ofertaIterator.hasNext()) {
                CompraOferta compraOferta = ofertaIterator.next();

                Acao acao = acoesIterator.next();

                VendaAcao vendaAcao = cliente.venderAcao(compraOferta, acao);

                vendaAcaoRepository.save(vendaAcao);
                clienteRepository.save(cliente);

                acaoRepository.save(acao);
                
                compraOfertaRepository.save(compraOferta);
                compraOfertaRepository.deleteById(compraOferta.getId());
            }
        } catch (Exception e) {
            throw e;
        }
    
        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações vendidas");
    }
    
    private void verificarAcoesParaVenda(Cliente cliente, int quantidadeParaVender, String siglaAcao) {
        Integer qntAcoesClienteSigla = acaoRepository.findQntAcoesBySiglaClienteId(siglaAcao, cliente.getId());

        if (qntAcoesClienteSigla == null) {
            throw new RuntimeException("Você não possui ações suficientes para a venda");
        }

        if (qntAcoesClienteSigla < quantidadeParaVender) {
            throw new RuntimeException("Você não possui ações suficientes para a venda");
        }
    }

    private boolean quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(Integer quantidadeOferta, Integer quantidadeAcoes) {
        return quantidadeOferta > quantidadeAcoes;
    }

    private boolean agendarCompraOferta(Integer quantidadeAgendamento, Cliente cliente, CompraAcoesDTO dados) {
        if (quantidadeAgendamento <= 0) {
            return false;
        }

        for (int i = 0; i < quantidadeAgendamento; i++) {
            CompraOferta oferta = (CompraOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), null, null, TipoOferta.COMPRA);
            compraOfertaRepository.save(oferta);
        }

        return true;
    }

    private boolean agendarVendaOferta(Integer quantidadeAgendamento, Cliente cliente, VendaAcoesDTO dados, Iterator<Acao> acoesIterator) {
        if (quantidadeAgendamento <= 0) {
            return false;
        }

        for (int i = 0; i < quantidadeAgendamento; i++) {
            VendaOferta oferta = (VendaOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), acoesIterator.next().getEmpresa(), acoesIterator.next(), TipoOferta.VENDA);
            vendaOfertaRepository.save(oferta);
        }

        return true;
    }

    public List<String[]> getVendaOfertasUser(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        
        List<String[]> ofertas = vendaOfertaRepository.findOfertasVendaByClienteId(cliente.getId());
        return ofertas;
    }
 
}

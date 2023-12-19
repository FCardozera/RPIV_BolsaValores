package com.unipampa.stocktrade.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.acao.CompraAcoesDTO;
import com.unipampa.stocktrade.controller.dto.acao.VendaAcoesDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.acao.VendaAcao;
import com.unipampa.stocktrade.model.entity.acao.iterator.listarAcao.AcaoIterator;
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
import com.unipampa.stocktrade.model.repository.oferta.CompraOfertaRepository;
import com.unipampa.stocktrade.model.repository.oferta.VendaOfertaRepository;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;
import com.unipampa.stocktrade.service.exception_handler.ExceptionHandlerChain;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

@Service
public class InvistaLogadoService {

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaOfertaRepository vendaOfertaRepository;

    @Autowired
    private CompraOfertaRepository compraOfertaRepository;

    @Autowired
    private CompraAcaoRepository compraAcaoRepository;

    @Autowired
    private VendaAcaoRepository vendaAcaoRepository;

    @Autowired
    private RegistroRepository registroRepository;

    private static final String USUARIO_LOGADO = "usuarioLogado";

    public List<Acao> getAcoesSiglaPrecoIterator() {
        Iterator<String[]> acoesString = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco().iterator();

        AcaoIterator acaoIterator = new AcaoIterator(acoesString);

        return acaoIterator.getAllAcoes();
    }

    public List<String[]> getAcoesSiglaPrecoQuantidadeVenda(Cliente cliente) {

        List<String[]> acoes = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco();
        List<String[]> acoesFinal = new ArrayList<>();

        for (String[] acao : acoes) {
            String[] acaoFinal = new String[6];
            acaoFinal[0] = acao[1];
            acaoFinal[1] = acao[2];
            acaoFinal[2] = acaoRepository.findQtdAcoesClienteSigla(cliente.getId(), acao[1]);
            if (acaoFinal[2] == null) {
                acaoFinal[2] = "0";
            }
            acaoFinal[3] = acao[3];
            acaoFinal[4] = acao[0];
            acaoFinal[5] = acaoFinal[3];

            acoesFinal.add(acaoFinal);
        }

        return acoesFinal;
    }

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Usuario usuario = clienteRepository.findByEmail(usuarioLogado.getEmail());
        Cliente cliente = (Cliente) usuario;
        cliente.fixarSaldoAtual();
        session.setAttribute(USUARIO_LOGADO, cliente);
        return session;
    }

    public ResponseEntity<String> comprarAcoes(HttpSession session, CompraAcoesDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

        if (usuario == null) {
            return ResponseEntity.badRequest()
                    .body(ExceptionHandlerChain.handle(TipoException.SEM_USUARIO, null, registroRepository));
        }

        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());

        if (!cliente.isSenhaAutenticacaoCorreta(dados.senhaAutenticacao())) {
            return ResponseEntity.badRequest()
                    .body(ExceptionHandlerChain.handle(TipoException.SENHA_INVALIDA, session, registroRepository));
        }

        if (acaoRepository.findAcoesSigla(dados.siglaAcao()) < 0) {
            return ResponseEntity.badRequest()
                    .body(ExceptionHandlerChain.handle(TipoException.SIGLA_INVALIDA, session, registroRepository));
        }

        if (!cliente.possuiSaldoSuficiente(dados.quantidadeAcoes(), dados.precoAcao())) {
            return ResponseEntity.badRequest()
                    .body(ExceptionHandlerChain.handle(TipoException.SALDO_INSUFICIENTE, session, registroRepository));
        }

        List<VendaOferta> ofertasVenda = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco(dados.siglaAcao(),
                PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
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
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

        if (usuario == null) {
            return ResponseEntity.badRequest()
                    .body(ExceptionHandlerChain.handle(TipoException.SEM_USUARIO, null, registroRepository));
        }

        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());

        if (!cliente.isSenhaAutenticacaoCorreta(dados.senhaAutenticacao())) {
            return ResponseEntity.badRequest()
                    .body(ExceptionHandlerChain.handle(TipoException.SENHA_INVALIDA, session, registroRepository));
        }

        ResponseEntity<String> response = verificarAcoesParaVenda(cliente, dados.quantidadeAcoes(), dados.siglaAcao());
        if (response != null) {
            return response;
        }

        List<CompraOferta> ofertasCompra = compraOfertaRepository.findOfertasCompraBySiglaAndPreco(dados.siglaAcao(),
                PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
        List<Acao> acoesCliente = acaoRepository.findAcoesClienteByClienteIdSigla(cliente.getId(), dados.siglaAcao());

        Iterator<Acao> acoesIteratorAgendarVendaOferta = acoesCliente.iterator();
        if (ofertasCompra.isEmpty()) {
            agendarVendaOferta(dados.quantidadeAcoes(), cliente, dados, acoesIteratorAgendarVendaOferta);
            return ResponseEntity.ok("Aguardando ofertas de compra");
        }

        Iterator<Acao> acoesIteratorAgendarParcialmenteVendaOferta = acoesCliente.iterator();
        if (quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(ofertasCompra.size(), dados.quantidadeAcoes())) {
            agendarVendaOferta(dados.quantidadeAcoes() - ofertasCompra.size(), cliente, dados,
                    acoesIteratorAgendarParcialmenteVendaOferta);
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
            e.printStackTrace();
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações vendidas");
    }

    public List<String[]> buscarAcoesBySigla(String busca, Cliente cliente) {
        if (busca == null || busca.trim().isEmpty()) {
            return vendaOfertaRepository.findOfertasVendaBySiglaAndPreco();
        }

        List<String[]> listaAcoes = vendaOfertaRepository.findOfertaBySiglaOrEmpresaNome(busca);
        List<String[]> listaAcoesFinal = new ArrayList<>();

        for (String[] acao : listaAcoes) {
            String[] acaoFinal = new String[6];
            acaoFinal[0] = acao[1];
            acaoFinal[1] = acao[2];
            acaoFinal[2] = acaoRepository.findQtdAcoesClienteSigla(cliente.getId(), acao[1]);
            if (acaoFinal[2] == null) {
                acaoFinal[2] = "0";
            }
            acaoFinal[3] = acao[3];
            acaoFinal[4] = acao[0];
            acaoFinal[5] = acaoFinal[3];

            listaAcoesFinal.add(acaoFinal);
        }

        return listaAcoesFinal;
    }

    public List<String[]> buscarAcoesByPreco(String preco, Cliente cliente) {
        if (preco == null || preco.trim().isEmpty()) {
            return vendaOfertaRepository.findOfertasVendaBySiglaAndPreco();
        }

        try {
            preco = preco.replaceAll("[^\\d.,]", "");
            preco = preco.replace(',', '.');

            Double precoBusca = Double.parseDouble(preco);

            List<String[]> ofertas = vendaOfertaRepository.findOfertasVendaByPreco(precoBusca);
            List<String[]> acoesFinal = new ArrayList<>();

            for (String[] oferta : ofertas) {
                String[] acaoFinal = new String[6];
                acaoFinal[0] = oferta[1];
                acaoFinal[1] = oferta[2];
                acaoFinal[2] = acaoRepository.findQtdAcoesClienteSigla(cliente.getId(), oferta[1]);
                if (acaoFinal[2] == null) {
                    acaoFinal[2] = "0";
                }
                acaoFinal[3] = oferta[3];
                acaoFinal[4] = oferta[0];
                acaoFinal[5] = acaoFinal[3];

                acoesFinal.add(acaoFinal);
            }

            return acoesFinal;

        } catch (NumberFormatException e) {
            throw new NumberFormatException("Formato inválido para o preço");
        }
    }

    private ResponseEntity<String> verificarAcoesParaVenda(Cliente cliente, int quantidadeParaVender,
            String siglaAcao) {
        Integer qntAcoesClienteSigla = acaoRepository.findQntAcoesBySiglaClienteId(siglaAcao, cliente.getId());

        if (qntAcoesClienteSigla < quantidadeParaVender || qntAcoesClienteSigla == null) {
            return ResponseEntity.badRequest()
                    .body(ExceptionHandlerChain.handle(TipoException.QTD_ACOES_INSUFICIENTE, null, registroRepository));
        }

        return null;
    }

    private boolean quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(Integer quantidadeOferta,
            Integer quantidadeAcoes) {
        return quantidadeOferta > quantidadeAcoes;
    }

    private boolean agendarCompraOferta(Integer quantidadeAgendamento, Cliente cliente, CompraAcoesDTO dados) {
        if (quantidadeAgendamento <= 0) {
            return false;
        }

        for (int i = 0; i < quantidadeAgendamento; i++) {
            CompraOferta oferta = (CompraOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(),
                    Instant.now(), dados.siglaAcao(), null, null, TipoOferta.COMPRA);
            compraOfertaRepository.save(oferta);
        }

        return true;
    }

    private boolean agendarVendaOferta(Integer quantidadeAgendamento, Cliente cliente, VendaAcoesDTO dados,
            Iterator<Acao> acoesIterator) {
        if (quantidadeAgendamento <= 0) {
            return false;
        }

        for (int i = 0; i < quantidadeAgendamento; i++) {
            VendaOferta oferta = (VendaOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(),
                    dados.siglaAcao(), acoesIterator.next().getEmpresa(), acoesIterator.next(), TipoOferta.VENDA);
            vendaOfertaRepository.save(oferta);
        }

        return true;
    }
}

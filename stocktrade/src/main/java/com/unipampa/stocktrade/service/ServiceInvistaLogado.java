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
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ServiceInvistaLogado {

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

    private static final String USUARIO_LOGADO = "usuarioLogado";

    public List<Acao> getAcoesSiglaPrecoIterator() {
        Iterator<String[]> acoesString = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco().iterator();

        AcaoIterator acaoIterator = new AcaoIterator(acoesString);

        return acaoIterator.getAllAcoes();
    }

    public List<String[]> getAcoesSiglaPrecoQuantidadeVenda() {

        List<String[]> acoes = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco();
        List<String[]> acoesFinal = new ArrayList<>();

        for (String[] acao : acoes) {
            String[] acaoFinal = new String[6];

            for (int i = 0; i < acao.length; i++) {
                acaoFinal[i] = acao[i];
            }

            acaoFinal[3] = "0";
            acaoFinal[4] = "0";

            acaoFinal[5] = acao[2];

            acoesFinal.add(acaoFinal);
        }

        return acoesFinal;
    }

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Usuario usuario = clienteRepository.findByEmail(usuarioLogado.getEmail());
        session.setAttribute(USUARIO_LOGADO, usuario);
        return session;
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

        List<VendaOferta> ofertasVenda = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco(dados.siglaAcao(),
                PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
        if (ofertasVenda.isEmpty()) {
            for (int i = 0; i < dados.quantidadeAcoes(); i++) {
                CompraOferta oferta = (CompraOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(),
                        Instant.now(), dados.siglaAcao(), null, null, TipoOferta.COMPRA);
                compraOfertaRepository.save(oferta);
            }
            return ResponseEntity.ok("Aguardando ofertas");
        }

        for (int i = 0; i < dados.quantidadeAcoes() - ofertasVenda.size(); i++) {
            CompraOferta oferta = (CompraOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(),
                    Instant.now(), dados.siglaAcao(), null, null, TipoOferta.COMPRA);
            compraOfertaRepository.save(oferta);
        }

        Iterator<VendaOferta> ofertaIterator = new VendaOfertaIterator(ofertasVenda.iterator());
        try {
            while (ofertaIterator.hasNext()) {
                VendaOferta vendaOferta = ofertaIterator.next();
                Acao acao = vendaOferta.getAcao();

                CompraAcao compraAcao = cliente.comprarAcao(vendaOferta);

                compraAcaoRepository.save(compraAcao); // SALVA A NOVA COMPRAACAO
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
            for (int i = 0; i < dados.quantidadeAcoes(); i++) {
                VendaOferta vendaOferta = (VendaOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), acoesIteratorAgendarVendaOferta.next().getEmpresa(), acoesIteratorAgendarVendaOferta.next(), TipoOferta.VENDA);
                vendaOfertaRepository.save(vendaOferta);
            }
            return ResponseEntity.ok("Aguardando ofertas de compra");
        }

        Iterator<Acao> acoesIteratorAgendarParcialmenteVendaOferta = acoesCliente.iterator();
        for (int i = 0; i < dados.quantidadeAcoes() - ofertasCompra.size(); i++) {
            VendaOferta oferta = (VendaOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), acoesIteratorAgendarParcialmenteVendaOferta.next().getEmpresa(), acoesIteratorAgendarParcialmenteVendaOferta.next(), TipoOferta.VENDA);
            vendaOfertaRepository.save(oferta);
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

    public List<String[]> buscarAcoesBySigla(String busca) {
        if (busca == null || busca.trim().isEmpty()) {
            return vendaOfertaRepository.findOfertasVendaBySiglaAndPreco();
        }

        List<String[]> listaAcoes = vendaOfertaRepository.findOfertaBySiglaOrEmpresaNome(busca);
        List<String[]> listaAcoesFinal = new ArrayList<>();

        for (String[] acao : listaAcoes) {

            String[] acaoFinal = new String[6];

            for (int i = 0; i < acao.length; i++) {
                acaoFinal[i] = acao[i];
            }

            acaoFinal[3] = "0";
            acaoFinal[4] = "0";
            acaoFinal[5] = acao[2];

            listaAcoesFinal.add(acaoFinal);
        }

        return listaAcoesFinal;
    }

    public List<String[]> buscarAcoesByPreco(String preco) {
        if (preco == null || preco.trim().isEmpty()) {
            return vendaOfertaRepository.findOfertasVendaBySiglaAndPreco();
        }

        try {
            Double precoBusca = Double.parseDouble(preco);
            List<String[]> ofertas = vendaOfertaRepository.findOfertasVendaByPreco(precoBusca);
            List<String[]> acoesFinal = new ArrayList<>();

            for (String[] oferta : ofertas) {
                String[] acaoFinal = new String[6];

                for (int i = 0; i < oferta.length; i++) {
                    acaoFinal[i] = oferta[i];
                }
                acaoFinal[3] = "0";
                acaoFinal[4] = "0";

                acoesFinal.add(acaoFinal);
            }

            return acoesFinal;

        } catch (Exception e) {
            throw new NumberFormatException();
        }
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
}

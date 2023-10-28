package com.unipampa.stocktrade.service;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.acao.CompraAcoesDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.acao.iterator.listarAcao.AcaoIterator;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
import com.unipampa.stocktrade.model.entity.oferta.iterator.compraOferta.CompraOfertaIterator;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.model.repository.oferta.OfertaRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class ServiceInvistaLogado {

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private CompraAcaoRepository compraAcaoRepository;

    public List<Acao> getAcoesSiglaPrecoIterator() {
        Iterator<String[]> acoesString = acaoRepository.findAcoesSiglaPreco().iterator();

        AcaoIterator acaoIterator = new AcaoIterator(acoesString);

        return acaoIterator.getAllAcoes();
    }

    public List<String[]> getAcoesSiglaPrecoQuantidadeVenda() {
        return ofertaRepository.findOfertasVendaBySiglaAndPreco();
    }

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = clienteRepository.findByEmail(usuarioLogado.getEmail());
        session.setAttribute("usuarioLogado", usuario);
        return session;
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

        List<Oferta> ofertasVenda = ofertaRepository.findOfertasVendaBySiglaAndPreco(dados.siglaAcao(), PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
        if (ofertasVenda.isEmpty()) {
            for (int i = 0; i < dados.quantidadeAcoes(); i++) {
                Oferta oferta = new Oferta(null, cliente, dados.precoAcao(), Instant.now(), TipoOferta.COMPRA, dados.siglaAcao());
                ofertaRepository.save(oferta);
            }
            return ResponseEntity.ok("Aguardando ofertas");
        }

        for (int i = 0; i < dados.quantidadeAcoes() - ofertasVenda.size(); i++) {
            Oferta oferta = new Oferta(null, cliente, dados.precoAcao(), Instant.now(), TipoOferta.COMPRA, dados.siglaAcao());
            ofertaRepository.save(oferta);
        }

        Iterator<Oferta> ofertaIterator = new CompraOfertaIterator(ofertasVenda.iterator());
        try {
            while (ofertaIterator.hasNext()) {
                Oferta oferta = ofertaIterator.next();
                Acao acao = oferta.getAcao();

                CompraAcao compraAcao = cliente.comprarAcao(oferta);
                compraAcaoRepository.save(compraAcao);

                acaoRepository.save(acao);
                
                ofertaRepository.save(oferta);
                ofertaRepository.deleteById(oferta.getId());
            }
        } catch (Exception e) {
            throw e;
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações compradas");
    }

    public List<String[]> buscarAcoesBySigla(String busca) {
        if (busca == null || busca.trim().isEmpty()) {
            return acaoRepository.findAcoesSiglaPrecoQuantidadeDisponivel();
        }
        List<String[]> listaAcoes = acaoRepository.findAcoesBySiglaOrEmpresaNome(busca);
        return listaAcoes;
    }
}
 
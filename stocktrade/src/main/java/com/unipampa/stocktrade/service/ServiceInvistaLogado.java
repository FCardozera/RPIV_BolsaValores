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
import com.unipampa.stocktrade.model.entity.acao.iterator.compraAcao.CompraAcaoIterator;
import com.unipampa.stocktrade.model.entity.acao.iterator.listarAcao.AcaoIterator;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
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

    private static final String USUARIO_LOGADO = "usuarioLogado";

    public List<Acao> getAcoesSiglaPrecoIterator() {
        Iterator<String[]> acoesString = acaoRepository.findAcoesSiglaPreco().iterator();

        AcaoIterator acaoIterator = new AcaoIterator(acoesString);

        return acaoIterator.getAllAcoes();
    }

    public List<String[]> getAcoesSiglaPrecoQuantidade() {
        return acaoRepository.findAcoesSiglaPrecoQuantidadeDisponivel();
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

        List<Acao> acoesSemCliente = acaoRepository.findAcoesClienteNull(dados.siglaAcao(), PageRequest.of(0, dados.quantidadeAcoes()));
        if (acoesSemCliente.isEmpty()) {
            for (int i = 0; i < dados.quantidadeAcoes(); i++) {
                Oferta oferta = new Oferta(null, null, cliente, 100.0, Instant.now(), TipoOferta.COMPRA);
                ofertaRepository.save(oferta);
            }
            return ResponseEntity.ok("Aguardando ofertas");
        }
        
        Iterator<Acao> acaoIterator = new CompraAcaoIterator(acoesSemCliente.iterator());
        try {
            while (acaoIterator.hasNext()) {
                Acao acao = acaoIterator.next();
                CompraAcao compraAcao = cliente.comprarAcao(acao);
                compraAcaoRepository.save(compraAcao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações compradas");
    }

    public List<String[]> buscarAcoesBySigla(String busca) {
        if (busca == null || busca.trim().isEmpty()) {
            return acaoRepository.findAcoesSiglaPrecoQuantidadeDisponivel();
        }
        return acaoRepository.findAcoesBySiglaOrEmpresaNome(busca);
    }
}
 
package com.unipampa.stocktrade.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class CarteiraService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getEmail());
        session.setAttribute("usuarioLogado", usuario);
        return session;
    }

    public Double variacaoSaldoUsuario24H(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getEmail());
        Double variacaoSaldo = usuario.variacaoSaldo24h();
        return variacaoSaldo;
    }

    public List<String> mesesMovimentacoes1AnoUsuario(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getEmail());
        LinkedHashMap<String, Double> movimentacoesMensais = usuario.movimentacoesMensais1Ano();
        LinkedList<String> listaMeses = new LinkedList<String>();
        for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
            listaMeses.add(entry.getKey());
        }
        return listaMeses;
    }

    public List<Double> saldosFinaisMovimentacoes1AnoUsuario(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getEmail());
        LinkedHashMap<String, Double> movimentacoesMensais = usuario.movimentacoesMensais1Ano();
        LinkedList<Double> listaSaldosFinais = new LinkedList<Double>();
        for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
            listaSaldosFinais.add(entry.getValue());
        }
        return listaSaldosFinais;
    }

    public List<Acao> getAcoesUsuario(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        Usuario usuario = usuarioRepository.findByEmail(usuarioLogado.getEmail());
        Set<Acao> acoesUsuario = usuario.getAcoes();
        List<Acao> listaAcoes = new ArrayList<Acao>();

        if (acoesUsuario.size() != 0) {
            for (Acao acao : acoesUsuario) {
                listaAcoes.add(acao);
            }
            return listaAcoes;
        }
        return null;
    }
}

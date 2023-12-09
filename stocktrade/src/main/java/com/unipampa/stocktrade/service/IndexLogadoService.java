package com.unipampa.stocktrade.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.oferta.VendaOfertaRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class IndexLogadoService {

    @Autowired
    private AcaoRepository acaoRepository;
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaOfertaRepository vendaOfertaRepository;

    
    private static final String USUARIO_LOGADO = "usuarioLogado";

    public List<String[]> getAcoesUser(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        List<String[]> acoesString = acaoRepository.findAcoesClientePrecoMedio(cliente.getId());
        List<String[]> acoes = new ArrayList<>();

        for (String[] acaoQueryBanco : acoesString) {
            String[] acaoFinal = new String[7];

            acaoFinal[0] = acaoQueryBanco[1];
            String[] valorAtualEQuantidadeDisponivel = vendaOfertaRepository
                    .findMenorPrecoOfertaVendaEQuantidadeDisponivelBySigla(acaoQueryBanco[1]).split(",");

            acaoFinal[3] = acaoQueryBanco[3];

            Double valorAtualDouble = Double.parseDouble(valorAtualEQuantidadeDisponivel[0]);
            Double precoMedio = Double.parseDouble(acaoFinal[3]);

            Double variacao = ((valorAtualDouble * 100) / precoMedio) - 100;
            String variacaoFormatada = String.format("%.2f", variacao);
            acaoFinal[4] = variacaoFormatada;
            acaoFinal[5] = valorAtualEQuantidadeDisponivel[1];
            acaoFinal[6] = acaoQueryBanco[0];
            acoes.add(acaoFinal);
        }

        return acoes;
    }

}

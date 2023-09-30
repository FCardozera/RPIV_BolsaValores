package com.unipampa.stocktrade.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;

@Service
public class ServiceInvistaLogado {

    @Autowired
    private AcaoRepository acaoRepository;

    public List<Acao> getAcoesSiglaPreco() {
        List<String[]> acoesString = acaoRepository.findAcoesSiglaPreco();
        List<Acao> acoes = new ArrayList<>();

        for (String[] dadoAcao : acoesString) {
            Acao acao = new Acao(dadoAcao[0], Double.parseDouble(dadoAcao[1]));
            acoes.add(acao);
        }

        return acoes;
    }
    
}

package com.unipampa.stocktrade.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.iterator.AcaoIterator;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;

@Service
public class ServiceInvistaLogado {

    @Autowired
    private AcaoRepository acaoRepository;

    public List<Acao> getAcoesSiglaPrecoIterator() {
        Iterator<String[]> acoesString = acaoRepository.findAcoesSiglaPreco().iterator();

        AcaoIterator acaoIterator = new AcaoIterator(acoesString);

        return acaoIterator.getAllAcoes();
    }

    public List<String[]> getAcoesSiglaPrecoQuantidade() {
        return acaoRepository.findAcoesSiglaPrecoQuantidade();
    }
}
 
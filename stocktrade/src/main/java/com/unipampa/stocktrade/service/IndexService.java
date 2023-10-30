package com.unipampa.stocktrade.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;

@Service
public class IndexService {

    @Autowired
    private AcaoRepository acaoRepository;

    public List<Acao> buscaAcoes() {
        return acaoRepository.findAll();
    }
}

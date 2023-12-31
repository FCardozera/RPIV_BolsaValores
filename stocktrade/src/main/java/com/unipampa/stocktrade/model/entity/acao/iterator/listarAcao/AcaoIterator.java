package com.unipampa.stocktrade.model.entity.acao.iterator.listarAcao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public class AcaoIterator implements IAcaoIterator {

    private Iterator<String[]> acoesStringIterator;
    private List<Acao> acoes = new ArrayList<>();

    public AcaoIterator(Iterator<String[]> acoesStringIterator) {
        this.acoesStringIterator = acoesStringIterator;
    }

    @Override
    public boolean hasNext() {
        return acoesStringIterator.hasNext();
    }

    @Override
    public Acao next() {
        if (hasNext()) {
            String[] dadoAcao = acoesStringIterator.next();
            Acao acao = new Acao(dadoAcao[0]);
            acoes.add(acao); 
            return acao;
        }
        return null;
    }

    public List<Acao> getAllAcoes() {
        while (hasNext()) {
            next(); 
        }
        return acoes;
    }
    
}

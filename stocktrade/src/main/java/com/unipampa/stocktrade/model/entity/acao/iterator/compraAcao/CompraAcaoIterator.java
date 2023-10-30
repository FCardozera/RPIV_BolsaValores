package com.unipampa.stocktrade.model.entity.acao.iterator.compraAcao;

import java.util.Iterator;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public class CompraAcaoIterator implements Iterator<Acao> {

    private Iterator<Acao> acaoIterator;

    public CompraAcaoIterator(Iterator<Acao> acaoIterator) {
        this.acaoIterator = acaoIterator;
    }

    @Override
    public boolean hasNext() {
        return acaoIterator.hasNext();
    }

    @Override
    public Acao next() {
        return acaoIterator.next();
    }
}

package com.unipampa.stocktrade.model.entity.acao.iterator;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public interface IAcaoIterator {
    boolean hasNext();
    Acao next();
}


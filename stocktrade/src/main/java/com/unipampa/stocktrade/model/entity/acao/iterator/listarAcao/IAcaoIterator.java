package com.unipampa.stocktrade.model.entity.acao.iterator.listarAcao;

import com.unipampa.stocktrade.model.entity.acao.Acao;

public interface IAcaoIterator {
    boolean hasNext();
    Acao next();
}


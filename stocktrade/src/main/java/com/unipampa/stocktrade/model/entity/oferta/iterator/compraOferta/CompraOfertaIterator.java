package com.unipampa.stocktrade.model.entity.oferta.iterator.compraOferta;

import java.util.Iterator;

import com.unipampa.stocktrade.model.entity.oferta.CompraOferta;

public class CompraOfertaIterator implements Iterator<CompraOferta> {

    private Iterator<CompraOferta> ofertaIterator;

    public CompraOfertaIterator(Iterator<CompraOferta> ofertaIterator) {
        this.ofertaIterator = ofertaIterator;
    }

    @Override
    public boolean hasNext() {
        return ofertaIterator.hasNext();
    }

    @Override
    public CompraOferta next() {
        CompraOferta oferta = ofertaIterator.next();
        return oferta;
    }
    
}

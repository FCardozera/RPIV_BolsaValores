package com.unipampa.stocktrade.model.entity.oferta.iterator.compraOferta;

import java.util.Iterator;

import com.unipampa.stocktrade.model.entity.oferta.Oferta;

public class CompraOfertaIterator implements Iterator<Oferta> {

    private Iterator<Oferta> ofertaIterator;

    public CompraOfertaIterator(Iterator<Oferta> ofertaIterator) {
        this.ofertaIterator = ofertaIterator;
    }

    @Override
    public boolean hasNext() {
        return ofertaIterator.hasNext();
    }

    @Override
    public Oferta next() {
        Oferta oferta = ofertaIterator.next();
        return oferta;
    }
    
}

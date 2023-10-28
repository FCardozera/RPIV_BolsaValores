package com.unipampa.stocktrade.model.entity.oferta.iterator.vendaOferta;

import java.util.Iterator;

import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;

public class VendaOfertaIterator implements Iterator<VendaOferta> {

    private Iterator<VendaOferta> ofertaIterator;

    public VendaOfertaIterator(Iterator<VendaOferta> ofertaIterator) {
        this.ofertaIterator = ofertaIterator;
    }

    @Override
    public boolean hasNext() {
        return ofertaIterator.hasNext();
    }

    @Override
    public VendaOferta next() {
        VendaOferta oferta = ofertaIterator.next();
        return oferta;
    }
    
}

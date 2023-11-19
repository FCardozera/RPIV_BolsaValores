package com.unipampa.stocktrade.model.entity.usuario.exception.cliente;

public class CpfIncorretoException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public CpfIncorretoException(String msg) {
        super(msg);
    }
    
}

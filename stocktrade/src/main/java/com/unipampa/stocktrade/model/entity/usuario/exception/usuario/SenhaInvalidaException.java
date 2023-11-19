package com.unipampa.stocktrade.model.entity.usuario.exception.usuario;

public class SenhaInvalidaException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public SenhaInvalidaException(String msg) {
        super(msg);
    }
    
}

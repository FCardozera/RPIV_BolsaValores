package com.unipampa.stocktrade.model.entity.usuario.exception.usuario;

public class SenhaIncorretaException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public SenhaIncorretaException(String msg) {
        super(msg);
    }
    
}

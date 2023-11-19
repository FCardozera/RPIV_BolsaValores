package com.unipampa.stocktrade.model.entity.usuario.exception.usuario;

public class EmailInvalidoException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public EmailInvalidoException(String msg) {
        super(msg);
    }
    
}

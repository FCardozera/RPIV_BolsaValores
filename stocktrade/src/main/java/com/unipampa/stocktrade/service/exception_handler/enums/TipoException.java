package com.unipampa.stocktrade.service.exception_handler.enums;

public enum TipoException {

    USUARIO_NAO_AUTORIZADO(0),
    SENHA_INVALIDA(1),
    SEM_USUARIO(2);

    private int codigo;

    TipoException(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoException valueOf(int codigo) {
        for (TipoException tipoException : TipoException.values()) {
            if (tipoException.getCodigo() == codigo) {
                return tipoException;
            }
        } 
        throw new IllegalArgumentException("Código inválido");
    }
}
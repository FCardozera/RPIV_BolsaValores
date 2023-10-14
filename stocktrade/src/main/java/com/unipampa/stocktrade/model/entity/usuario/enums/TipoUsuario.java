package com.unipampa.stocktrade.model.entity.usuario.enums;

public enum TipoUsuario {

    CLIENTE(0),
    ADMIN(1), 
    EMPRESA(2);

    private int codigo;

    TipoUsuario(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoUsuario valueOf(int codigo) {
        for (TipoUsuario tipoUsuario : TipoUsuario.values()) {
            if (tipoUsuario.getCodigo() == codigo) {
                return tipoUsuario;
            }
        } 
        throw new IllegalArgumentException("Código inválido");
    }

}

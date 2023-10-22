package com.unipampa.stocktrade.model.entity.oferta.enums;

public enum TipoOferta {

    VENDA(0),
    COMPRA(1);

    private int codigo;

    TipoOferta(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoOferta valueOf(int codigo) {
        for (TipoOferta tipoOferta : TipoOferta.values()) {
            if (tipoOferta.getCodigo() == codigo) {
                return tipoOferta;
            }
        } 
        throw new IllegalArgumentException("Código inválido");
    }
    
}

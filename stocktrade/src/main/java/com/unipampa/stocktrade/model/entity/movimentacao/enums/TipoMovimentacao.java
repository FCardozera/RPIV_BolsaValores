package com.unipampa.stocktrade.model.entity.movimentacao.enums;

public enum TipoMovimentacao {

    SAQUE(0), 
    DEPOSITO(1),
    TRANSFERENCIA(2),
    DIVIDENDO(3);
    private int codigo;

    TipoMovimentacao(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    public static TipoMovimentacao valueOf(int codigo) {
        for (TipoMovimentacao tipoMovimentacao : TipoMovimentacao.values()) {
            if (tipoMovimentacao.getCodigo() == codigo) {
                return tipoMovimentacao;
            }
        } 
        throw new IllegalArgumentException("Código inválido");
    }
    
}
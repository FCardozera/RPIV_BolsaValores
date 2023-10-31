package com.unipampa.stocktrade.service.exception_handler;

import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

public class ExceptionHandlerChain {

    public static String handle(TipoException tipoException, HttpSession session) {
        ExceptionHandler handlerChain = new UsuarioNaoAutorizadoExceptionHandler()
            .setNextHandler(new SenhaInvalidaExceptionHandler())
            .setNextHandler(new SemUsuarioExceptionHandler());

        return handlerChain.handle(tipoException, session);
    }
}

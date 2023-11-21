package com.unipampa.stocktrade.service.exception_handler;

import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

@Component
public class ExceptionHandlerChain {
    public static String handle(TipoException tipoException, HttpSession session, RegistroRepository repository) {
        ExceptionHandler handlerChain = new UsuarioNaoAutorizadoExceptionHandler();
        ((((((handlerChain.setNextHandler(new SenhaInvalidaExceptionHandler()))
        .setNextHandler(new SemUsuarioExceptionHandler()))
        .setNextHandler(new UsuarioJaExistenteExceptionHandler()))
        .setNextHandler(new SiglaAcaoInvalidaExceptionHandler()))
        .setNextHandler(new SaldoInsuficienteExceptionHandler()))
        .setNextHandler(new QtdAcoesInsuficienteExceptionHandler()))
        .setNextHandler(new AcoesAindaVinculadasExceptionHandler());

        return handlerChain.handle(tipoException, session, repository);
    }
}

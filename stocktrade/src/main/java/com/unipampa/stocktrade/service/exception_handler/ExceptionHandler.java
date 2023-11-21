package com.unipampa.stocktrade.service.exception_handler;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

@Component
public abstract class ExceptionHandler {
    protected ExceptionHandler nextHandler = null;

    public ExceptionHandler setNextHandler(ExceptionHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public String handle(TipoException tipoException, HttpSession session, RegistroRepository registroRepository) {
        if (nextHandler != null) {
            return this.nextHandler.handle(tipoException, session, registroRepository);
        }
        String erro = "Ocorreu um erro que não pôde ser tratado pela cadeia de tratamento.";
        Registro logException = new Registro(null, null, erro, Instant.now());
        registroRepository.save(logException);

        return erro;
    }
}

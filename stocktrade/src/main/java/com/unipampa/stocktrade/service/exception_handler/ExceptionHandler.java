package com.unipampa.stocktrade.service.exception_handler;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

public abstract class ExceptionHandler {
    protected ExceptionHandler nextHandler = null;
    
    @Autowired
    private RegistroRepository registroRepository;

    public ExceptionHandler setNextHandler(ExceptionHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public String handle(TipoException tipoException, HttpSession session) {
        if (nextHandler != null) {
            return this.nextHandler.handle(tipoException, session);
        }
        String erro = "Ocorreu um erro que não pôde ser tratado pela cadeia de tratamento.";
        Registro logException = new Registro(null, null, erro, Instant.now());
        registroRepository.save(logException);

        return erro;
    }
}

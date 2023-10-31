package com.unipampa.stocktrade.service.exception_handler;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

public class SemUsuarioExceptionHandler extends ExceptionHandler {
    @Autowired
    private RegistroRepository registroRepository;

    @Override
    public String handle(TipoException tipoException, HttpSession session) {
        if (tipoException == TipoException.SEM_USUARIO) {
            String erro = "Não foi possível completar a requisição, pois não há usuário um logado.";
            Registro logException = new Registro(null, null, erro, Instant.now());

            registroRepository.save(logException);
            return erro;
        }

        return super.handle(tipoException, session);
    }
}

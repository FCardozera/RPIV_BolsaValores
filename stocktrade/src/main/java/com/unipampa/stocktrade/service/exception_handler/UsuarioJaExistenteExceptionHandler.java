package com.unipampa.stocktrade.service.exception_handler;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

@Component
public class UsuarioJaExistenteExceptionHandler extends ExceptionHandler {
    @Override
    public String handle(TipoException tipoException, HttpSession session, RegistroRepository registroRepository) {
        if (tipoException == TipoException.USUARIO_JA_EXISTENTE) {
            String erro = "Não foi possível completar a requisição, pois já existe um usuário cadastrado com este e-mail.";
            Registro logException = new Registro(null, null, erro, Instant.now());

            registroRepository.save(logException);
            return erro;
        }

        return super.handle(tipoException, session, registroRepository);
    }
}

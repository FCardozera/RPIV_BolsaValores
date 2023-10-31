package com.unipampa.stocktrade.service.exception_handler;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

public class UsuarioNaoAutorizadoExceptionHandler extends ExceptionHandler {
    @Autowired
    private RegistroRepository registroRepository;

    private static final String USUARIO_LOGADO = "usuarioLogado";

    @Override
    public String handle(TipoException tipoException, HttpSession session) {
        if (tipoException == TipoException.USUARIO_NAO_AUTORIZADO) {
            Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
            String erro = "O usuário tentou acessar uma área sem autorização.";
            Registro logException = new Registro(null, usuario.getId(), erro, Instant.now());

            registroRepository.save(logException);
            return erro;
        }

        return super.handle(tipoException, session);
    }
}

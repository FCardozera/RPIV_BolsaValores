package com.unipampa.stocktrade.service.exception_handler;

import java.time.Instant;

import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

@Component
public class AcoesAindaVinculadasExceptionHandler extends ExceptionHandler {
    private static final String USUARIO_LOGADO = "usuarioLogado";

    @Override
    public String handle(TipoException tipoException, HttpSession session, RegistroRepository registroRepository) {
        if (tipoException == TipoException.ACOES_AINDA_VINCULADAS) {
            Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
            String erro = "Ainda existem ações vinculadas a esta conta!";
            Registro logException = new Registro(null, usuario.getId(), erro, Instant.now());

            registroRepository.save(logException);
            return erro;
        }

        return super.handle(tipoException, session, registroRepository);
    }
}

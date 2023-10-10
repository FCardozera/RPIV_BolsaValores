package com.unipampa.stocktrade.aspect.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;

@Aspect
@Component
public class LoginLoggingAspect {

    @Autowired
    private RegistroRepository registroRepository;

    @Pointcut("execution(* com.unipampa.stocktrade.controller.LoginController.login(..))")
    public void login() {
    }

    @AfterReturning("login()")
    public void logOperationLogarUsuario(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        UsuarioRequestDTO usuarioRequestDTO = (UsuarioRequestDTO) args[0];

        Registro registro = new Registro("Login com sucesso com o e-mail " + usuarioRequestDTO.email());

        registroRepository.save(registro);
    }

    @AfterThrowing(pointcut = "login()", throwing = "exception")
    public void logOperationLogarUsuarioException(JoinPoint joinPoint, Exception exception) {
        Object[] args = joinPoint.getArgs();

        UsuarioRequestDTO usuarioRequestDTO = (UsuarioRequestDTO) args[0];

        Registro registro = new Registro("Tentativa mal sucedida de login com e-mail " + usuarioRequestDTO.email() + ": " + exception.getMessage());

        registroRepository.save(registro);
    }


}

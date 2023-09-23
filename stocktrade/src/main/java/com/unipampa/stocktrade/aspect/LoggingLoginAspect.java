package com.unipampa.stocktrade.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.controller.DTO.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;

@Aspect
@Component
public class LoggingLoginAspect {

    @Autowired
    private RegistroRepository registroRepository;

    public LoggingLoginAspect(RegistroRepository registroRepository) {
        this.registroRepository = registroRepository;
    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.ControllerLogin.login(..))")
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

        Registro registro = new Registro("Tentativa mal sucedida de login com e-mail " + usuarioRequestDTO.email() + " falhou: " + exception.getMessage());

        registroRepository.save(registro);
    }


}

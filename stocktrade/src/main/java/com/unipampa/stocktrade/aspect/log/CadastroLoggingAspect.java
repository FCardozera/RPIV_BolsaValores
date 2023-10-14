package com.unipampa.stocktrade.aspect.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;

@Aspect
@Component
public class CadastroLoggingAspect {

    @Autowired
    private RegistroRepository registroRepository;

    @Pointcut("execution(* com.unipampa.stocktrade.controller.CadastroController.cadastrarUsuario(..))")
    public void cadastrarUsuario() {
    }

    @AfterReturning("cadastrarUsuario()")
    public void logOperationCadastrarUsuario(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();

        ClienteRequestDTO usuarioRequestDTO = (ClienteRequestDTO) args[0];

        Registro registro = new Registro("Cadastro sucedido com o e-mail " + usuarioRequestDTO.email() + " e nome " + usuarioRequestDTO.nome());

        registroRepository.save(registro);
    }

    @AfterThrowing(pointcut = "cadastrarUsuario()", throwing = "exception")
    public void logOperationCadastrarUsuarioError(JoinPoint joinPoint, Exception exception) {
        Object[] args = joinPoint.getArgs();

        ClienteRequestDTO usuarioRequestDTO = (ClienteRequestDTO) args[0];

        Registro registro = new Registro("Erro ao cadastrar usuário com e-mail " + usuarioRequestDTO.email() + " e nome " + usuarioRequestDTO.nome() + ": " + exception.getMessage());

        registroRepository.save(registro);
    }

}

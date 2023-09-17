package com.unipampa.stocktrade.aspect;

import org.aspectj.lang.annotation.AfterReturning;

import java.time.Instant;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class LoggingAspect {

    private final RegistroRepository registroRepository;
    private final HttpSession session;

    @Autowired
    public LoggingAspect(RegistroRepository registroRepository, HttpSession session) {
        this.registroRepository = registroRepository;
        this.session = session;
    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.ControllerCadastro.*(..))")
    public void cadastroMetodos(){

    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.ControllerLogin.*(..))")
    public void loginMetodos(){

    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.ControllerInvista.*(..))")
    public void invistaMetodos(){

    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.ControllerContato.*(..))")
    public void contatoMetodos(){

    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.ControllerSobre.*(..))")
    public void sobreMetodos(){

    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.ControllerIndex.*(..))")
    public void indexMetodos(){

    }


    @AfterReturning("cadastroMetodos() || indexMetodos() || loginMetodos()")
    public void logOperation(JoinPoint joinPoint){
        String atividade = joinPoint.getSignature().getName();

        Registro registro = new Registro();
        registro.setAtividade(atividade);
 
        Instant timestamp = Instant.now();
        registro.setInstant(timestamp);
        
        UUID usuarioId = null;
        // Object usuarioLogado = session.getAttribute("usuarioLogado");
        // if (usuarioLogado != null) {
        //     usuarioId = registroRepository.findByUsuarioId(usuarioLogado.getId());
        // }
        
        registro.setUsuarioId(usuarioId);

        registroRepository.save(registro);
    }

}

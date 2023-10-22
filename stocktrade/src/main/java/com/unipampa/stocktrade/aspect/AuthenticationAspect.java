package com.unipampa.stocktrade.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthenticationAspect {

    @Pointcut("execution(* com.unipampa.stocktrade.controller.LoginController.loginPagina(..)) ||" +
              "execution(* com.unipampa.stocktrade.controller.IndexController.index(..)) ||" +
              "execution(* com.unipampa.stocktrade.controller.CadastroController.cadastroPagina(..))")
    public void notLoggedPointcut() {
    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.IndexLogadoController.indexLogado(..)) ||" +
    "execution(* com.unipampa.stocktrade.controller.CarteiraController.carteiraPagina(..)) ||" +
    "execution(* com.unipampa.stocktrade.controller.PerfilController.perfilPagina(..)) ||" + 
    "execution(* com.unipampa.stocktrade.controller.InvistaLogadoController.invistaLogado(..))")
    public void loggedPointcut() {
    }

    @Around("notLoggedPointcut()")
    public Object aroundLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = (HttpSession) joinPoint.getArgs()[0];

        if (session.getAttribute("usuarioLogado") != null) {
            ModelAndView mv = new ModelAndView("indexLogado");
            System.out.println("Você já está logado");
            return mv;
        }

        return joinPoint.proceed();
    }

    @Around("loggedPointcut()")
    public Object aroundLogged(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = (HttpSession) joinPoint.getArgs()[0];

        if (session.getAttribute("usuarioLogado") == null) {
            ModelAndView mv = new ModelAndView("index");
            System.out.println("Você não está logado");
            return mv;
        }

        return joinPoint.proceed();
    }

}

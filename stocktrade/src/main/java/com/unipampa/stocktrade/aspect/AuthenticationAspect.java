package com.unipampa.stocktrade.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.model.entity.registro.Registro;
import com.unipampa.stocktrade.model.entity.usuario.Admin;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthenticationAspect {

    @Autowired 
    RegistroRepository registroRepository;

    private static final String USUARIO_LOGADO = "usuarioLogado";

    @Pointcut("execution(* com.unipampa.stocktrade.controller.LoginController.loginPagina(..)) ||" +
              "execution(* com.unipampa.stocktrade.controller.IndexController.index(..)) ||" +
              "execution(* com.unipampa.stocktrade.controller.CadastroController.cadastroPagina(..))")
    public void notLoggedPointcut() {
    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.IndexLogadoController.indexLogado(..)) ||" +
    "execution(* com.unipampa.stocktrade.controller.CarteiraController.carteiraPagina(..)) ||" +
    "execution(* com.unipampa.stocktrade.controller.PerfilController.perfilPagina(..)) ||" + 
    "execution(* com.unipampa.stocktrade.controller.InvistaLogadoController.invistaLogado(..)) ||" +
    "execution(* com.unipampa.stocktrade.controller.HistoricoController.historicoPagina(..))"
    )
    public void loggedPointcut() {
    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.DividendoController.dividendoPagina(..)) ||" +
              "execution(* com.unipampa.stocktrade.controller.IndexAdmController.indexAdm(..))")
    public void loggedAdminPointCut() {
    }

    @Around("notLoggedPointcut()")
    public Object aroundLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = (HttpSession) joinPoint.getArgs()[0];

        if (session.getAttribute(USUARIO_LOGADO) != null) {
            ModelAndView mv = new ModelAndView("indexLogado");
            Usuario user = (Usuario) session.getAttribute(USUARIO_LOGADO);
            // Logging...
            Registro registro = new Registro("O usuário " + user.getEmail() + " acessou uma página indevida já estando logado.");
            registroRepository.save(registro);
            return mv;
        }

        return joinPoint.proceed();
    }

    @Around("loggedPointcut()")
    public Object aroundLogged(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = (HttpSession) joinPoint.getArgs()[0];

        if (session.getAttribute(USUARIO_LOGADO) == null) {
            ModelAndView mv = new ModelAndView("index");
            // Logging...
            Registro registro = new Registro("Tentativa de acesso inválida pois não está logado.");
            registroRepository.save(registro);
            return mv;
        }

        Usuario user = (Usuario) session.getAttribute(USUARIO_LOGADO);

        if (user instanceof Admin) {
            ModelAndView mv = new ModelAndView("indexAdm");
            // Logging...
            Registro registro = new Registro("O usuário " + user.getEmail() + " tentou acessar uma página de cliente.");
            registroRepository.save(registro);
            return mv;
        }

        return joinPoint.proceed();
    }

    @Around("loggedAdminPointCut()")
    public Object aroundLoggedAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpSession session = (HttpSession) joinPoint.getArgs()[0];

        if (session.getAttribute(USUARIO_LOGADO) == null) {
            ModelAndView mv = new ModelAndView("index");
            // Logging...
            Registro registro = new Registro("Tentativa de acesso inválida pois não está logado.");
            registroRepository.save(registro);
            return mv;
        }

        Usuario user = (Usuario) session.getAttribute(USUARIO_LOGADO);

        if (!(user instanceof Admin)) {
            ModelAndView mv = new ModelAndView("indexLogado");
            // Logging...
            Registro registro = new Registro("O usuário " + user.getEmail() + " tentou acessar uma página de administrador.");
            registroRepository.save(registro);
            return mv;
        }

        return joinPoint.proceed();
    }

}

package com.unipampa.stocktrade.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.dto.usuario.UsuarioRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.service.LoginService;

import jakarta.servlet.http.HttpSession;

@Aspect
@Component
public class AuthenticationAspect {
    private final HttpSession session;

    public AuthenticationAspect(HttpSession session) {
        this.session = session;
    }

    @Autowired
    private LoginService serviceLogin;

    @Pointcut("execution(* com.unipampa.stocktrade.controller.LoginController.login(..))")
    public void loginPointcut() {
    }

    @Pointcut("execution(* com.unipampa.stocktrade.controller.IndexLogadoController.*(..)) ||" +
    "execution(* com.unipampa.stocktrade.controller.CarteiraController.*(..)) ||" +
    "execution(* com.unipampa.stocktrade.controller.PerfilController.*(..))")
    public void loggedPointcut() {
    }

    @Around("loginPointcut()")
    public Object aroundLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        UsuarioRequestDTO usuarioRequestDTO = (UsuarioRequestDTO) args[0];
        Usuario userLogin = serviceLogin.login(usuarioRequestDTO);

        if (userLogin != null) {
            session.setAttribute("usuarioLogado", userLogin);
            System.out.println("Login realizado com sucesso!");
        }

        return joinPoint.proceed();
    }

    @Around("loggedPointcut()")
    public Object aroundLogged(ProceedingJoinPoint joinPoint) throws Throwable {
        ModelAndView mv = new ModelAndView("/login");
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado == null) {
            System.out.println("Deve haver um usuário logado para acessar esta página");
            return mv;
        }
        return joinPoint.proceed();
    }

    @Around("execution(* com.unipampa.stocktrade.controller.LoginController.loginPagina(..)) ||" + 
    "execution(* com.unipampa.stocktrade.controller.CadastroController.cadastroPagina(..)) ||" + 
    "execution(* com.unipampa.stocktrade.controller.IndexController.*(..))")
    public Object aroundLoginCadastroPagina(ProceedingJoinPoint joinPoint) throws Throwable {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        if (usuarioLogado != null) {
            ModelAndView mv = new ModelAndView("indexLogado");
            System.out.println("Você não pode estar logado");
            return mv;
        }
        return joinPoint.proceed();
    }
}

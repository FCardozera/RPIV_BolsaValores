package com.unipampa.stocktrade;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.exception.usuario.SenhaIncorretaException;
import com.unipampa.stocktrade.service.CadastroService;
import com.unipampa.stocktrade.service.LoginService;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
public class LoginUserTests {

    @Autowired
    private LoginService loginService;

    @Autowired
    private CadastroService cadastroService; 

    @Autowired
    private HttpSession httpSession;

    @Test
    public void testLoginUser() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO(null, "11111111111", "joao@gmail.com", "12345678",
                "1234", null, null);

        cadastroService.salvarCliente(clienteDTO);

        loginService.login(clienteDTO, httpSession);

        assert(httpSession.isNew());
    }

    @Test
    public void testLoginIncorretoUser() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO(null, "11111111111", "joao@gmail.com", "12345678",
                "1234", null, null);
        cadastroService.salvarCliente(clienteDTO);

        ClienteRequestDTO clienteDTOSenhaIncorreta = new ClienteRequestDTO(null, "11111111111", "joao@gmail.com", "87654321",
                "1234", null, null);

        Exception e = null;
        
        try {
            loginService.login(clienteDTOSenhaIncorreta, httpSession);
        } catch (SenhaIncorretaException err) {
            e = err;
        }

        assertNotNull(e);
    }

    @Test
    public void testLoginIncorretoUserSenha() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO(null, "11111111111", "joao@gmail.com", "12345678",
                "1234", null, null);
        cadastroService.salvarCliente(clienteDTO);

        ClienteRequestDTO clienteDTOSenhaIncorreta = new ClienteRequestDTO(null, "11111111111", "joao@gmail.com", "senhaLetra",
                "1234", null, null);

        Exception e = null;
        
        try {
            loginService.login(clienteDTOSenhaIncorreta, httpSession);
        } catch (SenhaIncorretaException err) {
            e = err;
        }

        assertNotNull(e);
    }

}
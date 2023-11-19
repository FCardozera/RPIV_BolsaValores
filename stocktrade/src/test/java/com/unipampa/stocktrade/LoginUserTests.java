package com.unipampa.stocktrade;


import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.service.LoginService;
import jakarta.servlet.http.HttpSession;

@SpringBootTest
public class LoginUserTests {

    @Autowired
    private LoginService loginService;

    @Autowired
    private HttpSession httpSession;

    @Test
    public void testLoginUser() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO(null, null, "joao@gmail.com", "12345678",
                null, null, null);

        loginService.login(clienteDTO, httpSession);

        assert(httpSession.isNew());
    }

    @Test
    public void testLoginIncorretoUser() {
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO(null, null, "joao@gmail.com", "87654321",
                null, null, null);
        Exception e = null;
        
        try {
            loginService.login(clienteDTO, httpSession);
        } catch (Exception err) {
            e = err;
        }

        assertNotNull(e);
    }
}



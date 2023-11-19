package com.unipampa.stocktrade;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.exception.cliente.CpfIncorretoException;
import com.unipampa.stocktrade.model.entity.usuario.exception.usuario.EmailInvalidoException;
import com.unipampa.stocktrade.model.entity.usuario.exception.usuario.SenhaInvalidaException;
import com.unipampa.stocktrade.service.CadastroService;

@SpringBootTest
public class CadastroUserTests {

	@Autowired
	private CadastroService cadastroService;

	@Test
	public void testCriarUsuario() {
		ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Nome", "12345678901", "test@mail.com", "12345678",
				"1234", null, null);
		Cliente cliente = new Cliente(clienteDTO);
	
		cadastroService.salvarCliente(clienteDTO);

		assert(cliente.getNome().equals(clienteDTO.nome()));
		assert(cliente.getCpf().equals(clienteDTO.cpf()));
		assert(cliente.getEmail().equals(clienteDTO.email()));
		assert(cliente.isSenhaCorreta(clienteDTO.senha()));
		assert(cliente.isSenhaAutenticacaoCorreta(clienteDTO.senhaAutenticacao()));
	}

	@Test
	public void testCriarUsuarioSenhaErrada() {
		ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Nome", "12345678901", "test@mail.com", "12345678",
				"1234", null, null);
		Cliente cliente = new Cliente(clienteDTO);
	
		cadastroService.salvarCliente(clienteDTO);

		assert(cliente.getNome().equals(clienteDTO.nome()));
		assert(cliente.getCpf().equals(clienteDTO.cpf()));
		assert(cliente.getEmail().equals(clienteDTO.email()));
		assert(!cliente.isSenhaCorreta("12345666"));
		assert(cliente.isSenhaAutenticacaoCorreta(clienteDTO.senhaAutenticacao()));
	}

	@Test
	public void testCriarUsuarioCpfErrado(){
        ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Nome", "012345678901", "test@mail.com", "12345678", "1234", null, null);
	
		Exception e = null;
        
        try {
            cadastroService.salvarCliente(clienteDTO);
        } catch (CpfIncorretoException err) {
            e = err;
        }

        assertNotNull(e);
	}

	@Test
	public void testCriarUsuarioEmailErrado(){
		ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Nome", "12345678901", "testmail.com", "12345678", "1234", null, null);

		Exception e = null;
		
		try {
			cadastroService.salvarCliente(clienteDTO);
		} catch (EmailInvalidoException err) {
			e = err;
		}

		assertNotNull(e);
	}

	@Test
	public void testCriarUsuarioSenhaLetras(){
		ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Nome", "12345678901", "test@mail.com", "senhaLetra", "1234", null, null);

		Exception e = null;

		try {
			cadastroService.salvarCliente(clienteDTO);
		} catch (SenhaInvalidaException err) {
			e = err;
		}

		assertNotNull(e);
	}

	@Test
	public void testCriarUsuarioSenhaTamanho(){
		ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Nome", "12345678901", "test@mail.com", "1234567800000000", "1234", null, null);

		Exception e = null;

		try {
			cadastroService.salvarCliente(clienteDTO);
		} catch (SenhaInvalidaException err) {
			e = err;
		}

		assertNotNull(e);
	}

	@Test
	public void testCriarUsuarioSenhaAutenticacaoTamanho(){
		ClienteRequestDTO clienteDTO = new ClienteRequestDTO("Nome", "12345678901", "test@mail.com", "12345678", "12341111111", null, null);

		Exception e = null;

		try {
			cadastroService.salvarCliente(clienteDTO);
		} catch (SenhaInvalidaException err) {
			e = err;
		}

		assertNotNull(e);
	}
}
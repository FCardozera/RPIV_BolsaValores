package com.unipampa.stocktrade.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.usuario.Admin;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.repository.usuario.AdminRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;
import com.unipampa.stocktrade.model.repository.usuario.EmpresaRepository;

import jakarta.servlet.http.HttpSession;

@Service
public class PerfilService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private EmpresaRepository empresaRepository;
    @Autowired
    private AdminRepository adminRepository;

    public void deleteConta(HttpSession session, ClienteRequestDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }

        if (!usuario.isSenhaCorreta(dados.senha())) {
            throw new RuntimeException("Senha incorreta");
        }

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            if (!cliente.getAcoes().isEmpty()) {
                throw new RuntimeException("Não é possível deletar a conta pois existem ações vinculadas a ela");
            }
            clienteRepository.delete(cliente);
        } else if (usuario instanceof Empresa) {
            Empresa empresa = (Empresa) usuario;
            if (!empresa.getAcoes().isEmpty()) {
                throw new RuntimeException("Não é possível deletar a conta pois existem ações vinculadas a ela");
            }
            empresaRepository.delete(empresa);
        } else if (usuario instanceof Admin) {
            Admin admin = (Admin) usuario;
            adminRepository.delete(admin);
        }

        session.invalidate();

        System.out.println("Usuário deletado com sucesso");
    }

    public void trocarEmail(HttpSession session, ClienteRequestDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (usuario == null) {
            throw new RuntimeException("Não existe um usuário logado");
        }

        if (dados.email() != null && !dados.email().isEmpty() && !dados.email().equals(usuario.getEmail())) {
            usuario.trocarEmail(dados.email());
        }

        if (dados.newEmail() != null && !dados.newEmail().isEmpty() && !dados.newEmail().equals(usuario.getEmail())) {
            usuario.trocarEmail(dados.newEmail());
        }

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            clienteRepository.save(cliente);
        } else if (usuario instanceof Empresa) {
            Empresa empresa = (Empresa) usuario;
            empresaRepository.save(empresa);
        } else if (usuario instanceof Admin) {
            Admin admin = (Admin) usuario;
            adminRepository.save(admin);
        }

        System.out.println("Email trocado com sucesso");

    }

    public void trocarSenha(HttpSession session, ClienteRequestDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        if (dados.senha() != null && dados.newPassword() != null && !dados.senha().isEmpty()
                && !dados.newPassword().isEmpty()) {
            if (!usuario.isSenhaCorreta(dados.senha())) {
                throw new RuntimeException("Senha atual incorreta");
            }
            usuario.trocarSenha(dados.newPassword());
        }

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            clienteRepository.save(cliente);
        } else if (usuario instanceof Empresa) {
            Empresa empresa = (Empresa) usuario;
            empresaRepository.save(empresa);
        } else if (usuario instanceof Admin) {
            Admin admin = (Admin) usuario;
            adminRepository.save(admin);
        }
        
        System.out.println("Senha trocada com sucesso");
    }

    
}
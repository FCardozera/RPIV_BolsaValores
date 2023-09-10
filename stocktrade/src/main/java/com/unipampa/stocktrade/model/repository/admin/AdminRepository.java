package com.unipampa.stocktrade.model.repository.admin;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.unipampa.stocktrade.model.entity.usuario.admin.Admin;

public interface AdminRepository extends JpaRepository<Admin, UUID> {

    @Query("select i from admin i where i.email = :email")
    public Admin findByEmail(String email);

    @Query("select j from admin j where j.email = :email and j.senha = :senha")
    public Admin buscarLogin(String email, String senha);
    
}
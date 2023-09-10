package com.unipampa.stocktrade.model.entity.empresa;


import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import com.unipampa.stocktrade.model.entity.acao.Acao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "empresa")
@Table(name = "tb_empresa")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Empresa implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true)
    private String cnpj;

    @OneToMany(mappedBy = "empresa")
    private Set<Acao> acoes;
}
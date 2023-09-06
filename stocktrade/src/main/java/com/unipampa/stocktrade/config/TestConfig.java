package com.unipampa.stocktrade.config;

import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.unipampa.stocktrade.domain.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.domain.entity.movimentacao.enums.TipoMovimentacao;
import com.unipampa.stocktrade.repository.movimentacao.MovimentacaoRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Override
    public void run(String... args) throws Exception {

        // Instant instant1 = Instant.parse("2021-03-25T19:53:07Z");
        // Instant instant2 = Instant.parse("2021-12-29T19:53:07Z");

        // Movimentacao mov1 = new Movimentacao(null, 20.0, instant1, TipoMovimentacao.DEPOSITO);
        // Movimentacao mov2 = new Movimentacao(null, 10.0, instant2, TipoMovimentacao.SAQUE);

        // movimentacaoRepository.saveAll(List.of(mov1, mov2));

    }

}
package com.unipampa.stocktrade.config;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.unipampa.stocktrade.domain.entity.acao.Acao;
import com.unipampa.stocktrade.domain.entity.acao.CompraAcao;
import com.unipampa.stocktrade.domain.entity.acao.VendaAcao;
import com.unipampa.stocktrade.domain.entity.empresa.Empresa;
import com.unipampa.stocktrade.domain.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.domain.entity.movimentacao.enums.TipoMovimentacao;
import com.unipampa.stocktrade.domain.entity.usuario.cliente.Cliente;
import com.unipampa.stocktrade.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.repository.acao.VendaAcaoRepository;
import com.unipampa.stocktrade.repository.cliente.ClienteRepository;
import com.unipampa.stocktrade.repository.empresa.EmpresaRepository;
import com.unipampa.stocktrade.repository.movimentacao.MovimentacaoRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private CompraAcaoRepository compraAcaoRepository;

    @Autowired
    private VendaAcaoRepository vendaAcaoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    

    @Override
    public void run(String... args) throws Exception {

        compraAcaoRepository.deleteAll();
        vendaAcaoRepository.deleteAll();
        acaoRepository.deleteAll();
        movimentacaoRepository.deleteAll();
        clienteRepository.deleteAll();
        empresaRepository.deleteAll();

        Instant instant1 = Instant.parse("2021-03-25T19:53:07Z");
        Instant instant2 = Instant.parse("2021-12-29T19:53:07Z");

        Cliente cliente1 = new Cliente("Ricardo", "444444444351", "ricardo@gmail.com", "123456", "123456", 20.0, true);
        Cliente cliente2 = new Cliente("Felipe", "444485464352", "felipe@gmail.com", "123456", "123456", 4444444444.0,
                true);

        clienteRepository.saveAll(List.of(cliente1, cliente2));

        Movimentacao mov1 = new Movimentacao(null, 20.0, instant1, TipoMovimentacao.DEPOSITO, cliente1);
        Movimentacao mov2 = new Movimentacao(null, 10.0, instant2, TipoMovimentacao.SAQUE, cliente2);

        movimentacaoRepository.saveAll(List.of(mov1, mov2));

        Empresa empresa1 = new Empresa(null, "Petrobras LTDA", "0001544987", null);
        Empresa empresa2 = new Empresa(null, "Vale LTDA", "1231231233", null);

        empresaRepository.saveAll(List.of(empresa1, empresa2));

        Set<Acao> acoesEmpresa1 = new HashSet<>();

        for (int i = 0; i < 70; i++) {
            Acao acao = new Acao(null, "PETR4", 20.0, empresa1, null, null);
            acoesEmpresa1.add(acao);
        }

        Set<Acao> acoesEmpresa2 = new HashSet<>();

        for (int i = 0; i < 50; i++) {
            Acao acao = new Acao(null, "VALE5", 20.0, empresa2, null, null);
            acoesEmpresa2.add(acao);
        }

        acaoRepository.saveAll(acoesEmpresa1);
        acaoRepository.saveAll(acoesEmpresa2);

        // Cliente 1 comprou 20 Ações da PETR4
        int count = 0;
        for (Acao acao : acoesEmpresa1) {
            if (count < 20) {
                if (acao.getCliente() == null) {
                    acao.setCliente(cliente1);
                    CompraAcao compraAcao = new CompraAcao(null, acao, cliente1, acao.getValor(), instant1);
                    compraAcaoRepository.save(compraAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }

        // Cliente 2 comprou 40 Ações da PETR4
        count = 0;
        for (Acao acao : acoesEmpresa1) {
            if (count < 40) {
                if (acao.getCliente() == null) {
                    acao.setCliente(cliente2);
                    CompraAcao compraAcao = new CompraAcao(null, acao, cliente2, acao.getValor(), instant2);
                    compraAcaoRepository.save(compraAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }

        // Cliente 1 comprou 20 Ações da VALE5
        count = 0;
        for (Acao acao : acoesEmpresa2) {
            if (count < 20) {
                if (acao.getCliente() == null) {
                    acao.setCliente(cliente1);
                    CompraAcao compraAcao = new CompraAcao(null, acao, cliente1, acao.getValor(), instant2);
                    compraAcaoRepository.save(compraAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }

        // Cliente 2 comprou 10 Ações da VALE5
        count = 0;
        for (Acao acao : acoesEmpresa2) {
            if (count < 10) {
                if (acao.getCliente() == null) {
                    acao.setCliente(cliente2);
                    CompraAcao compraAcao = new CompraAcao(null, acao, cliente2, acao.getValor(), instant1);
                    compraAcaoRepository.save(compraAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }


        // Cliente 1 vendeu 10 Ações da VALE5
        count = 0;
        for (Acao acao : acoesEmpresa2) {
            if (count < 10) {
                if (acao.getCliente() == null) {
                    acao.setCliente(cliente2);
                    VendaAcao vendaAcao = new VendaAcao(null, acao, cliente2, acao.getValor(), instant1);
                    vendaAcaoRepository.save(vendaAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }

        

        // System.out.println(clienteRepository.findByEmail("ricardo@gmail.com").getAcoes().toString());
    }

}
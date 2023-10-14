package com.unipampa.stocktrade.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.acao.VendaAcao;
import com.unipampa.stocktrade.model.entity.empresa.Empresa;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.movimentacao.enums.TipoMovimentacao;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.VendaAcaoRepository;
import com.unipampa.stocktrade.model.repository.empresa.EmpresaRepository;
import com.unipampa.stocktrade.model.repository.movimentacao.MovimentacaoRepository;
import com.unipampa.stocktrade.model.repository.usuario.UsuarioRepository;

@Configuration
@Profile("test")
public class TestConfig implements CommandLineRunner {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        usuarioRepository.deleteAll();
        empresaRepository.deleteAll();

        Instant instant1 = Instant.now();
        Instant instant2 = (LocalDateTime.of(2022, Month.AUGUST, 02, 12, 0)).toInstant(ZoneOffset.UTC);
        Instant instant3 = (LocalDateTime.of(2022, Month.DECEMBER, 31, 23, 0)).toInstant(ZoneOffset.UTC);
        Instant instant4 = (LocalDateTime.of(2023, Month.JUNE, 15, 23, 0)).toInstant(ZoneOffset.UTC);
        Instant instant5 = (LocalDateTime.of(2023, Month.JULY, 10, 12, 0)).toInstant(ZoneOffset.UTC);
        Instant instant6 = (LocalDateTime.of(2023, Month.AUGUST, 01, 12, 0)).toInstant(ZoneOffset.UTC);

        Usuario usuario1 = new Usuario(null, "Ricardo", "44444444435", "ricardo@gmail.com", "12345678", "1234", TipoUsuario.CLIENTE);
        Usuario usuario2 = new Usuario(null, "Felipe", "44424444435", "felipe@gmail.com", "12345678", "1234", TipoUsuario.ADMIN);

        Movimentacao mov1 = new Movimentacao(null, 15000.0, instant1, TipoMovimentacao.DEPOSITO, usuario1);
        usuario1.setSaldo(usuario1.getSaldo() + 15000.0);

        Movimentacao mov2 = new Movimentacao(null, 25000.0, instant2, TipoMovimentacao.DEPOSITO, usuario2);
        usuario2.setSaldo(usuario2.getSaldo() + 25000.0);

        Movimentacao mov3 = new Movimentacao(null, 5500.0, instant3, TipoMovimentacao.SAQUE, usuario2);
        usuario2.setSaldo(usuario2.getSaldo() - 5500.0);

        Movimentacao mov4 = new Movimentacao(null, 200.0, instant4, TipoMovimentacao.TRANSFERENCIA, usuario2);
        usuario2.setSaldo(usuario2.getSaldo() - 200.0);

        Movimentacao mov5 = new Movimentacao(null, 850.0, instant5, TipoMovimentacao.DEPOSITO, usuario2);
        usuario2.setSaldo(usuario2.getSaldo() + 850.0);

        Movimentacao mov6 = new Movimentacao(null, 60.55, instant6, TipoMovimentacao.DIVIDENDO, usuario2);
        usuario2.setSaldo(usuario2.getSaldo() + 60.55);

        usuarioRepository.saveAll(List.of(usuario1, usuario2));

        movimentacaoRepository.saveAll(List.of(mov1, mov2, mov3, mov4, mov5, mov6));

        Empresa empresa1 = new Empresa(null, "Petrobras LTDA", "53048280000174", null);
        Empresa empresa2 = new Empresa(null, "Vale LTDA", "50951271000109", null);

        empresaRepository.saveAll(List.of(empresa1, empresa2));

        Set<Acao> acoesEmpresa1 = new HashSet<>();

        for (int i = 0; i < 70; i++) {
            Acao acao = new Acao(null, "PETR4", 20.0, empresa1, null, null, null, null);
            acoesEmpresa1.add(acao);
        }

        Set<Acao> acoesEmpresa2 = new HashSet<>();

        for (int i = 0; i < 50; i++) {
            Acao acao = new Acao(null, "VALE5", 20.0, empresa2, null, null, null, null);
            acoesEmpresa2.add(acao);
        }

        acaoRepository.saveAll(acoesEmpresa1);
        acaoRepository.saveAll(acoesEmpresa2);

        // Cliente 1 comprou 20 Ações da PETR4
        int count = 0;
        for (Acao acao : acoesEmpresa1) {
            if (count < 20) {
                if (acao.getUsuario() == null) {
                    acao.setUsuario(usuario1);
                    CompraAcao compraAcao = new CompraAcao(null, acao, usuario1, acao.getValor(), instant1);
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
                if (acao.getUsuario() == null) {
                    acao.setUsuario(usuario2);
                    CompraAcao compraAcao = new CompraAcao(null, acao, usuario2, acao.getValor(), instant2);
                    compraAcaoRepository.save(compraAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }

        // Cliente 2 comprou 10 Ações da PETR4
        count = 0;
        for (Acao acao : acoesEmpresa1) {
            if (count < 10) {
                if (acao.getUsuario() == null) {
                    acao.setUsuario(usuario2);
                    CompraAcao compraAcao = new CompraAcao(null, acao, usuario2, 70.0, instant2);
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
                if (acao.getUsuario() == null) {
                    acao.setUsuario(usuario1);
                    CompraAcao compraAcao = new CompraAcao(null, acao, usuario1, acao.getValor(), instant2);
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
                if (acao.getUsuario() == null) {
                    acao.setUsuario(usuario2);
                    CompraAcao compraAcao = new CompraAcao(null, acao, usuario2, acao.getValor(), instant1);
                    compraAcaoRepository.save(compraAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }

        // Cliente 2 comprou 20 Ações da VALE5
        count = 0;
        for (Acao acao : acoesEmpresa2) {
            if (count < 20) {
                if (acao.getUsuario() == null) {
                    acao.setUsuario(usuario2);
                    CompraAcao compraAcao = new CompraAcao(null, acao, usuario2, 45.0, instant1);
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
                if (acao.getUsuario() == null) {
                    acao.setUsuario(usuario2);
                    VendaAcao vendaAcao = new VendaAcao(null, acao, usuario2, acao.getValor(), instant1);
                    vendaAcaoRepository.save(vendaAcao);
                    acaoRepository.save(acao);
                    count++;
                }
            }
        }

        

        // System.out.println(clienteRepository.findByEmail("ricardo@gmail.com").getAcoes().toString());
    }

}
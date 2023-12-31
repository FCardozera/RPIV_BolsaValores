package com.unipampa.stocktrade.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.movimentacao.enums.TipoMovimentacao;
import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
import com.unipampa.stocktrade.model.entity.oferta.factoryMethod.OfertaFactory;
import com.unipampa.stocktrade.model.entity.usuario.Admin;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;
import com.unipampa.stocktrade.model.entity.usuario.FactoryMethod.UsuarioFactory;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.VendaAcaoRepository;
import com.unipampa.stocktrade.model.repository.movimentacao.MovimentacaoRepository;
import com.unipampa.stocktrade.model.repository.oferta.CompraOfertaRepository;
import com.unipampa.stocktrade.model.repository.oferta.VendaOfertaRepository;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.model.repository.usuario.EmpresaRepository;

import com.unipampa.stocktrade.model.repository.usuario.AdminRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

@Configuration
@Profile("produce")
public class ProduceConfig implements CommandLineRunner {

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

    @Autowired
    private VendaOfertaRepository vendaOfertaRepository;

    @Autowired
    private CompraOfertaRepository compraOfertaRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private RegistroRepository registroRepository;

    @Override
    public void run(String... args) {

        String corVerde = "\u001B[32m";
        String corVermelha = "\u001B[31m";
        String resetCor = "\u001B[0m";
        
        System.out.println("\n\n\n\n");
        System.out.println("===================================================================================");
        System.out.println(corVermelha + "BUGS PODEM OCORRER CASO INICIE A APLICAÇÃO ANTES DA EXECUÇÃO DESTE SCRIPT." + resetCor);
        System.out.println("===================================================================================");
        System.out.println("\n\n\n\n");
        System.out.println("===================================================================================");
        System.out.println("INICIANDO CONFIGURAÇÃO DE PRODUÇÃO...");
        System.out.println("===================================================================================");

        long tempoDeletarInicio = System.currentTimeMillis();

        System.out.println("Deletando todos os registros do banco de dados...");
        vendaOfertaRepository.deleteAll();
        compraOfertaRepository.deleteAll();
        compraAcaoRepository.deleteAll();
        vendaAcaoRepository.deleteAll();
        acaoRepository.deleteAll();
        movimentacaoRepository.deleteAll();
        clienteRepository.deleteAll();
        empresaRepository.deleteAll();
        registroRepository.deleteAll();
        adminRepository.deleteAll();
        System.out.println(corVerde + "Deletando todos os registros do banco de dados... OK" + resetCor);

        long tempoDeletarFim = System.currentTimeMillis();

        long tempoInserirInicio = System.currentTimeMillis();

        System.out.println("Inserindo registros no banco de dados...");
        Instant instant1 = Instant.now();
        Instant instant2 = (LocalDateTime.of(2022, Month.AUGUST, 02, 12, 0)).toInstant(ZoneOffset.UTC);
        Instant instant3 = (LocalDateTime.of(2022, Month.DECEMBER, 31, 23, 0)).toInstant(ZoneOffset.UTC);
        Instant instant4 = (LocalDateTime.of(2023, Month.JUNE, 15, 23, 0)).toInstant(ZoneOffset.UTC);
        Instant instant5 = (LocalDateTime.of(2023, Month.JULY, 10, 12, 0)).toInstant(ZoneOffset.UTC);
        Instant instant6 = (LocalDateTime.of(2023, Month.AUGUST, 01, 12, 0)).toInstant(ZoneOffset.UTC);

        Cliente cliente1 = (Cliente) UsuarioFactory.novoUsuario(null, "Ricardo", "44444444435", "ricardo@gmail.com", "12345678", "1234", TipoUsuario.CLIENTE);
        Cliente cliente2 = (Cliente) UsuarioFactory.novoUsuario(null, "Felipe", "44424444435", "felipe@gmail.com", "12345678", "1234", TipoUsuario.CLIENTE);
        Cliente cliente3 = (Cliente) UsuarioFactory.novoUsuario(null, "João", "44424444444", "joao@gmail.com", "12345678", "1234", TipoUsuario.CLIENTE);
        Cliente cliente4 = (Cliente) UsuarioFactory.novoUsuario(null, "Tales", "44424444467", "tales@gmail.com", "12345678", "1234", TipoUsuario.CLIENTE);

        Admin admin1 = (Admin) UsuarioFactory.novoUsuario(null, "Gilleanes", "44444444469", "gilleanes@gmail.com", "12345678", "1234", TipoUsuario.ADMIN);
        Admin admin2 = (Admin) UsuarioFactory.novoUsuario(null, "Silvio", "44444444470", "silvio@gmail.com", "12345678", "1234", TipoUsuario.ADMIN);

        Movimentacao mov1 = new Movimentacao(null, 15000.0, instant1, TipoMovimentacao.DEPOSITO, cliente1);
        cliente1.setSaldo(cliente1.getSaldo() + 15000.0);

        Movimentacao mov2 = new Movimentacao(null, 25000.0, instant2, TipoMovimentacao.DEPOSITO, cliente2);
        cliente2.setSaldo(cliente2.getSaldo() + 25000.0);

        Movimentacao mov3 = new Movimentacao(null, 5500.0, instant3, TipoMovimentacao.SAQUE, cliente2);
        cliente2.setSaldo(cliente2.getSaldo() - 5500.0);

        Movimentacao mov4 = new Movimentacao(null, 200.0, instant4, TipoMovimentacao.TRANSFERENCIA, cliente2);
        cliente2.setSaldo(cliente2.getSaldo() - 200.0);

        Movimentacao mov5 = new Movimentacao(null, 850.0, instant5, TipoMovimentacao.DEPOSITO, cliente2);
        cliente2.setSaldo(cliente2.getSaldo() + 850.0);

        Movimentacao mov6 = new Movimentacao(null, 60.55, instant6, TipoMovimentacao.DIVIDENDO, cliente2);
        cliente2.setSaldo(cliente2.getSaldo() + 60.55);
        
        cliente3.setSaldo(20000.0);
        cliente4.setSaldo(1000.0);

        clienteRepository.saveAll(List.of(cliente1, cliente2, cliente3, cliente4));
        adminRepository.saveAll(List.of(admin1, admin2));
        movimentacaoRepository.saveAll(List.of(mov1, mov2, mov3, mov4, mov5, mov6));

        Empresa empresa1 = (Empresa) UsuarioFactory.novoUsuario(null, "Petrobras LTDA", "53048280000174", "petrobras@gmail.com", "12345678", "1234", TipoUsuario.EMPRESA);
        Empresa empresa2 = (Empresa) UsuarioFactory.novoUsuario(null, "Vale LTDA", "50951271000109", "vale@gmail.com", "12345678", "1234", TipoUsuario.EMPRESA);
        Empresa empresa3 = (Empresa) UsuarioFactory.novoUsuario(null, "Itaú Unibanco", "50951271000110", "itau@gmail.com", "12345678", "1234", TipoUsuario.EMPRESA);
        Empresa empresa4 = (Empresa) UsuarioFactory.novoUsuario(null, "Lojas Marisa", "50951271000111", "marisa@gmail.com", "12345678", "1234", TipoUsuario.EMPRESA);
        Empresa empresa5 = (Empresa) UsuarioFactory.novoUsuario(null, "Azul SA", "50951271000112", "azul@gmail.com", "12345678", "1234", TipoUsuario.EMPRESA);

        empresaRepository.saveAll(List.of(empresa1, empresa2, empresa3, empresa4, empresa5));

        List<VendaOferta> ofertasEmpresa1 = new ArrayList<>();
        List<VendaOferta> ofertasEmpresa2 = new ArrayList<>();
        // List<Oferta> ofertasEmpresa3 = new ArrayList<>();
        List<VendaOferta> ofertasEmpresa4 = new ArrayList<>();
        // List<Oferta> ofertasEmpresa5 = new ArrayList<>();

        gerarVendarOfertas(empresa1, ofertasEmpresa1, 1000, 50.01, "PETR4");
        gerarVendarOfertas(empresa2, ofertasEmpresa2, 200, 25.41, "VALE5");
        gerarVendarOfertas(empresa3, 100, 104.52, "ITUB4");
        gerarVendarOfertas(empresa4, ofertasEmpresa4, 125, 84.33, "AMAR3");
        gerarVendarOfertas(empresa5, 75, 32.64, "AZUL4");

        // Cliente 1 comprou 20 Ações da PETR4
        processarCompra(ofertasEmpresa1, cliente1, instant1, 20);

        // Cliente 2 comprou 50 Ações da AMAR3
        processarCompra(ofertasEmpresa4, cliente2, instant1, 50);

        // Cliente 2 comprou 40 Ações da PETR4
        processarCompra(ofertasEmpresa1, cliente2, instant2, 40);

        // Cliente 2 comprou 10 Ações da PETR4
        processarCompra(ofertasEmpresa1, cliente2, instant2, 10);

        // Cliente 1 comprou 20 Ações da VALE5
        processarCompra(ofertasEmpresa2, cliente1, instant2, 20);

        // Cliente 2 comprou 10 Ações da VALE5
        processarCompra(ofertasEmpresa2, cliente2, instant1, 10);

        // Cliente 2 comprou 20 Ações da VALE5
        processarCompra(ofertasEmpresa2, cliente2, instant1, 20);

        System.out.println(corVerde + "Inserindo registros no banco de dados... OK" + resetCor);

        long tempoInserirFim = System.currentTimeMillis();

        System.out.println("===================================================================================");
        System.out.println("TEMPO DE EXECUÇÃO:");
        System.out.println("Deletar registros: " + (tempoDeletarFim - tempoDeletarInicio) + " ms");
        System.out.println("Inserir registros: " + (tempoInserirFim - tempoInserirInicio) + " ms");
        System.out.println("Total: " + ((tempoDeletarFim - tempoDeletarInicio) + (tempoInserirFim - tempoInserirInicio)) + " ms");
        System.out.println("===================================================================================");
        System.out.println(corVerde + "CONFIGURAÇÃO DE PRODUÇÃO FINALIZADA." + resetCor);
        System.out.println("===================================================================================");
        System.out.println("\n\n\n\n");
    }

    public void gerarVendarOfertas(Empresa empresa, List<VendaOferta> ofertasEmpresa, int quantidade, Double valor, String sigla) {
        for (int i = 0; i < quantidade; i++) {
            Acao acao = new Acao(null, sigla, empresa, null, null, null, null);
            VendaOferta oferta = (VendaOferta) OfertaFactory.novaOferta(null, null, valor, Instant.now(), null, empresa, acao, TipoOferta.VENDA);
            
            acao.setVendaOferta(oferta);

            acaoRepository.save(acao);
            VendaOferta ofertaSalva = vendaOfertaRepository.save(oferta);
            ofertasEmpresa.add(ofertaSalva);
        }
    }

    public void gerarVendarOfertas(Empresa empresa, int quantidade, Double valor, String sigla) {
        for (int i = 0; i < quantidade; i++) {
            Acao acao = new Acao(null, sigla, empresa, null, null, null, null);
            VendaOferta oferta = (VendaOferta) OfertaFactory.novaOferta(null, null, valor, Instant.now(), null, empresa, acao, TipoOferta.VENDA);
            
            acao.setVendaOferta(oferta);

            acaoRepository.save(acao);
            vendaOfertaRepository.save(oferta);
        }
    }

    public void processarCompra(List<VendaOferta> ofertasEmpresa, Cliente cliente, Instant instant, int quantidade) {
        int count = 0;

        for (VendaOferta oferta : ofertasEmpresa) {
            if (count < quantidade && oferta.getAcao() != null) {
                if (oferta.getAcao().getCliente() == null) {

                    Acao acao = oferta.getAcao();
                    acao.setCliente(cliente);

                    CompraAcao compraAcao = new CompraAcao(null, acao, cliente, oferta.getValorOferta(), instant);
                    compraAcaoRepository.save(compraAcao);

                    acaoRepository.save(acao);

                    oferta.setAcao(null);
                    vendaOfertaRepository.save(oferta);
                    vendaOfertaRepository.deleteById(oferta.getId());

                    count++;
                }
            }
        }
    }

}
package com.unipampa.stocktrade.model.entity.usuario;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.TextStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.unipampa.stocktrade.controller.dto.cliente.ClienteRequestDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.acao.VendaAcao;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.movimentacao.enums.TipoMovimentacao;
import com.unipampa.stocktrade.model.entity.oferta.Oferta;
import com.unipampa.stocktrade.model.entity.usuario.enums.TipoUsuario;

import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "cliente")
@Table(name = "tb_cliente")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cliente extends Usuario {

    @Column(unique = true)
    private String cpf;

    @Column(name = "saldo")
    private Double saldo;

    @OneToMany(mappedBy = "cliente")
    private Set<Movimentacao> movimentacoes;

    @OneToMany(mappedBy = "cliente", fetch = FetchType.EAGER)
    private Set<Acao> acoes;

    @OneToMany(mappedBy = "cliente")
    private Set<Oferta> ofertas;

    @OneToMany(mappedBy = "cliente")
    private Set<CompraAcao> compraAcoes;

    @OneToMany(mappedBy = "cliente")
    private Set<VendaAcao> vendaAcoes;

    public Cliente(UUID id, String nome, String cpf, String email, String senha, String senhaAutenticacao) {
        super(id, nome, email, senha, senhaAutenticacao, TipoUsuario.CLIENTE);
        this.cpf = cpf;
        this.saldo = 0.0;
    }

    public Cliente(ClienteRequestDTO data) {
        this(null, data.nome(), data.cpf(), data.email(), data.senha(), data.senhaAutenticacao());
    }

    private Movimentacao getMovimentacaoMaisAntiga() {
        if (movimentacoes == null || movimentacoes.isEmpty()) {
            return null;
        }

        Movimentacao movimentacaoMaisAntiga = movimentacoes.iterator().next();

        for (Movimentacao movimentacao : movimentacoes) {
            if (movimentacao.getData().isBefore(movimentacaoMaisAntiga.getData())) {
                movimentacaoMaisAntiga = movimentacao;
            }
        }

        return movimentacaoMaisAntiga;
    }

    public Double variacaoSaldo24h() {
        Instant agora = Instant.now();
        Instant limiteTempo = agora.minusSeconds(24 * 60 * 60); // 24 horas em segundos

        List<Movimentacao> movimentacoes24h = movimentacoes.stream()
                .filter(movimentacao -> movimentacao.getData().isAfter(limiteTempo))
                .collect(Collectors.toList());

        Double somaDepositos24h = movimentacoes24h.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.DEPOSITO)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double somaSaques24h = movimentacoes24h.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.SAQUE)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double somaTransferencias24h = movimentacoes24h.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.TRANSFERENCIA)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double somaDividendos24h = movimentacoes24h.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.DIVIDENDO)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double variacaoSaldo24 = (somaDepositos24h + somaDividendos24h) - (somaSaques24h + somaTransferencias24h);

        return variacaoSaldo24;
    }

    public Double variacaoSaldoMesAno(int mes, int ano) {
        Instant inicioMes = Instant.ofEpochSecond(0)
                .atZone(ZoneOffset.UTC)
                .withYear(ano)
                .withMonth(mes)
                .withDayOfMonth(1)
                .toInstant();

        Instant inicioProximoMes = inicioMes.plus(Period.ofMonths(1));

        List<Movimentacao> movimentacoesMesAno = movimentacoes.stream()
                .filter(movimentacao -> movimentacao.getData().isAfter(inicioMes)
                        || movimentacao.getData().equals(inicioMes)
                                && movimentacao.getData().isBefore(inicioProximoMes))
                .collect(Collectors.toList());

        Double somaDepositosMesAno = movimentacoesMesAno.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.DEPOSITO)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double somaSaquesMesAno = movimentacoesMesAno.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.SAQUE)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double somaTransferenciasMesAno = movimentacoesMesAno.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.TRANSFERENCIA)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double somaDividendosMesAno = movimentacoesMesAno.stream()
                .filter(movimentacao -> movimentacao.getTipo() == TipoMovimentacao.DIVIDENDO)
                .mapToDouble(Movimentacao::getValor)
                .sum();

        Double variacaoSaldoMesAno = (somaDepositosMesAno + somaDividendosMesAno)
                - (somaSaquesMesAno + somaTransferenciasMesAno);

        return variacaoSaldoMesAno;
    }

    public Double saldoFinalMesAno(int mes, int ano) {
        if (mes > 12) {
            mes = mes - 12;
        }

        Instant inicioMes = Instant.ofEpochSecond(0)
                .atZone(ZoneOffset.UTC)
                .withYear(ano)
                .withMonth(mes)
                .withDayOfMonth(1)
                .withHour(12)
                .toInstant();

        LocalDate dataEmLocalDate = inicioMes.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate inicioProximoMes = dataEmLocalDate.with(TemporalAdjusters.firstDayOfNextMonth());
        Instant inicioProximoMesEmInstant = inicioProximoMes.atStartOfDay(ZoneId.systemDefault()).toInstant();

        List<Movimentacao> movimentacoesMesAno = movimentacoes.stream()
                .filter(movimentacao -> movimentacao.getData().isBefore(inicioProximoMesEmInstant))
                .collect(Collectors.toList());

        Double saldoFinal = 0.0;

        for (Movimentacao movimentacao : movimentacoesMesAno) {
            if (movimentacao.getTipo() == TipoMovimentacao.DEPOSITO
                    || movimentacao.getTipo() == TipoMovimentacao.DIVIDENDO) {
                saldoFinal += movimentacao.getValor();
            } else if (movimentacao.getTipo() == TipoMovimentacao.SAQUE
                    || movimentacao.getTipo() == TipoMovimentacao.TRANSFERENCIA) {
                saldoFinal -= movimentacao.getValor();
            }
        }

        return saldoFinal;
    }

    public LinkedHashMap<String, Double> movimentacoesMensais1Ano() {
        LinkedHashMap<String, Double> mapaMovimentacoes = new LinkedHashMap<String, Double>();
        Instant primeiraMovimentacao = getMovimentacaoMaisAntiga().getData();
        Instant agora = Instant.now();
        LocalDate dataPrimeiraMovimentacao = primeiraMovimentacao.atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dataAtual = agora.atZone(ZoneId.systemDefault()).toLocalDate();
        Period periodo = Period.between(dataPrimeiraMovimentacao, dataAtual);
        long diferencaEmAnos = periodo.getYears();
        long diferencaEmMeses = ChronoUnit.MONTHS.between(dataPrimeiraMovimentacao, dataAtual);
        Locale BRAZIL = new Locale.Builder().setLanguage("pt").setRegion("BR").build();

        if (diferencaEmAnos >= 1) {
            LocalDate pontoInicialLocalDate = dataAtual.minusMonths(11);
            Instant pontoInicialInstant = pontoInicialLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant(); // Mês atual menos 11 meses
            int mesInicial = pontoInicialInstant.atZone(ZoneId.systemDefault()).get(ChronoField.MONTH_OF_YEAR);
            int anoInicial = pontoInicialInstant.atZone(ZoneId.systemDefault()).get(ChronoField.YEAR);

            for (int i = mesInicial; i <= (mesInicial + 11); i++) {
                int ano = anoInicial;
                int mes = i;
                if (i > 12) {
                    ano = anoInicial + 1;
                    mes = i - 12;
                }

                Instant mesInstant = Instant.ofEpochSecond(0)
                        .atZone(ZoneOffset.UTC)
                        .withYear(ano)
                        .withMonth(mes)
                        .withDayOfMonth(1)
                        .withHour(12)
                        .toInstant();

                String nomeMes = mesInstant.atZone(ZoneId.systemDefault()).getMonth().getDisplayName(TextStyle.SHORT,
                        BRAZIL);
                nomeMes = nomeMes.substring(0, 1).toUpperCase()
                        + nomeMes.substring(1, nomeMes.length() - 1).toLowerCase();
                Double saldoFinalMes = saldoFinalMesAno(mes, ano);

                mapaMovimentacoes.put(nomeMes, saldoFinalMes);
            }
        } else {
            int mesInicial = primeiraMovimentacao.atZone(ZoneId.systemDefault()).get(ChronoField.MONTH_OF_YEAR);
            int anoInicial = primeiraMovimentacao.atZone(ZoneId.systemDefault()).get(ChronoField.YEAR);

            for (int i = mesInicial; i <= (mesInicial + diferencaEmMeses); i++) {
                int ano = anoInicial;
                int mes = i;
                if (i > 12) {
                    ano = anoInicial + 1;
                    mes = i - 12;
                }

                Instant mesInstant = Instant.ofEpochSecond(0)
                        .atZone(ZoneOffset.UTC)
                        .withYear(ano)
                        .withMonth(mes)
                        .withDayOfMonth(1)
                        .withHour(12)
                        .toInstant();

                String nomeMes = mesInstant.atZone(ZoneId.systemDefault()).getMonth().getDisplayName(TextStyle.SHORT,
                        BRAZIL);
                nomeMes = nomeMes.substring(0, 1).toUpperCase()
                        + nomeMes.substring(1, nomeMes.length() - 1).toLowerCase();
                Double saldoFinalMes = saldoFinalMesAno(mes, ano);

                mapaMovimentacoes.put(nomeMes, saldoFinalMes);
            }
        }
        return mapaMovimentacoes;
    }
}
package com.unipampa.stocktrade.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.acao.CompraAcoesDTO;
import com.unipampa.stocktrade.controller.dto.acao.VendaAcoesDTO;
import com.unipampa.stocktrade.controller.dto.oferta.ExcluirOfertaRequestDTO;
import com.unipampa.stocktrade.model.entity.acao.Acao;
import com.unipampa.stocktrade.model.entity.acao.CompraAcao;
import com.unipampa.stocktrade.model.entity.acao.VendaAcao;
import com.unipampa.stocktrade.model.entity.oferta.CompraOferta;
import com.unipampa.stocktrade.model.entity.oferta.VendaOferta;
import com.unipampa.stocktrade.model.entity.oferta.enums.TipoOferta;
import com.unipampa.stocktrade.model.entity.oferta.factoryMethod.OfertaFactory;
import com.unipampa.stocktrade.model.entity.oferta.iterator.vendaOferta.VendaOfertaIterator;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.entity.usuario.Empresa;
import com.unipampa.stocktrade.model.entity.usuario.Usuario;

import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.CompraAcaoRepository;
import com.unipampa.stocktrade.model.repository.acao.VendaAcaoRepository;
import com.unipampa.stocktrade.model.repository.oferta.CompraOfertaRepository;
import com.unipampa.stocktrade.model.repository.oferta.VendaOfertaRepository;
import com.unipampa.stocktrade.model.repository.registro.RegistroRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;
import com.unipampa.stocktrade.service.exception_handler.ExceptionHandlerChain;
import com.unipampa.stocktrade.service.exception_handler.enums.TipoException;

import jakarta.servlet.http.HttpSession;

@Service
public class CarteiraService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private VendaOfertaRepository vendaOfertaRepository;

    @Autowired
    private CompraOfertaRepository compraOfertaRepository;

    @Autowired 
    private CompraAcaoRepository compraAcaoRepository;

    @Autowired 
    private VendaAcaoRepository vendaAcaoRepository;

    @Autowired
    private RegistroRepository registroRepository;

    private static final String USUARIO_LOGADO = "usuarioLogado";

    public HttpSession updateSession(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Usuario usuario = clienteRepository.findByEmail(usuarioLogado.getEmail());
        session.setAttribute(USUARIO_LOGADO, usuario);
        return session;
    }

    public Double variacaoSaldoUsuario24H(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            return cliente.variacaoSaldo24h();
        } catch (Exception e) {
            return 0.0;
        }
    }

    public List<String> mesesMovimentacoes1AnoUsuario(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            LinkedHashMap<String, Double> movimentacoesMensais = (LinkedHashMap<String, Double>) cliente.movimentacoesMensais1Ano();
            LinkedList<String> listaMeses = new LinkedList<>();
            for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
                listaMeses.add(entry.getKey());
            }
            return listaMeses;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<Double> saldosFinaisMovimentacoes1AnoUsuario(HttpSession session) {
        try {
            Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
            Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
            LinkedHashMap<String, Double> movimentacoesMensais = (LinkedHashMap<String, Double>) cliente.movimentacoesMensais1Ano();
            LinkedList<Double> listaSaldosFinais = new LinkedList<>();
            for (Map.Entry<String, Double> entry : movimentacoesMensais.entrySet()) {
                listaSaldosFinais.add(entry.getValue());
            }
            return listaSaldosFinais;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String[]> getAcoesUser (HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
        List<String[]> acoesString = acaoRepository.findAcoesClientePrecoMedio(cliente.getId());
        List<String[]> acoes = new ArrayList<>();

        for (String[] acaoQueryBanco : acoesString) {
            String[] acaoFinal = new String[7];

            acaoFinal[0] = acaoQueryBanco[1];
            String[] valorAtualEQuantidadeDisponivel = vendaOfertaRepository.findMenorPrecoOfertaVendaEQuantidadeDisponivelBySigla(acaoQueryBanco[1]).split(",");
            acaoFinal[1] = valorAtualEQuantidadeDisponivel[0];
            acaoFinal[2] = acaoQueryBanco[2];
            acaoFinal[3] = acaoQueryBanco[3];

            Double valorAtualDouble = Double.parseDouble(valorAtualEQuantidadeDisponivel[0]);
            Double precoMedio = Double.parseDouble(acaoFinal[3]);

            Double variacao = ((valorAtualDouble*100)/precoMedio)-100;
            String variacaoFormatada = String.format("%.2f", variacao);
            acaoFinal[4] = variacaoFormatada;
            acaoFinal[5] = valorAtualEQuantidadeDisponivel[1];
            acaoFinal[6] = acaoQueryBanco[0];
            acoes.add(acaoFinal);
        }

        return acoes;
    }

    public List<String[]> getCompraOfertasUser(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());

        return compraOfertaRepository.findOfertasCompraByClienteId(cliente.getId());
    }

    public ResponseEntity<String> comprarAcoes(HttpSession session, CompraAcoesDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

        if (usuario == null) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SEM_USUARIO, null, registroRepository));
        }

        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());

        if (!cliente.isSenhaAutenticacaoCorreta(dados.senhaAutenticacao())) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SENHA_INVALIDA, session, registroRepository));
        }

        if (acaoRepository.findAcoesSigla(dados.siglaAcao()) < 0) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SIGLA_INVALIDA, session, registroRepository));
        }

        if (!cliente.possuiSaldoSuficiente(dados.quantidadeAcoes(), dados.precoAcao())) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SALDO_INSUFICIENTE, session, registroRepository));
        }

        List<VendaOferta> ofertasVenda = vendaOfertaRepository.findOfertasVendaBySiglaAndPreco(dados.siglaAcao(), PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
        if (ofertasVenda.isEmpty()) {
            agendarCompraOferta(dados.quantidadeAcoes(), cliente, dados);
            return ResponseEntity.ok("Aguardando ofertas");
        }

        if (quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(ofertasVenda.size(), dados.quantidadeAcoes())) {
            agendarCompraOferta(dados.quantidadeAcoes() - ofertasVenda.size(), cliente, dados);
        }

        Iterator<VendaOferta> ofertaIterator = new VendaOfertaIterator(ofertasVenda.iterator());
        try {
            while (ofertaIterator.hasNext()) {
                VendaOferta vendaOferta = ofertaIterator.next();
                Acao acao = vendaOferta.getAcao();

                CompraAcao compraAcao = cliente.comprarAcao(vendaOferta);    

                compraAcaoRepository.save(compraAcao);
                clienteRepository.save(cliente);

                acaoRepository.save(acao);
                
                vendaOfertaRepository.save(vendaOferta);
                vendaOfertaRepository.deleteById(vendaOferta.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações compradas");
    }

    public ResponseEntity<String> venderAcoes(HttpSession session, VendaAcoesDTO dados) {
        Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);
    
        if (usuario == null) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SEM_USUARIO, null, registroRepository));
        }
    
        Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());
    
        if (!cliente.isSenhaAutenticacaoCorreta(dados.senhaAutenticacao())) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SENHA_INVALIDA, session, registroRepository));
        }
    
        ResponseEntity<String> response = verificarAcoesParaVenda(cliente, dados.quantidadeAcoes(), dados.siglaAcao());
        if (response != null) {
            return response;
        }

        List<CompraOferta> ofertasCompra = compraOfertaRepository.findOfertasCompraBySiglaAndPreco(dados.siglaAcao(), PageRequest.of(0, dados.quantidadeAcoes()), dados.precoAcao());
        List<Acao> acoesCliente = acaoRepository.findAcoesClienteByClienteIdSigla(cliente.getId(), dados.siglaAcao());

        if (acoesCliente.isEmpty()) {
            return ResponseEntity.badRequest().body("Você não pode vender ações que não possui");
        }

        if (acoesCliente.size() < dados.quantidadeAcoes()) {
            return ResponseEntity.badRequest().body("Você não pode vender mais ações do que possui");
        }

        Iterator<Acao> acoesIteratorAgendarVendaOferta = acoesCliente.iterator();
        if (ofertasCompra.isEmpty()) {
            agendarVendaOferta(dados.quantidadeAcoes(), cliente, dados, acoesIteratorAgendarVendaOferta);
            return ResponseEntity.ok("Aguardando ofertas de compra");
        }

        Iterator<Acao> acoesIteratorAgendarParcialmenteVendaOferta = acoesCliente.iterator();
        if (quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(ofertasCompra.size(), dados.quantidadeAcoes())) {
            agendarVendaOferta(dados.quantidadeAcoes() - ofertasCompra.size(), cliente, dados, acoesIteratorAgendarParcialmenteVendaOferta);
        }
        
        Iterator<CompraOferta> ofertaIterator = ofertasCompra.iterator();
        Iterator<Acao> acoesIterator = acoesCliente.iterator();
        try {
            while (ofertaIterator.hasNext()) {
                CompraOferta compraOferta = ofertaIterator.next();

                Acao acao = acoesIterator.next();

                VendaAcao vendaAcao = cliente.venderAcao(compraOferta, acao);

                vendaAcaoRepository.save(vendaAcao);
                clienteRepository.save(cliente);

                acaoRepository.save(acao);
                
                compraOfertaRepository.save(compraOferta);
                compraOfertaRepository.deleteById(compraOferta.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        clienteRepository.save(cliente);
        return ResponseEntity.ok("Ações vendidas");
    }
    
    private ResponseEntity<String> verificarAcoesParaVenda(Cliente cliente, int quantidadeParaVender, String siglaAcao) {
        Integer qntAcoesClienteSigla = acaoRepository.findQntAcoesBySiglaClienteId(siglaAcao, cliente.getId());

        if (qntAcoesClienteSigla < quantidadeParaVender || qntAcoesClienteSigla == null) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.QTD_ACOES_INSUFICIENTE, null, registroRepository));
        }

        return null;
    }

    private boolean quantidadeOfertaMaiorQueAQuantidadeDeAcoesDesejadas(Integer quantidadeOferta, Integer quantidadeAcoes) {
        return quantidadeOferta > quantidadeAcoes;
    }

    private boolean agendarCompraOferta(Integer quantidadeAgendamento, Cliente cliente, CompraAcoesDTO dados) {
        if (quantidadeAgendamento <= 0) {
            return false;
        }

        for (int i = 0; i < quantidadeAgendamento; i++) {
            CompraOferta oferta = (CompraOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), null, null, TipoOferta.COMPRA);
            compraOfertaRepository.save(oferta);
        }

        return true;
    }

    private boolean agendarVendaOferta(Integer quantidadeAgendamento, Cliente cliente, VendaAcoesDTO dados, Iterator<Acao> acoesIterator) {
        if (quantidadeAgendamento <= 0) {
            return false;
        }

        for (int i = 0; i < quantidadeAgendamento; i++) {
            Acao acao = acoesIterator.next();
            Empresa empresa = acao.getEmpresa();
            VendaOferta oferta = (VendaOferta) OfertaFactory.novaOferta(null, cliente, dados.precoAcao(), Instant.now(), dados.siglaAcao(), empresa, acao, TipoOferta.VENDA);
            vendaOfertaRepository.save(oferta);
        }

        return true;
    }

    public List<String[]> getVendaOfertasUser(HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute(USUARIO_LOGADO);
        Cliente cliente = clienteRepository.findByEmail(usuarioLogado.getEmail());
    
        return vendaOfertaRepository.findOfertasVendaByClienteId(cliente.getId());
    }

    public String getLucroTotal(List<String[]> acoesUsuario) {
        if (acoesUsuario.isEmpty()) {
            return "R$0.00 (0%)";
        }

        Double lucroTotal = 0.0D;
        Double totalInvestido = 0.0D;
        Double totalAtual = 0.0D;
        Double porcentagemLucroTotal = 0.0D;

        for (String[] acao : acoesUsuario) {
            Double precoAtual = Double.parseDouble(acao[1]);
            Double precoMedioCompra = Double.parseDouble(acao[3]);
            int qtdAcoes = Integer.parseInt(acao[2]);
            Double totalAtualAcao = (precoAtual * qtdAcoes);
            Double totalInvestidoAcao = (precoMedioCompra * qtdAcoes);
            Double lucroTotalAcao = totalAtualAcao - totalInvestidoAcao;
            lucroTotal += lucroTotalAcao;
            totalInvestido += totalInvestidoAcao;
            totalAtual += totalAtualAcao;
        }
        // Garantir que o total investido não seja 0 par a divisão funcionar
        if (totalInvestido != 0) {
            porcentagemLucroTotal = ((totalAtual * 100)/totalInvestido) - 100;
        }

        String porcentagemFormatada = String.format("%.2f", porcentagemLucroTotal);
        if (lucroTotal > 0) {
            return "R$+" + lucroTotal + " (" + porcentagemFormatada + "%)";
        } else if (lucroTotal < 0) {
            return "R$" + lucroTotal + " (" + porcentagemFormatada + "%)";
        } else {
            return "R$" + lucroTotal + " (" + porcentagemFormatada + "%)";
        }
    }

    public String getMelhorPerformance(List<String[]> acoesUsuario) {
        if (acoesUsuario.isEmpty()) {
            return "Não há";
        }

        String siglaMelhorAtivo = "";
        Double lucroMelhorAtivo = 0.0D;
        String porcentagemLucroMelhorAtivo = "";
        int count = 0;

        for (String[] acao : acoesUsuario) {
            Double precoAtual = Double.parseDouble(acao[1]);
            Double precoMedioCompra = Double.parseDouble(acao[3]);
            int qtdAcoes = Integer.parseInt(acao[2]);
            Double totalAtualAcao = (precoAtual * qtdAcoes);
            Double totalInvestidoAcao = (precoMedioCompra * qtdAcoes);
            Double lucroTotalAcao = totalAtualAcao - totalInvestidoAcao;
            if (count == 0) {
                siglaMelhorAtivo = acao[0];
                lucroMelhorAtivo = lucroTotalAcao;
                porcentagemLucroMelhorAtivo = acao[4];
                count++;
            }
            if (lucroTotalAcao > lucroMelhorAtivo) {
                siglaMelhorAtivo = acao[0];
                lucroMelhorAtivo = lucroTotalAcao;
                porcentagemLucroMelhorAtivo = acao[4];
            }
        }
        
        if (lucroMelhorAtivo > 0) {
            return siglaMelhorAtivo + " | R$+" + lucroMelhorAtivo + " (" + porcentagemLucroMelhorAtivo + "%)";
        } else if (lucroMelhorAtivo < 0) {
            return siglaMelhorAtivo + " | R$" + lucroMelhorAtivo + " (" + porcentagemLucroMelhorAtivo + "%)";
        } else {
            return siglaMelhorAtivo + " | R$" + lucroMelhorAtivo + " (" + porcentagemLucroMelhorAtivo + "%)";
        }
    }

    public String getPiorPerformance(List<String[]> acoesUsuario) {
        if (acoesUsuario.isEmpty()) {
            return "Não há";
        }

        String siglaPiorAtivo = "";
        Double lucroPiorAtivo = 0.0D;
        String porcentagemLucroPiorAtivo = "";
        int count = 0;

        for (String[] acao : acoesUsuario) {
            Double precoAtual = Double.parseDouble(acao[1]);
            Double precoMedioCompra = Double.parseDouble(acao[3]);
            int qtdAcoes = Integer.parseInt(acao[2]);
            Double totalAtualAcao = (precoAtual * qtdAcoes);
            Double totalInvestidoAcao = (precoMedioCompra * qtdAcoes);
            Double lucroTotalAcao = totalAtualAcao - totalInvestidoAcao;
            if (count == 0) {
                siglaPiorAtivo = acao[0];
                lucroPiorAtivo = lucroTotalAcao;
                porcentagemLucroPiorAtivo = acao[4];
                count++;
            }
            if (lucroTotalAcao < lucroPiorAtivo) {
                siglaPiorAtivo = acao[0];
                lucroPiorAtivo = lucroTotalAcao;
                porcentagemLucroPiorAtivo = acao[4];
            }
        }
        
        if (lucroPiorAtivo > 0) {
            return siglaPiorAtivo + " | R$+" + lucroPiorAtivo + " (" + porcentagemLucroPiorAtivo + "%)";
        } else if (lucroPiorAtivo < 0) {
            return siglaPiorAtivo + " | R$" + lucroPiorAtivo + " (" + porcentagemLucroPiorAtivo + "%)";
        } else {
            return siglaPiorAtivo + " | R$" + lucroPiorAtivo + " (" + porcentagemLucroPiorAtivo + "%)";
        }
    }

    public Object excluirOfertaCompra(HttpSession session, ExcluirOfertaRequestDTO dados) {

        try {
            Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

            if (usuario == null) {
                return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SEM_USUARIO, null, registroRepository));
            }

            Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());

            List<CompraOferta> ofertasCompra = compraOfertaRepository.findOfertasCompraBySiglaAndPrecoAndIdUser(dados.sigla(), dados.preco(), cliente.getId());

            if (ofertasCompra.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.OFERTA_NAO_ENCONTRADA, null, registroRepository));
            }

            compraOfertaRepository.deleteAll(ofertasCompra);

            return ResponseEntity.ok("Oferta excluída");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.ERRO_INTERNO, null, registroRepository));
        }
        
    }

    public Object excluirOfertaVenda(HttpSession session, ExcluirOfertaRequestDTO dados) {
        try {
            Usuario usuario = (Usuario) session.getAttribute(USUARIO_LOGADO);

            if (usuario == null) {
                return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.SEM_USUARIO, null, registroRepository));
            }

            Cliente cliente = clienteRepository.findByEmail(usuario.getEmail());

            List<VendaOferta> ofertasVenda = vendaOfertaRepository.findOfertasVendaBySiglaAndPrecoAndIdUser(dados.sigla(), dados.preco(), cliente.getId());

            if (ofertasVenda.isEmpty()) {
                return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.OFERTA_NAO_ENCONTRADA, null, registroRepository));
            }

            for (VendaOferta vendaOferta : ofertasVenda) {
                vendaOferta.setAcao(null);
                vendaOferta.setCliente(null);

                vendaOfertaRepository.save(vendaOferta);

                vendaOfertaRepository.deleteById(vendaOferta.getId());
            }

            return ResponseEntity.ok("Oferta excluída");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ExceptionHandlerChain.handle(TipoException.ERRO_INTERNO, null, registroRepository));
        }
    }
 
}

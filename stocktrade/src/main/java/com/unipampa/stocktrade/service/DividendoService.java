package com.unipampa.stocktrade.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.unipampa.stocktrade.controller.dto.dividendo.DividendoRequestDTO;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import com.unipampa.stocktrade.model.entity.usuario.Cliente;
import com.unipampa.stocktrade.model.repository.acao.AcaoRepository;
import com.unipampa.stocktrade.model.repository.movimentacao.MovimentacaoRepository;
import com.unipampa.stocktrade.model.repository.usuario.ClienteRepository;

@Service
public class DividendoService {

    @Autowired
    private AcaoRepository acaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    public Object cadastrarDividendo(DividendoRequestDTO dividendoRequestDTO) {

        if (acaoRepository.findAcoesSigla(dividendoRequestDTO.sigla()) == null) {
            return ResponseEntity.badRequest().body("Ação não encontrada");
        }

        List<String[]> acoes = acaoRepository.findClienteQuantidadeBySigla(dividendoRequestDTO.sigla());

        if (acoes.isEmpty()) {
            return ResponseEntity.badRequest().body("Nenhum cliente possui essa ação");
        }

        Double valorDividendo = dividendoRequestDTO.valor();

        for (String[] acao : acoes) {
            Double quantidade = Double.parseDouble(acao[1]);

            UUID idCliente = UUID.fromString(acao[0]);
            Cliente cliente = clienteRepository.findById(idCliente).get();

            Movimentacao movimentacao = cliente.adicionarDividendo(quantidade, valorDividendo);
            movimentacaoRepository.save(movimentacao);

            clienteRepository.save(cliente);
        }

        return ResponseEntity.ok().build();
    }
}

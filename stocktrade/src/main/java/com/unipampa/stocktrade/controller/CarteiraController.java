package com.unipampa.stocktrade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.dto.acao.CompraAcoesDTO;
import com.unipampa.stocktrade.service.CarteiraService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/carteira")
public class CarteiraController {

    @Autowired
    private CarteiraService carteiraService;
    
    @GetMapping
    public ModelAndView carteiraPagina(HttpSession session) {
        ModelAndView mv = new ModelAndView("/carteira");
        carteiraService.updateSession(session);
        Double variacaoSaldo24H = carteiraService.variacaoSaldoUsuario24H(session);
        mv.addObject("variacaoSaldo24H", variacaoSaldo24H);
        List<String> mesesMovimentacoes1Ano = carteiraService.mesesMovimentacoes1AnoUsuario(session);
        mv.addObject("mesesMovimentacoes1Ano", mesesMovimentacoes1Ano);
        List<Double> saldosFinaisMovimentacoes1Ano = carteiraService.saldosFinaisMovimentacoes1AnoUsuario(session);
        mv.addObject("saldosFinaisMovimentacoes1Ano", saldosFinaisMovimentacoes1Ano);
        List<String[]> acoesUsuario = carteiraService.getAcoesUser(session);
        mv.addObject("acoesUsuario", acoesUsuario);
        return mv;
    }

    @PostMapping("/comprar")
    public Object comprarAcoes(HttpSession session, @RequestBody CompraAcoesDTO dados) {
        return carteiraService.comprarAcoes(session, dados);
    }

}
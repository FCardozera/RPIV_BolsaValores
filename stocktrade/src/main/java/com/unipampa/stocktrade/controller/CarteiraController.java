package com.unipampa.stocktrade.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.dto.acao.CompraAcoesDTO;
import com.unipampa.stocktrade.controller.dto.acao.VendaAcoesDTO;
import com.unipampa.stocktrade.controller.dto.oferta.ExcluirOfertaRequestDTO;
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
        String lucroTotal = carteiraService.getLucroTotal(acoesUsuario);
        mv.addObject("lucroTotal", lucroTotal);
        String melhorPerformance = carteiraService.getMelhorPerformance(acoesUsuario);
        mv.addObject("melhorPerformance", melhorPerformance);
        String piorPerformance = carteiraService.getPiorPerformance(acoesUsuario);
        mv.addObject("piorPerformance", piorPerformance);
        List<String[]> ofertasCompraUsuario = carteiraService.getCompraOfertasUser(session);
        mv.addObject("ofertasCompra", ofertasCompraUsuario);
        List<String[]> ofertasVendaUsuario = carteiraService.getVendaOfertasUser(session);
        mv.addObject("ofertasVenda", ofertasVendaUsuario);
        return mv;
    }

    @PostMapping("/comprar")
    public Object comprarAcoes(HttpSession session, @RequestBody CompraAcoesDTO dados) {
        return carteiraService.comprarAcoes(session, dados);
    }

    @PostMapping("/vender")
    public Object venderAcoes(HttpSession session, @RequestBody VendaAcoesDTO dados){
        return carteiraService.venderAcoes(session, dados);
    }

    @DeleteMapping("/excluir-oferta-compra")
    public Object excluirOfertaCompra(HttpSession session, @RequestBody ExcluirOfertaRequestDTO dados){
        return carteiraService.excluirOfertaCompra(session, dados);
    }

    @DeleteMapping("/excluir-oferta-venda")
    public Object excluirOfertaVenda(HttpSession session, @RequestBody ExcluirOfertaRequestDTO dados){
        return carteiraService.excluirOfertaVenda(session, dados);
    }

}
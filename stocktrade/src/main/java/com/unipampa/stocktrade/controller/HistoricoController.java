package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.service.HistoricoService;
import com.unipampa.stocktrade.model.entity.movimentacao.Movimentacao;
import java.util.Set;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/historico")
public class HistoricoController {

    @Autowired
    private HistoricoService historicoService;

    @GetMapping
    public ModelAndView historicoPagina(HttpSession session) {
        Set<Movimentacao> movimentacoes = historicoService.getMovimentacoes(session);

        ModelAndView mv = new ModelAndView("/historico");
        mv.addObject("movimentacoes", movimentacoes);

        return mv;
    }
    
}

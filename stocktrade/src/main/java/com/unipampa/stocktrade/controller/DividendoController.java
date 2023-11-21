package com.unipampa.stocktrade.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.unipampa.stocktrade.controller.dto.dividendo.DividendoRequestDTO;
import com.unipampa.stocktrade.service.DividendoService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dividendo")
public class DividendoController {

    @Autowired
    private DividendoService serviceDividendo;
    
    @GetMapping
    public ModelAndView dividendoPagina(HttpSession session) {
        return new ModelAndView("/dividendo");
    }

    @PostMapping
    public Object cadastrarDividendo(HttpSession session, @RequestBody DividendoRequestDTO dividendoRequestDTO) {
        return serviceDividendo.cadastrarDividendo(dividendoRequestDTO);
    }
}

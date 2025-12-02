package com.sgo.controller;

import com.sgo.dto.DashboardDTO;
import com.sgo.repository.RegistroTrabalhoRepository;
import com.sgo.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Controller
public class HomeController {

    @Autowired private DashboardService dashboardService;
    @Autowired private RegistroTrabalhoRepository regRepo; // Apenas para a lista de "Diário"

    @GetMapping({"/", "/home", "/dashboard"})
    public String dashboard(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
            Model model) {

        // Padrão: Início do ano até hoje
        if (inicio == null) inicio = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        if (fim == null) fim = LocalDate.now();

        DashboardDTO dados = dashboardService.gerarDashboard(inicio, fim);
        model.addAttribute("dash", dados);

        // Lista operacional (Diário) que fica abaixo dos gráficos
        model.addAttribute("ultimosRegistros", regRepo.findTop20ByOrderByDataDesc());

        return "home";
    }
}
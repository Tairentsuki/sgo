package com.sgo.controller;

import com.sgo.model.RegistroTrabalho;
import com.sgo.repository.RegistroTrabalhoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RegistroTrabalhoRepository repo;

    @GetMapping
    public String paginaRelatorios(Model model) {
        model.addAttribute("custoObras", repo.relatorioCustoPorObra());
        model.addAttribute("pendencias", repo.relatorioPendencias());
        return "relatorios";
    }

    @GetMapping("/obras/csv")
    public void baixarCsvObras(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=detalhe_obras.csv");

        // Chama o método que agora existe no Repo
        List<RegistroTrabalho> lista = repo.findAllDetalhadoPorObra();

        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF"); // BOM para acentos no Excel
        csv.append("Obra;Data;Funcionário;Observação;Horas;Valor/Hora;Total (R$)\n");

        BigDecimal somaHoras = BigDecimal.ZERO;
        BigDecimal somaValor = BigDecimal.ZERO;

        for (RegistroTrabalho reg : lista) {
            csv.append(reg.getObra().getNome()).append(";")
                    .append(reg.getData()).append(";")
                    .append(reg.getFuncionario().getNome()).append(";")
                    .append(reg.getObservacao() != null ? reg.getObservacao() : "").append(";")
                    .append(reg.getHoras().toString().replace(".", ",")).append(";")
                    .append(reg.getValorHoraSnapshot().toString().replace(".", ",")).append(";")
                    .append(reg.getTotalCalculado().toString().replace(".", ",")).append("\n");

            somaHoras = somaHoras.add(reg.getHoras());
            somaValor = somaValor.add(reg.getTotalCalculado());
        }

        // Adiciona linha final de Totais
        csv.append(";;;;TOTAL GERAL;")
                .append(somaHoras.toString().replace(".", ",")).append(";")
                .append(somaValor.toString().replace(".", ",")).append("\n");

        response.getWriter().write(csv.toString());
    }

    @GetMapping("/pendencias/csv")
    public void baixarCsvPendencias(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=detalhe_a_pagar.csv");

        // Chama o método que agora existe no Repo
        List<RegistroTrabalho> lista = repo.findPendentesDetalhado();

        StringBuilder csv = new StringBuilder();
        csv.append("\uFEFF");
        csv.append("Funcionário;Data;Obra;Serviço;Horas;Valor/Hora;A Receber (R$)\n");

        BigDecimal somaHoras = BigDecimal.ZERO;
        BigDecimal somaValor = BigDecimal.ZERO;

        for (RegistroTrabalho reg : lista) {
            csv.append(reg.getFuncionario().getNome()).append(";")
                    .append(reg.getData()).append(";")
                    .append(reg.getObra().getNome()).append(";")
                    .append(reg.getObservacao() != null ? reg.getObservacao() : "").append(";")
                    .append(reg.getHoras().toString().replace(".", ",")).append(";")
                    .append(reg.getValorHoraSnapshot().toString().replace(".", ",")).append(";")
                    .append(reg.getTotalCalculado().toString().replace(".", ",")).append("\n");

            somaHoras = somaHoras.add(reg.getHoras());
            somaValor = somaValor.add(reg.getTotalCalculado());
        }

        // Adiciona linha final de Totais
        csv.append(";;;;TOTAL A PAGAR;")
                .append(somaHoras.toString().replace(".", ",")).append(";")
                .append(somaValor.toString().replace(".", ",")).append("\n");

        response.getWriter().write(csv.toString());
    }
}
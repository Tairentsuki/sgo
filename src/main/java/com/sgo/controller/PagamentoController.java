package com.sgo.controller;

import com.sgo.dto.HistoricoPagamentoDTO;
import com.sgo.dto.ResumoPendenciaDTO;
import com.sgo.model.Funcionario;
import com.sgo.model.RegistroTrabalho;
import com.sgo.repository.PagamentoRepository;
import com.sgo.repository.RegistroTrabalhoRepository;
import com.sgo.service.FuncionarioService;
import com.sgo.service.PagamentoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;
    private final RegistroTrabalhoRepository registroRepo;
    private final FuncionarioService funcionarioService;
    private final PagamentoRepository pagamentoRepo;

    public PagamentoController(PagamentoService pagamentoService,
                               RegistroTrabalhoRepository registroRepo,
                               FuncionarioService funcionarioService,
                               PagamentoRepository pagamentoRepo) {
        this.pagamentoService = pagamentoService;
        this.registroRepo = registroRepo;
        this.funcionarioService = funcionarioService;
        this.pagamentoRepo = pagamentoRepo;
    }

    @GetMapping
    public String centralPagamentos(
            @RequestParam(required = false) String buscaHist,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicioHist,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fimHist,
            Model model) {

        List<ResumoPendenciaDTO> pendencias = registroRepo.buscarResumoPendencias();
        model.addAttribute("pendencias", pendencias);

        List<HistoricoPagamentoDTO> historico =
                pagamentoRepo.buscarHistoricoComFiltros(buscaHist, inicioHist, fimHist);
        model.addAttribute("historico", historico);

        model.addAttribute("buscaHist", buscaHist);
        model.addAttribute("inicioHist", inicioHist);
        model.addAttribute("fimHist", fimHist);

        boolean filtrarHistorico = buscaHist != null || inicioHist != null || fimHist != null;
        model.addAttribute("abaAtiva", filtrarHistorico ? "historico" : "pendentes");

        return "pagamento-hub";
    }

    @GetMapping("/novo/{funcionarioId}")
    public String prepararPagamento(@PathVariable Long funcionarioId, Model model) {
        Funcionario funcionario = funcionarioService.buscarPorId(funcionarioId);
        List<RegistroTrabalho> registros =
                registroRepo.findByFuncionarioIdAndPagamentoIsNullOrderByDataDesc(funcionarioId);

        model.addAttribute("funcionario", funcionario);
        model.addAttribute("registros", registros);
        return "pagamento-checkout";
    }

    @PostMapping("/efetuar")
    public String efetuarPagamento(@RequestParam(required = false) List<Long> ids,
                                   RedirectAttributes attributes) {
        if (ids == null || ids.isEmpty()) {
            attributes.addFlashAttribute("erro", "Nenhum item selecionado.");
            return "redirect:/pagamentos";
        }

        try {
            pagamentoService.efetuarPagamento(ids);
            attributes.addFlashAttribute("mensagem", "Pagamento realizado com sucesso!");
        } catch (Exception e) {
            attributes.addFlashAttribute("erro", "Erro: " + e.getMessage());
        }

        return "redirect:/pagamentos";
    }
}

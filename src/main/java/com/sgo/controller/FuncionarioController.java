package com.sgo.controller;

import com.sgo.model.Funcionario;
import com.sgo.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/funcionarios")
public class FuncionarioController {

    @Autowired private FuncionarioService service;

    @GetMapping
    public String listar(@RequestParam(required = false) String busca, Model model) {
        List<Funcionario> lista = (busca != null && !busca.isEmpty())
                ? service.buscarPorNome(busca)
                : service.listarTodos();
        model.addAttribute("funcionarios", lista);
        model.addAttribute("buscaAtual", busca);
        return "funcionario-list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("funcionario", new Funcionario());
        return "funcionario-form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) return "funcionario-form";

        service.salvar(funcionario);
        attributes.addFlashAttribute("mensagem", "Funcionário salvo com sucesso!");
        return "redirect:/funcionarios";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("funcionario", service.buscarPorId(id));
        return "funcionario-form";
    }

    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        model.addAttribute("funcionario", service.buscarPorId(id));
        model.addAttribute("historico", service.listarHistorico(id));
        return "funcionario-detalhes";
    }

    @GetMapping("/status/{id}")
    public String trocarStatus(@PathVariable Long id, RedirectAttributes attributes) {
        service.trocarStatus(id);
        attributes.addFlashAttribute("mensagem", "Status do funcionário alterado com sucesso!");
        return "redirect:/funcionarios";
    }
}
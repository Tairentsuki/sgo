package com.sgo.controller;

import com.sgo.model.Cliente;
import com.sgo.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    @GetMapping
    public String listar(@RequestParam(required = false) String busca,
                         @RequestParam(required = false, defaultValue = "ativos") String filtroStatus,
                         Model model) {

        List<Cliente> lista;
        boolean buscarAtivos = true;

        if ("todos".equals(filtroStatus)) {
            if (busca != null && !busca.isEmpty()) {
                lista = service.buscarPorNome(busca);
            } else {
                lista = service.listarTodos();
            }
        } else {
            buscarAtivos = !"inativos".equals(filtroStatus);

            if (busca != null && !busca.isEmpty()) {
                lista = service.buscarPorNomeEStatus(busca, buscarAtivos);
            } else {
                lista = service.buscarPorStatus(buscarAtivos);
            }
        }

        model.addAttribute("clientes", lista);
        model.addAttribute("buscaAtual", busca);
        model.addAttribute("statusAtual", filtroStatus);

        return "cliente-list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", service.buscarPorId(id));
        return "cliente-form";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) return "cliente-form";
        service.salvar(cliente);
        attributes.addFlashAttribute("mensagem", "Cliente salvo com sucesso!");
        return "redirect:/clientes";
    }

    @GetMapping("/excluir/{id}")
    public String inativar(@PathVariable Long id, RedirectAttributes attributes) {
        service.inativar(id);
        attributes.addFlashAttribute("mensagem", "Cliente inativado com sucesso!");
        return "redirect:/clientes";
    }
}
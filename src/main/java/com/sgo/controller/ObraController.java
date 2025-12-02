package com.sgo.controller;

import com.sgo.model.Obra;
import com.sgo.model.StatusObra;
import com.sgo.repository.ClienteRepository;
import com.sgo.service.ObraService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/obras")
public class ObraController {

    private final ObraService service;
    private final ClienteRepository clienteRepository;

    public ObraController(ObraService service, ClienteRepository clienteRepository) {
        this.service = service;
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String busca,
                         @RequestParam(required = false) StatusObra status,
                         Model model) {

        var obras = service.listarComFiltro(busca, status);

        model.addAttribute("obras", obras);
        model.addAttribute("buscaAtual", busca);
        model.addAttribute("statusAtual", status);

        carregarCombos(model);
        return "obra-list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("obra", new Obra());
        carregarCombos(model);
        return "obra-form";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("obra", service.buscarPorId(id));
        carregarCombos(model);
        return "obra-form";
    }

    @PostMapping("/salvar")
    public String salvar(Obra obra, RedirectAttributes attributes) {
        service.salvar(obra);
        attributes.addFlashAttribute("mensagem", "Obra salva com sucesso!");
        return "redirect:/obras";
    }

    private void carregarCombos(Model model) {
        model.addAttribute("clientes", clienteRepository.findAllByOrderByIdDesc());
        model.addAttribute("listaStatus", StatusObra.values());
    }
}

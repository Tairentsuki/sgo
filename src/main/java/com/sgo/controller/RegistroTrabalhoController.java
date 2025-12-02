package com.sgo.controller;

import com.sgo.model.RegistroTrabalho;
import com.sgo.repository.FuncionarioRepository;
import com.sgo.repository.ObraRepository;
import com.sgo.service.RegistroTrabalhoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/registros")
public class RegistroTrabalhoController {

    private final RegistroTrabalhoService service;
    private final FuncionarioRepository funcionarioRepository;
    private final ObraRepository obraRepository;

    public RegistroTrabalhoController(RegistroTrabalhoService service,
                                      FuncionarioRepository funcionarioRepository,
                                      ObraRepository obraRepository) {
        this.service = service;
        this.funcionarioRepository = funcionarioRepository;
        this.obraRepository = obraRepository;
    }

    @GetMapping
    public String listar(@RequestParam(required = false) Long funcionarioId, Model model) {
        var registros = (funcionarioId != null)
                ? service.buscarPorFuncionario(funcionarioId)
                : service.listarTodosOrdenado();

        model.addAttribute("registros", registros);
        model.addAttribute("filtroFuncionarioId", funcionarioId);
        model.addAttribute("funcionarios", funcionarioRepository.findByAtivoTrue());

        return "registro-list";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("registro", new RegistroTrabalho());
        carregarCombos(model);
        return "registro-form";
    }

    @PostMapping("/salvar")
    public String salvar(RegistroTrabalho registro, RedirectAttributes attributes) {
        service.lancarHoras(registro);
        attributes.addFlashAttribute("mensagem", "Apontamento registrado com sucesso!");
        return "redirect:/";
    }

    private void carregarCombos(Model model) {
        model.addAttribute("funcionarios", funcionarioRepository.findByAtivoTrue());
        model.addAttribute("obras", obraRepository.findAllByOrderByIdDesc());
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        RegistroTrabalho registro = service.buscarPorId(id);

        // Regra de Segurança: Não permitir editar se já estiver pago
        if (registro.isPago()) {
            throw new RuntimeException("Não é possível editar um registro já pago.");
        }

        model.addAttribute("registro", registro);
        carregarCombos(model); // Carrega a lista de funcionários/obras
        return "registro-form";
    }
}

package com.sgo.service;

import com.sgo.model.Funcionario;
import com.sgo.model.RegistroTrabalho;
import com.sgo.repository.RegistroTrabalhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
public class RegistroTrabalhoService {

    @Autowired private RegistroTrabalhoRepository registroRepo;
    @Autowired private FuncionarioService funcionarioService;

    @Transactional
    public RegistroTrabalho lancarHoras(RegistroTrabalho registro) {
        Funcionario func = funcionarioService.buscarPorId(registro.getFuncionario().getId());

        registro.setValorHoraSnapshot(func.getValorHoraAtual());
        registro.setTotalCalculado(registro.getHoras().multiply(registro.getValorHoraSnapshot()));

        return registroRepo.save(registro);
    }

    public List<RegistroTrabalho> listarTodosOrdenado() {
        return registroRepo.findAllByOrderByDataDesc();
    }

    public List<RegistroTrabalho> buscarPorFuncionario(Long funcionarioId) {
        return registroRepo.findByFuncionarioIdOrderByDataDesc(funcionarioId);
    }

    public RegistroTrabalho buscarPorId(Long id) {
        return registroRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Registro não encontrado"));
    }
}
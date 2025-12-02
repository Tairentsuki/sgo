package com.sgo.service;

import com.sgo.model.Funcionario;
import com.sgo.model.HistoricoValorHora;
import com.sgo.repository.FuncionarioRepository;
import com.sgo.repository.HistoricoValorHoraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FuncionarioService {

    @Autowired private FuncionarioRepository funcionarioRepo;
    @Autowired private HistoricoValorHoraRepository historicoRepo;

    public List<Funcionario> listarTodos() {
        return funcionarioRepo.findAllByOrderByIdDesc();
    }

    public List<Funcionario> buscarPorNome(String nome) {
        return funcionarioRepo.findByNomeContainingIgnoreCaseOrderByIdDesc(nome);
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioRepo.findById(id).orElseThrow(() -> new RuntimeException("Funcionário não encontrado"));
    }

    @Transactional
    public Funcionario salvar(Funcionario novo) {
        if (novo.getId() != null) {
            Funcionario antigo = buscarPorId(novo.getId());
            // Regra: Se salário mudou, gera histórico para auditoria
            if (novo.getValorHoraAtual().compareTo(antigo.getValorHoraAtual()) != 0) {
                criarHistorico(antigo, novo);
            }
        }
        return funcionarioRepo.save(novo);
    }

    private void criarHistorico(Funcionario antigo, Funcionario novo) {
        HistoricoValorHora historico = new HistoricoValorHora();
        historico.setFuncionario(antigo);
        historico.setDataAlteracao(LocalDateTime.now());
        historico.setValorAntigo(antigo.getValorHoraAtual());
        historico.setValorNovo(novo.getValorHoraAtual());
        historico.setObservacao("Alteração via sistema");
        historicoRepo.save(historico);
    }

    public List<HistoricoValorHora> listarHistorico(Long funcionarioId) {
        return historicoRepo.findByFuncionarioIdOrderByDataAlteracaoDesc(funcionarioId);
    }

    @Transactional
    public void trocarStatus(Long id) {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(!funcionario.isAtivo());
        funcionarioRepo.save(funcionario);
    }
}
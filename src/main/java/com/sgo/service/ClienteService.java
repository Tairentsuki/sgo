package com.sgo.service;

import com.sgo.model.Cliente;
import com.sgo.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> listarTodos() {
        return repository.findAllByOrderByIdDesc();
    }

    // --- MÉTODOS DE FILTRO (Consertam o erro de compilação) ---

    public List<Cliente> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCaseOrderByIdDesc(nome);
    }

    public List<Cliente> buscarPorStatus(boolean ativo) {
        return repository.findByAtivoOrderByIdDesc(ativo);
    }

    public List<Cliente> buscarPorNomeEStatus(String nome, boolean ativo) {
        return repository.findByNomeContainingIgnoreCaseAndAtivoOrderByIdDesc(nome, ativo);
    }
    // -----------------------------------------------------------

    @Transactional
    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    @Transactional
    public void inativar(Long id) {
        Cliente cliente = buscarPorId(id);
        cliente.setAtivo(false);
        repository.save(cliente);
    }
}
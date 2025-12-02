package com.sgo.repository;

import com.sgo.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Lista tudo ordenado (Recentes primeiro)
    List<Cliente> findAllByOrderByIdDesc();

    // Busca por Nome
    List<Cliente> findByNomeContainingIgnoreCaseOrderByIdDesc(String nome);

    // Busca por Status
    List<Cliente> findByAtivoOrderByIdDesc(boolean ativo);

    // Busca Combinada (Nome + Status)
    List<Cliente> findByNomeContainingIgnoreCaseAndAtivoOrderByIdDesc(String nome, boolean ativo);
}
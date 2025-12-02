package com.sgo.repository;

import com.sgo.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {
    List<Funcionario> findByAtivoTrue(); // Para comboboxes
    List<Funcionario> findByNomeContainingIgnoreCaseOrderByIdDesc(String nome); // Busca
    List<Funcionario> findAllByOrderByIdDesc(); // Listagem padrão
}
package com.sgo.repository;

import com.sgo.model.HistoricoValorHora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HistoricoValorHoraRepository extends JpaRepository<HistoricoValorHora, Long> {
    List<HistoricoValorHora> findByFuncionarioIdOrderByDataAlteracaoDesc(Long funcionarioId);
}
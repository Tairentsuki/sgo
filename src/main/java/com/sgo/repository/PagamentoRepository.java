package com.sgo.repository;

import com.sgo.dto.HistoricoPagamentoDTO;
import com.sgo.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("SELECT DISTINCT new com.sgo.dto.HistoricoPagamentoDTO(" +
            "p.id, p.dataPagamento, r.funcionario.nome, p.valorTotalPago, COUNT(r)) " +
            "FROM Pagamento p JOIN RegistroTrabalho r ON r.pagamento.id = p.id " +
            "WHERE (:nome IS NULL OR LOWER(r.funcionario.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:inicio IS NULL OR p.dataPagamento >= :inicio) " +
            "AND (:fim IS NULL OR p.dataPagamento <= :fim) " +
            "GROUP BY p.id, p.dataPagamento, r.funcionario.nome, p.valorTotalPago " +
            "ORDER BY p.dataPagamento DESC, p.id DESC")
    List<HistoricoPagamentoDTO> buscarHistoricoComFiltros(String nome, LocalDate inicio, LocalDate fim);
}
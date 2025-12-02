package com.sgo.repository;

import com.sgo.dto.GraficoDTO;
import com.sgo.dto.RelatorioObraDTO;
import com.sgo.dto.RelatorioPendenciaDTO;
import com.sgo.dto.ResumoPendenciaDTO;
import com.sgo.model.RegistroTrabalho;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface RegistroTrabalhoRepository extends JpaRepository<RegistroTrabalho, Long> {

    // --- Operacional ---
    List<RegistroTrabalho> findByPagamentoIsNull();

    // Filtros da tela de apontamentos
    List<RegistroTrabalho> findByFuncionarioIdOrderByDataDesc(Long funcionarioId);
    List<RegistroTrabalho> findAllByOrderByDataDesc();
    List<RegistroTrabalho> findByObraId(Long obraId);

    // Diário do Dashboard (AQUI ESTAVA O ERRO: Mudamos de Top5 para Top20)
    List<RegistroTrabalho> findTop20ByOrderByDataDesc();

    // --- Financeiro (Pagamentos) ---
    // Usado no Hub de Pagamentos para mostrar quem tem saldo a receber
    @Query("SELECT new com.sgo.dto.ResumoPendenciaDTO(r.funcionario.id, r.funcionario.nome, COUNT(r), SUM(r.totalCalculado)) " +
            "FROM RegistroTrabalho r WHERE r.pagamento IS NULL " +
            "GROUP BY r.funcionario.id, r.funcionario.nome ORDER BY SUM(r.totalCalculado) DESC")
    List<ResumoPendenciaDTO> buscarResumoPendencias();

    // Usado no Checkout de Pagamento
    List<RegistroTrabalho> findByFuncionarioIdAndPagamentoIsNullOrderByDataDesc(Long funcionarioId);

    // --- Dashboard & Gráficos ---
    List<RegistroTrabalho> findByDataBetween(LocalDate inicio, LocalDate fim);

    @Query("SELECT DISTINCT r.obra.id FROM RegistroTrabalho r WHERE r.data BETWEEN :inicio AND :fim")
    List<Long> findObrasIdsComApontamento(LocalDate inicio, LocalDate fim);

    @Query("SELECT SUM(r.totalCalculado) FROM RegistroTrabalho r")
    BigDecimal calcularTotalGastoGeral();

    @Query("SELECT new com.sgo.dto.GraficoDTO(r.obra.nome, CAST(SUM(r.totalCalculado) AS long)) " +
            "FROM RegistroTrabalho r GROUP BY r.obra.nome ORDER BY SUM(r.totalCalculado) DESC")
    List<GraficoDTO> custoPorObraGrafico();

    @Query("SELECT new com.sgo.dto.GraficoDTO(f.profissao, CAST(SUM(r.totalCalculado) AS long)) " +
            "FROM RegistroTrabalho r JOIN r.funcionario f WHERE r.data BETWEEN :inicio AND :fim " +
            "GROUP BY f.profissao ORDER BY SUM(r.totalCalculado) DESC")
    List<GraficoDTO> findCustoPorProfissao(LocalDate inicio, LocalDate fim);

    @Query("SELECT new com.sgo.dto.RelatorioPendenciaDTO(r.funcionario.nome, SUM(r.horas), SUM(r.totalCalculado)) " +
            "FROM RegistroTrabalho r WHERE r.data BETWEEN :inicio AND :fim " +
            "GROUP BY r.funcionario.nome ORDER BY SUM(r.totalCalculado) DESC")
    List<RelatorioPendenciaDTO> desempenhoPorFuncionario(LocalDate inicio, LocalDate fim);

    // --- Relatórios CSV ---
    @Query("SELECT new com.sgo.dto.RelatorioObraDTO(r.obra.nome, SUM(r.horas), SUM(r.totalCalculado)) " +
            "FROM RegistroTrabalho r GROUP BY r.obra.nome")
    List<RelatorioObraDTO> relatorioCustoPorObra();

    @Query("SELECT new com.sgo.dto.RelatorioPendenciaDTO(r.funcionario.nome, SUM(r.horas), SUM(r.totalCalculado)) " +
            "FROM RegistroTrabalho r WHERE r.pagamento IS NULL GROUP BY r.funcionario.nome")
    List<RelatorioPendenciaDTO> relatorioPendencias();

    // Métodos para exportação detalhada (linha a linha)
    @Query("SELECT r FROM RegistroTrabalho r ORDER BY r.obra.nome, r.funcionario.nome, r.data")
    List<RegistroTrabalho> findAllDetalhadoPorObra();

    @Query("SELECT r FROM RegistroTrabalho r WHERE r.pagamento IS NULL ORDER BY r.funcionario.nome, r.data")
    List<RegistroTrabalho> findPendentesDetalhado();
}
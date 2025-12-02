package com.sgo.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class DashboardDTO {
    // Filtros
    private String dataInicio;
    private String dataFim;
    private boolean temDados;

    // KPIs Gerais
    private BigDecimal totalGastoPeriodo;
    private BigDecimal totalHorasPeriodo;
    private long totalFuncionariosAtivos;
    private long totalObrasAtivas;

    // GRÁFICOS
    private Map<String, BigDecimal> evolucaoSemanal = new HashMap<>();
    private Map<String, BigDecimal> topObrasCusto = new HashMap<>();
    private Map<String, BigDecimal> statusObras = new HashMap<>();
    private Map<String, BigDecimal> custoPorProfissao = new HashMap<>();
    private Map<String, BigDecimal> topFuncionariosCusto = new HashMap<>(); // Ranking Equipe

    // LISTAS ESPECIAIS
    private List<ScatterData> dispersaoEquipe = new ArrayList<>();
    private List<String> obrasSemApontamento = new ArrayList<>();

    // PAGAMENTOS (Cards da aba Equipe)
    private List<PendenciaCard> cardsPagamento = new ArrayList<>();

    @Data
    @AllArgsConstructor
    public static class ScatterData {
        private String nome;
        private BigDecimal horas;
        private BigDecimal custo;
    }

    @Data
    @AllArgsConstructor
    public static class PendenciaCard {
        private Long funcionarioId;
        private String nome;
        private String cargo;
        private BigDecimal valor;
        private Long qtdRegistros;
    }
}
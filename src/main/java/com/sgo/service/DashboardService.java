package com.sgo.service;

import com.sgo.dto.DashboardDTO;
import com.sgo.dto.GraficoDTO;
import com.sgo.dto.RelatorioPendenciaDTO;
import com.sgo.model.Obra;
import com.sgo.model.RegistroTrabalho;
import com.sgo.model.StatusObra;
import com.sgo.repository.FuncionarioRepository;
import com.sgo.repository.ObraRepository;
import com.sgo.repository.RegistroTrabalhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired private RegistroTrabalhoRepository registroRepo;
    @Autowired private ObraRepository obraRepo;
    @Autowired private FuncionarioRepository funcRepo;

    public DashboardDTO gerarDashboard(LocalDate inicio, LocalDate fim) {
        DashboardDTO dto = new DashboardDTO();
        dto.setDataInicio(inicio.toString());
        dto.setDataFim(fim.toString());

        List<RegistroTrabalho> registros = registroRepo.findByDataBetween(inicio, fim);

        if (registros.isEmpty()) {
            dto.setTemDados(false);
            // Inicializa KPIs com zero para evitar erro visual
            dto.setTotalGastoPeriodo(BigDecimal.ZERO);
            dto.setTotalHorasPeriodo(BigDecimal.ZERO);
            dto.setTotalFuncionariosAtivos(funcRepo.count());
            dto.setTotalObrasAtivas(obraRepo.countByStatus(StatusObra.ANDAMENTO));
            return dto;
        }
        dto.setTemDados(true);

        // 1. KPIs
        dto.setTotalGastoPeriodo(registros.stream().map(RegistroTrabalho::getTotalCalculado).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setTotalHorasPeriodo(registros.stream().map(RegistroTrabalho::getHoras).reduce(BigDecimal.ZERO, BigDecimal::add));
        dto.setTotalFuncionariosAtivos(funcRepo.count());
        dto.setTotalObrasAtivas(obraRepo.countByStatus(StatusObra.ANDAMENTO));

        // 2. Gráficos
        // Semanal
        Map<String, BigDecimal> evoSemanal = new LinkedHashMap<>();
        registros.stream().sorted(Comparator.comparing(RegistroTrabalho::getData))
                .forEach(r -> evoSemanal.merge("Sem " + r.getData().get(ChronoField.ALIGNED_WEEK_OF_YEAR), r.getTotalCalculado(), BigDecimal::add));
        dto.setEvolucaoSemanal(evoSemanal);

        // Ranking Obras
        dto.setTopObrasCusto(getRanking(registros, true));

        // Custo Profissão
        List<GraficoDTO> listaProfissao = registroRepo.findCustoPorProfissao(inicio, fim);
        Map<String, BigDecimal> mapProf = new LinkedHashMap<>();
        listaProfissao.forEach(g -> mapProf.put(g.getLabel() == null ? "N/A" : g.getLabel(), BigDecimal.valueOf(g.getValue())));
        dto.setCustoPorProfissao(mapProf);

        // Dispersão
        List<RelatorioPendenciaDTO> perf = registroRepo.desempenhoPorFuncionario(inicio, fim);
        List<DashboardDTO.ScatterData> dispersao = new ArrayList<>();
        perf.forEach(p -> dispersao.add(new DashboardDTO.ScatterData(p.getNomeFuncionario(), p.getTotalHoras(), p.getValorTotal())));
        dto.setDispersaoEquipe(dispersao);

        // 3. Alertas (Obras Paradas)
        List<Long> idsComAtividade = registroRepo.findObrasIdsComApontamento(inicio, fim);
        List<Obra> ativas = obraRepo.findByStatusOrderByIdDesc(StatusObra.ANDAMENTO);
        List<String> paradas = new ArrayList<>();
        for (Obra o : ativas) {
            if (!idsComAtividade.contains(o.getId())) paradas.add(o.getNome());
        }
        dto.setObrasSemApontamento(paradas);

        // 4. Cards Pagamento
        List<RegistroTrabalho> pendentes = registroRepo.findByPagamentoIsNull();
        Map<Long, DashboardDTO.PendenciaCard> mapPend = new HashMap<>();
        for(RegistroTrabalho r : pendentes) {
            mapPend.compute(r.getFuncionario().getId(), (k, v) -> {
                if(v==null) return new DashboardDTO.PendenciaCard(r.getFuncionario().getId(), r.getFuncionario().getNome(), r.getFuncionario().getProfissao(), r.getTotalCalculado(), 1L);
                v.setValor(v.getValor().add(r.getTotalCalculado()));
                v.setQtdRegistros(v.getQtdRegistros()+1);
                return v;
            });
        }
        dto.setCardsPagamento(new ArrayList<>(mapPend.values()));

        // Status Obras
        List<GraficoDTO> statusList = obraRepo.obrasPorStatus();
        Map<String, BigDecimal> mapStatus = new HashMap<>();
        statusList.forEach(s -> mapStatus.put(s.getLabel(), BigDecimal.valueOf(s.getValue())));
        dto.setStatusObras(mapStatus);

        return dto;
    }

    private Map<String, BigDecimal> getRanking(List<RegistroTrabalho> regs, boolean porObra) {
        return regs.stream().collect(Collectors.groupingBy(
                        r -> porObra ? r.getObra().getNome() : r.getFuncionario().getNome(),
                        Collectors.reducing(BigDecimal.ZERO, RegistroTrabalho::getTotalCalculado, BigDecimal::add)))
                .entrySet().stream().sorted(Map.Entry.<String, BigDecimal>comparingByValue().reversed())
                .limit(5).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }
}
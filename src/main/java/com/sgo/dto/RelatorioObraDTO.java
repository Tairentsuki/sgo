package com.sgo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor // Cria construtor com todos os argumentos (importante pro JPQL)
public class RelatorioObraDTO {
    private String nomeObra;
    private BigDecimal totalHoras;
    private BigDecimal custoTotal;
}
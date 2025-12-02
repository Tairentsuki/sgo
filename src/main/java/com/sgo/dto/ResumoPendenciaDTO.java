package com.sgo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ResumoPendenciaDTO {
    private Long funcionarioId;
    private String nomeFuncionario;
    private Long qtdRegistrosPendentes;
    private BigDecimal valorTotalPendente;
}
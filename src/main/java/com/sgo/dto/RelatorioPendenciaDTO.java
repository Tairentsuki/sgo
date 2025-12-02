package com.sgo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class RelatorioPendenciaDTO {
    private String nomeFuncionario;
    private BigDecimal totalHoras;
    private BigDecimal valorTotal;
}
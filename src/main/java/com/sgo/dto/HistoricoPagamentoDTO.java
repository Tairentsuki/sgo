package com.sgo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class HistoricoPagamentoDTO {
    private Long pagamentoId;
    private LocalDate dataPagamento;
    private String nomeFuncionario;
    private BigDecimal valorTotal;
    private Long qtdItens;
}
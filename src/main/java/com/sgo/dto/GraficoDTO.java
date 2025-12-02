package com.sgo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GraficoDTO {
    private String label; // Ex: "Em Andamento" ou "Obra A"
    private Long value;   // Ex: 10 ou 5000 (O Chart.js aceita números)
}
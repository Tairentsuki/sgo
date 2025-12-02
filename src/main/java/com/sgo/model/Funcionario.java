package com.sgo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Funcionario extends Pessoa {

    @NotNull(message = "A data de admissão é obrigatória")
    @PastOrPresent(message = "A data de admissão não pode ser futura")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dataAdmissao;

    @Column(length = 50)
    private String profissao;

    @NotNull(message = "O valor da hora é obrigatório")
    @Positive(message = "O valor da hora deve ser maior que zero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorHoraAtual;

    @Column(nullable = false)
    private boolean ativo = true;
}

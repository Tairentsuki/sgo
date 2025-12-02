package com.sgo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class RegistroTrabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data é obrigatória")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate data;

    @NotNull(message = "A quantidade de horas é obrigatória")
    @Positive(message = "As horas devem ser maiores que zero")
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal horas;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorHoraSnapshot;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCalculado;

    @Column(length = 255)
    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id", nullable = false)
    private Funcionario funcionario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id", nullable = false)
    private Obra obra;

    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;

    public boolean isPago() {
        return pagamento != null;
    }


}

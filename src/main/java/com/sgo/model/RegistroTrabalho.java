package com.sgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    // Removemos a validação @NotNull daqui pois o valor virá dos inputs temporários
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal horas;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valorHoraSnapshot;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal totalCalculado;

    private String observacao;

    @ManyToOne(optional = false)
    @JoinColumn(name = "funcionario_id")
    private Funcionario funcionario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "obra_id")
    private Obra obra;

    @ManyToOne
    @JoinColumn(name = "pagamento_id")
    private Pagamento pagamento;

    // --- CAMPOS TEMPORÁRIOS (Não vão pro Banco) ---
    @Transient
    private Integer inputHoras;

    @Transient
    private Integer inputMinutos;

    public boolean isPago() {
        return this.pagamento != null;
    }

    // --- LÓGICA DE CONVERSÃO ---

    // 1. Da Tela para o Banco (1h 30m -> 1.5)
    public void calcularDecimal() {
        double h = (inputHoras != null) ? inputHoras : 0;
        double m = (inputMinutos != null) ? inputMinutos : 0;
        // Fórmula: Horas + (Minutos / 60)
        this.horas = BigDecimal.valueOf(h + (m / 60.0)).setScale(2, RoundingMode.HALF_UP);
    }

    // 2. Do Banco para a Tela (1.5 -> 1h 30m)
    // Usado quando clicamos em "Editar"
    public void carregarInputs() {
        if (this.horas != null) {
            this.inputHoras = this.horas.intValue(); // Pega a parte inteira (1)

            // Pega a fração (0.5) e multiplica por 60 para achar os minutos (30)
            BigDecimal fracao = this.horas.subtract(BigDecimal.valueOf(this.inputHoras));
            this.inputMinutos = fracao.multiply(BigDecimal.valueOf(60)).intValue();
        }
    }
}
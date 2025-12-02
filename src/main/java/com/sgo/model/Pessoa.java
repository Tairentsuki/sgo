package com.sgo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    protected String nome;

    @NotBlank(message = "O CPF/CNPJ é obrigatório")
    @Size(min = 11, max = 20, message = "CPF/CNPJ deve ter entre 11 e 20 caracteres")
    @Pattern(regexp = "[0-9.\\-/]+", message = "O CPF/CNPJ deve conter apenas números e símbolos (. - /)")
    @Column(nullable = false, length = 20, unique = true)
    protected String cpf;
}
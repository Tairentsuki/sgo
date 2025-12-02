package com.sgo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Pessoa {

    @Column(length = 20)
    private String telefone;

    @Email(message = "Digite um e-mail válido")
    @Column(length = 100)
    private String email;

    @Column(length = 255)
    private String endereco;

    @Column(nullable = false)
    private boolean ativo = true;
}

package com.oasisfashion.backend.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data // Anotação do Lombok para gerar Getters, Setters, toString, etc.
@Entity // Indica que esta classe é uma entidade do banco de dados
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String email;
    private String senha; // Essencial para o login
    private LocalDate dataNascimento;
    private String sexo;
}

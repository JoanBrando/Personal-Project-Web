package com.oasisfashion.backend.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long id;
    private String email;
    private LocalDate dataNascimento;
    private String sexo;
    private PerfilDeUsuarioDTO perfil;
    private PessoaDTO pessoa;
}
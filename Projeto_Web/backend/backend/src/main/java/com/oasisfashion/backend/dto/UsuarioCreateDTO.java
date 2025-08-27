package com.oasisfashion.backend.dto;

import java.time.LocalDate;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UsuarioCreateDTO {
    @NotBlank @Email
    private String email;
    
    @NotBlank @Size(min = 6)
    private String senha;

    @NotNull
    private LocalDate dataNascimento;
    
    @NotBlank
    private String sexo;

    @NotNull(message = "O ID do perfil é obrigatório")
    private Long perfilId;

    @Valid // Valida o objeto aninhado
    @NotNull(message = "Os dados da pessoa são obrigatórios")
    private PessoaDTO pessoa;
}

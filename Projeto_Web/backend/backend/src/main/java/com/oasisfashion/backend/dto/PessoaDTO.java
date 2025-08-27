package com.oasisfashion.backend.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoPessoa")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ClienteDTO.class, name = "CLIENTE"),
    @JsonSubTypes.Type(value = FornecedorDTO.class, name = "FORNECEDOR")
})
public abstract class PessoaDTO {
    @NotBlank(message = "O nome da pessoa é obrigatório")
    private String nome;
    @NotBlank(message = "O documento (CPF/CNPJ) é obrigatório")
    private String documento;
    private String telefone;
    private String endereco;
}

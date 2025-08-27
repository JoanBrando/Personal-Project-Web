package com.oasisfashion.backend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FornecedorDTO extends PessoaDTO {
    private String listaProdutosFornecidos;
    private Integer prazoEntregaMedioDias;
}

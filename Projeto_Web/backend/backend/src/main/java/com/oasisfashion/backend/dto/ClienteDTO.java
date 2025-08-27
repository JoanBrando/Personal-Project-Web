package com.oasisfashion.backend.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClienteDTO extends PessoaDTO {
    private String historicoCompras;
    private BigDecimal limiteCredito;
}

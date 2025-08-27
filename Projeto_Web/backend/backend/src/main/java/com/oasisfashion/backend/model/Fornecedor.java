package com.oasisfashion.backend.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Fornecedor extends Pessoa {
    private String listaProdutosFornecidos;
    private Integer prazoEntregaMedioDias;
}
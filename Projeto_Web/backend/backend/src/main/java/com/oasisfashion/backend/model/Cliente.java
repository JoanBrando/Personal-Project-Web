package com.oasisfashion.backend.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Cliente extends Pessoa {
    private String historicoCompras;
    private BigDecimal limiteCredito;
}
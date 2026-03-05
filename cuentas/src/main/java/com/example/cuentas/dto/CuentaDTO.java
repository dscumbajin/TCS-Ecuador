package com.example.cuentas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO {
    private String numero;
    private String tipoCuenta;
    private Double saldoInicial;
    private boolean estado;
    private String nombre;
}

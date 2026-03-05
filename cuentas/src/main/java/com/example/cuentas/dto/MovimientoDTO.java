package com.example.cuentas.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {
    @NotBlank(message = "El numero de cuenta es obligatorio")
    private String numero;
    @NotBlank(message = "El tipo de transaccion es obligatorio")
    private String tipo;
    private Double saldo;
    private boolean estado;
    @NotBlank(message = "El valor es obligatorio")
    private String valor;
}

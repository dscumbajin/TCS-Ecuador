package com.example.cuentas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaRequestDTO {
    @NotBlank(message = "El numero de cuenta es requerido")
    private String numero;
    @NotBlank(message = "El tipo de cuenta es requerido")
    private String tipoCuenta;
    private Double saldoInicial;
    private boolean estado;
    @NotBlank(message = "La identificación es obligatoria")
    @Size(min = 10, max = 10, message = "La identificación debe tener 10 caracteres")
    private String identificacion;
}

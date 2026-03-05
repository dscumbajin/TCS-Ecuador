package com.example.cuentas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteDTO {
    public String fecha;
    public String cliente;
    public String numeroCuenta;
    public String tipo;
    public Double saldoInicial;
    public boolean estado;
    public String movimiento;
    public Double saldoDisponible;

}

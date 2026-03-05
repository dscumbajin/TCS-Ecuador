package com.example.cuentas.service;

import com.example.cuentas.dto.ReporteDTO;

import java.util.List;

public interface IReporteServiceImpl {

    List<ReporteDTO> findByCuentaNumeroAndFechaBetween(String numero, String fechaInicio, String fechaFin);
    List<ReporteDTO> findByNombreAndFechaBetween(String nombre, String fechaInicio, String fechaFin);
}

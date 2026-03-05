package com.example.cuentas.service;

import com.example.cuentas.dto.MovimientoDTO;
import com.example.cuentas.entity.Movimiento;

import java.util.List;

public interface IMoviminetoServiceImpl {

    Movimiento save(MovimientoDTO movimientoDTO);
    Movimiento update(Long id, MovimientoDTO movimientoDTO);
    boolean delete(Long id);
    List<MovimientoDTO> movimientoDtos();
    MovimientoDTO findById(Long id);
    List<MovimientoDTO> findByCuentaNumero(String numero);
}

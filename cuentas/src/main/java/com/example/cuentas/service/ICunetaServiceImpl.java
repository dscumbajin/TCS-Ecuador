package com.example.cuentas.service;

import com.example.cuentas.dto.CuentaDTO;
import com.example.cuentas.dto.CuentaRequestDTO;

import java.util.List;

public interface ICunetaServiceImpl {

    boolean save(CuentaRequestDTO cuentaDTO);
    boolean update(Long id, CuentaDTO cuentaDTO);
    boolean delete(Long id);
    List<CuentaDTO> cuentaDtos();
    CuentaDTO findById(Long id);
    CuentaDTO findByNumero(String numero);
}

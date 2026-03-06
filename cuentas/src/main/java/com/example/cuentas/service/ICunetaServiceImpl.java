package com.example.cuentas.service;

import com.example.cuentas.dto.CuentaDTO;
import com.example.cuentas.dto.CuentaRequestDTO;

import java.util.List;

public interface ICunetaServiceImpl {

    boolean save(CuentaRequestDTO cuentaDTO);
    boolean update(CuentaDTO cuentaDTO);
    boolean deleteByNumero(String numero);
    List<CuentaDTO> cuentaDtos();
    CuentaDTO findById(Long id);
    CuentaDTO findByNumero(String numero);
}

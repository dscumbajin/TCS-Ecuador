package com.example.clientes.service;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;

import java.util.List;

public interface IClienteServiceImpl {

    ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO);
    ClienteResponseDTO update(ClienteRequestDTO clienteRequestDTO);
    boolean deleteByIdentificacion(String identificacion);
    List<ClienteResponseDTO> clienteDTOs();
    ClienteResponseDTO findById(Long id);
    ClienteRequestDTO findByIdentificacion(String identificacion);
    ClienteRequestDTO findByNombre(String nombre);
}

package com.example.clientes.service;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;

import java.util.List;

public interface IClienteServiceImpl {

    boolean save(ClienteRequestDTO clienteRequestDTO);
    boolean update(Long id, ClienteRequestDTO clienteRequestDTO);
    boolean delete(Long id);
    List<ClienteResponseDTO> clienteDTOs();
    ClienteResponseDTO findById(Long id);
    ClienteRequestDTO findByIdentificacion(String identificacion);
    ClienteRequestDTO findByNombre(String nombre);
}

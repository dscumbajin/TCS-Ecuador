package com.example.clientes.service;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;
import com.example.clientes.entity.Cliente;
import com.example.clientes.exception.ClienteNotFoundException;
import com.example.clientes.exception.ClienteYaExisteException;
import com.example.clientes.mapper.ClientMapper;
import com.example.clientes.repository.ClienteRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ClienteServiceImpl implements IClienteServiceImpl{

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Override
    public ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO) {
        Cliente clientReq = clienteRepository.findByIdentificacion(clienteRequestDTO.getIdentificacion());
        if (clientReq != null) {
            throw new ClienteYaExisteException("El cliente con identificación " + clienteRequestDTO.getIdentificacion() + " ya existe");
        } else {
            Cliente clienteGuardado = clienteRepository.save(clientMapper.toEntity(clienteRequestDTO));
            return clientMapper.toClienteDTO(clienteGuardado);
        }
    }

    @Override
    public ClienteResponseDTO update(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = clienteRepository.findByIdentificacion(clienteRequestDTO.getIdentificacion());
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente no encontrado con identificación: " + clienteRequestDTO.getIdentificacion());
        } else {
            // Mantener el ID original del cliente
            clienteRequestDTO.setId(cliente.getId());
            Cliente clienteActualizado = clienteRepository.save(clientMapper.toEntity(clienteRequestDTO));
            return clientMapper.toClienteDTO(clienteActualizado);
        }
    }

    @Override
    public boolean deleteByIdentificacion(String identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion);
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente no encontrado con identificación: " + identificacion);
        } else {
            clienteRepository.deleteById(cliente.getId());
            return true;
        }
    }

    @Override
    public List<ClienteResponseDTO> clienteDTOs() {
        List<Cliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(clientMapper::toClienteDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNotFoundException("Cliente no encontrado con ID: " + id));
        return clientMapper.toClienteDTO(cliente);
    }

    @Override
    public ClienteRequestDTO findByIdentificacion(String identificacion) {
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion);
        if (cliente == null) {
            throw new ClienteNotFoundException("Cliente no encontrado con identificacion: " + identificacion);
        } else {
            return clientMapper.toClienteIdDTO(cliente);
        }
    }

    @Override
    public ClienteRequestDTO findByNombre(String nombre) {
        Cliente cliente = clienteRepository.findByNombre(nombre);
        if (cliente == null) {
            throw new ClienteNotFoundException("No existe el cliente: " + nombre);
        } else {
            return clientMapper.toClienteIdDTO(cliente);
        }
    }
}


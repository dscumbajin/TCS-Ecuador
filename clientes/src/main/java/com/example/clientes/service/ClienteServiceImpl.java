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
    public boolean save(ClienteRequestDTO clienteRequestDTO) {
        Cliente clientReq = clienteRepository.findByIdentificacion(clienteRequestDTO.getIdentificacion());
        if (clientReq != null) {
            throw new ClienteYaExisteException("La identificación debe ser única");
        } else {
            clienteRepository.save(clientMapper.toEntity(clienteRequestDTO));
            return true;
        }
    }

    @Override
    public boolean update(Long id, ClienteRequestDTO clienteRequestDTO) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) {
            throw new ClienteNotFoundException("Cliente no encontrado con ID: " + id);
        } else {
            clienteRepository.save(clientMapper.toEntity(clienteRequestDTO));
        }
        return true;
    }

    @Override
    public boolean delete(Long id) {

        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (clienteOptional.isEmpty()) {
            throw new ClienteNotFoundException("Cliente no encontrado con ID: " + id);
        } else {
            clienteRepository.deleteById(id);
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


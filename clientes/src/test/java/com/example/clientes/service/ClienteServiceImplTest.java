package com.example.clientes.service;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;
import com.example.clientes.entity.Cliente;
import com.example.clientes.exception.ClienteNotFoundException;
import com.example.clientes.exception.ClienteYaExisteException;
import com.example.clientes.mapper.ClientMapper;
import com.example.clientes.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO clienteRequestDTO;
    private ClienteResponseDTO clienteResponseDTO;


    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Test Cliente");
        cliente.setIdentificacion("1234567890");

        clienteRequestDTO = new ClienteRequestDTO();
        clienteRequestDTO.setId(1L);
        clienteRequestDTO.setNombre("Test Cliente");
        clienteRequestDTO.setIdentificacion("1234567890");

        clienteResponseDTO = new ClienteResponseDTO();
        clienteResponseDTO.setNombre("Test Cliente");
    }

    @Test
    public void save() {
        ClienteRequestDTO cliente = new ClienteRequestDTO(1L, "Juan", "Masculino", 20, "1234567890", "Quito", "0987654321", "admin", true);
        when(clienteRepository.findByIdentificacion(cliente.getIdentificacion())).thenReturn(null);
        boolean result = clienteService.save(cliente);
        assertTrue(result);
        verify(clienteRepository, times(1)).save(clientMapper.toEntity(cliente));
    }

    @Test
    public void saveExistingCliente() {
        when(clienteRepository.findByIdentificacion(clienteRequestDTO.getIdentificacion())).thenReturn(cliente);
        assertThrows(ClienteYaExisteException.class, () -> clienteService.save(clienteRequestDTO));
    }

    @Test
    void clienteDTOs() {
        List<Cliente> clientes = List.of(cliente);
        when(clienteRepository.findAll()).thenReturn(clientes);
        when(clientMapper.toClienteDTO(cliente)).thenReturn(clienteResponseDTO);
        List<ClienteResponseDTO> result = clienteService.clienteDTOs();
        assertEquals(1, result.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    public void findById() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clientMapper.toClienteDTO(cliente)).thenReturn(clienteResponseDTO);
        ClienteResponseDTO result = clienteService.findById(1L);
        assertNotNull(result);
        assertEquals("Test Cliente", result.getNombre());
    }

    @Test
    public void delete() {
        Long idCliente = 1L;
        Cliente cliente = new Cliente("test",true);
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.of(cliente));
        boolean result = clienteService.delete(idCliente);
        assertTrue(result);
        verify(clienteRepository, times(1)).deleteById(idCliente);
    }

    @Test
    public void updateExistingCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clientMapper.toEntity(clienteRequestDTO)).thenReturn(cliente);
        boolean result = clienteService.update(1L, clienteRequestDTO);
        assertTrue(result);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void updateNonExistingCliente() {
        Long idCliente = 1L;
        ClienteRequestDTO clienteNoExistente = new ClienteRequestDTO(1L, "Juan", "Masculino", 20, "1234567890", "Quito", "0987654321", "admin", true);
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());
        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class,
                () -> clienteService.update(idCliente, clienteNoExistente));
        assertEquals("Cliente no encontrado con ID: " + idCliente, exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void deleteNonExistingCliente() {
        Long idCliente = 1L;
        when(clienteRepository.findById(idCliente)).thenReturn(Optional.empty());
        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class,
                () -> clienteService.delete(idCliente));
        assertEquals("Cliente no encontrado con ID: " + idCliente, exception.getMessage());
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    void findClienteByNombre() {
        when(clienteRepository.findByNombre("Test Cliente")).thenReturn(cliente);
        when(clientMapper.toClienteIdDTO(cliente)).thenReturn(clienteRequestDTO);
        ClienteRequestDTO result = clienteService.findByNombre("Test Cliente");
        assertNotNull(result);
        assertEquals("Test Cliente", result.getNombre());
    }

    @Test
    void findClienteByNombreThrowsClienteNotFoundException() {
        when(clienteRepository.findByNombre("Test Cliente")).thenReturn(null);
        assertThrows(ClienteNotFoundException.class, () -> clienteService.findByNombre("Test Cliente"));
    }
}
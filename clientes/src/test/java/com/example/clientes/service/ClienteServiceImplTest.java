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
        Cliente clienteEntity = new Cliente();
        when(clienteRepository.findByIdentificacion(cliente.getIdentificacion())).thenReturn(null);
        when(clientMapper.toEntity(cliente)).thenReturn(clienteEntity);
        when(clienteRepository.save(clienteEntity)).thenReturn(clienteEntity);
        when(clientMapper.toClienteDTO(clienteEntity)).thenReturn(clienteResponseDTO);
        ClienteResponseDTO result = clienteService.save(cliente);
        assertNotNull(result);
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
    public void deleteByIdentificacion() {
        String identification = "1";
        Cliente cliente = new Cliente("test",true);
        when(clienteRepository.findByIdentificacion(identification)).thenReturn(cliente);
        boolean result = clienteService.deleteByIdentificacion(identification);
        assertTrue(result);
        verify(clienteRepository, times(1)).findByIdentificacion(identification);
    }

    @Test
    public void updateExistingCliente() {
        when(clienteRepository.findByIdentificacion(clienteRequestDTO.getIdentificacion())).thenReturn(cliente);
        when(clientMapper.toEntity(clienteRequestDTO)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clientMapper.toClienteDTO(cliente)).thenReturn(clienteResponseDTO);
        ClienteResponseDTO result = clienteService.update(clienteRequestDTO);
        assertNotNull(result);
        verify(clienteRepository, times(1)).save(cliente);
    }

    @Test
    public void updateNonExistingCliente() {
        Long idCliente = 1L;
        ClienteRequestDTO clienteNoExistente = new ClienteRequestDTO(1L, "Juan", "Masculino", 20, "1234567890", "Quito", "0987654321", "admin", true);
        when(clienteRepository.findByIdentificacion(clienteNoExistente.getIdentificacion())).thenReturn(null);
        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class,
                () -> clienteService.update(clienteNoExistente));
        assertEquals("Cliente no encontrado con identificación: " + clienteNoExistente.getIdentificacion(), exception.getMessage());
        verify(clienteRepository, never()).save(any());
    }

    @Test
    public void deleteNonExistingCliente() {
        String identification = "1";
        when(clienteRepository.findByIdentificacion(identification)).thenReturn(null);
        ClienteNotFoundException exception = assertThrows(ClienteNotFoundException.class,
                () -> clienteService.deleteByIdentificacion(identification));
        assertEquals("Cliente no encontrado con identificación: " + identification, exception.getMessage());
        verify(clienteRepository, times(1)).findByIdentificacion(identification);
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
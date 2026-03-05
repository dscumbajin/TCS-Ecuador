package com.example.cuentas.service;

import com.example.cuentas.client.ClienteClient;
import com.example.cuentas.dto.MovimientoDTO;
import com.example.cuentas.entity.Cuenta;
import com.example.cuentas.entity.Movimiento;
import com.example.cuentas.exception.CuentaNotFoundException;
import com.example.cuentas.exception.MovimientoNotFoundException;
import com.example.cuentas.mapper.MovimientoMappers;
import com.example.cuentas.repository.CuentaRepository;
import com.example.cuentas.repository.MovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimientoServiceImplTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private MovimientoMappers movimientoMappers;

    @Mock
    private DepositoSaldoImpl depositoSaldo;

    @Mock
    private RetiroSaldoImpl retiroSaldo;

    @InjectMocks
    private MovimientoServiceImpl movimientoService;

    @Mock
    private ClienteClient clienteClient;
    String nombre;
    String fechaInicio;
    String fechaFin;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        nombre = "Juan Pérez";
        fechaInicio = "01/08/2024";
        fechaFin = "09/08/2024";
    }

    @Test
    void testSaveMovimiento() {
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setNumero("12345");
        movimientoDTO.setTipo("Deposito");
        movimientoDTO.setValor("100.00");

        Cuenta cuenta = new Cuenta();
        cuenta.setNumero("12345");
        cuenta.setSaldoInicial(500.00);

        when(cuentaRepository.findByNumero("12345")).thenReturn(cuenta);
        when(movimientoMappers.toMovimiento(movimientoDTO)).thenReturn(new Movimiento());
        when(depositoSaldo.ajustarSaldo(movimientoDTO.getValor(),cuenta.getSaldoInicial())).thenReturn(600.00);
        Movimiento result = movimientoService.save(movimientoDTO);

        assertEquals(600.00, result.getSaldo());
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void testSaveMovimientoCuentaNoEncontrada() {
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setNumero("99999");

        when(cuentaRepository.findByNumero("99999")).thenReturn(null);

        assertThrows(CuentaNotFoundException.class, () -> {
            movimientoService.save(movimientoDTO);
        });
    }

    @Test
    void testUpdateMovimiento() {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setSaldo(500.00);

        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setTipo("Retiro");
        movimientoDTO.setValor("100.00");

        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        when(retiroSaldo.ajustarSaldo(movimientoDTO.getValor(),movimiento.getSaldo())).thenReturn(400.00);
        Movimiento updatedMovimiento = movimientoService.update(1L, movimientoDTO);

        assertEquals(400.00, updatedMovimiento.getSaldo());
        verify(movimientoRepository, times(1)).save(movimiento);
    }

    @Test
    void testUpdateMovimientoNoEncontrado() {
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setTipo("Deposito");
        movimientoDTO.setValor("100.00");

        when(movimientoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovimientoNotFoundException.class, () -> {
            movimientoService.update(1L, movimientoDTO);
        });
    }

    @Test
    void testDeleteMovimiento() {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);

        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        boolean result = movimientoService.delete(1L);

        assertTrue(result);
        verify(movimientoRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMovimientoNoEncontrado() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(MovimientoNotFoundException.class, () -> {
            movimientoService.delete(1L);
        });
    }

    @Test
    void testMovimientoDtos() {
        Movimiento movimiento1 = new Movimiento();
        Movimiento movimiento2 = new Movimiento();
        List<Movimiento> movimientos = Arrays.asList(movimiento1, movimiento2);
        MovimientoDTO movimientoDTO1 = new MovimientoDTO();
        MovimientoDTO movimientoDTO2 = new MovimientoDTO();
        when(movimientoRepository.findAll()).thenReturn(movimientos);
        when(movimientoMappers.toMovimientoDTO(movimiento1)).thenReturn(movimientoDTO1);
        when(movimientoMappers.toMovimientoDTO(movimiento2)).thenReturn(movimientoDTO2);
        List<MovimientoDTO> result = movimientoService.movimientoDtos();
        assertEquals(2, result.size());
        verify(movimientoMappers, times(1)).toMovimientoDTO(movimiento1);
        verify(movimientoMappers, times(1)).toMovimientoDTO(movimiento2);
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdMovimientoExistente() {
        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));
        when(movimientoMappers.toMovimientoDTO(movimiento)).thenReturn(movimientoDTO);
        MovimientoDTO result = movimientoService.findById(1L);
        assertEquals(movimientoDTO, result);
        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoMappers, times(1)).toMovimientoDTO(movimiento);
    }

    @Test
    void testFindByIdMovimientoNoEncontrado() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(MovimientoNotFoundException.class, () -> {
            movimientoService.findById(1L);
        });
        verify(movimientoRepository, times(1)).findById(1L);
        verify(movimientoMappers, never()).toMovimientoDTO(any());
    }

    @Test
    void testFindByCuentaNumeroConMovimientos() {
        Movimiento movimiento1 = new Movimiento();
        Movimiento movimiento2 = new Movimiento();
        List<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(movimiento1);
        movimientos.add(movimiento2);
        MovimientoDTO movimientoDTO1 = new MovimientoDTO();
        MovimientoDTO movimientoDTO2 = new MovimientoDTO();
        when(movimientoRepository.findByCuentaNumero("12345")).thenReturn(movimientos);
        when(movimientoMappers.toMovimientoDTO(movimiento1)).thenReturn(movimientoDTO1);
        when(movimientoMappers.toMovimientoDTO(movimiento2)).thenReturn(movimientoDTO2);
        List<MovimientoDTO> result = movimientoService.findByCuentaNumero("12345");
        assertEquals(2, result.size());
        assertEquals(movimientoDTO1, result.get(0));
        assertEquals(movimientoDTO2, result.get(1));
        verify(movimientoRepository, times(1)).findByCuentaNumero("12345");
        verify(movimientoMappers, times(1)).toMovimientoDTO(movimiento1);
        verify(movimientoMappers, times(1)).toMovimientoDTO(movimiento2);
    }

    @Test
    void testFindByCuentaNumeroSinMovimientos() {
        when(movimientoRepository.findByCuentaNumero("12345")).thenReturn(new ArrayList<>());
        assertThrows(MovimientoNotFoundException.class, () -> {
            movimientoService.findByCuentaNumero("12345");
        });
        verify(movimientoRepository, times(1)).findByCuentaNumero("12345");
        verify(movimientoMappers, never()).toMovimientoDTO(any());
    }

}
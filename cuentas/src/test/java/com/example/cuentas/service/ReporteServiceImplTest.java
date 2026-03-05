package com.example.cuentas.service;

import com.example.cuentas.client.ClienteClient;
import com.example.cuentas.dto.ClienteDTO;
import com.example.cuentas.dto.ReporteDTO;
import com.example.cuentas.entity.Cuenta;
import com.example.cuentas.entity.Movimiento;
import com.example.cuentas.exception.MovimientoNotFoundException;
import com.example.cuentas.mapper.MovimientoMappers;
import com.example.cuentas.repository.CuentaRepository;
import com.example.cuentas.repository.MovimientoRepository;
import com.example.cuentas.util.Conversion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ReporteServiceImplTest {

    @InjectMocks
    private ReporteServiceImpl reporteService;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private MovimientoMappers movimientoMappers;

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
    void testFindByCuentaNumeroAndFechaBetweenConMovimientos() {
        String numeroCuenta = "12345";
        Date inicioFecha = Conversion.convertStringToDate(fechaInicio);
        Date finFecha = Conversion.convertStringToDate(fechaFin);

        Cuenta cuenta = new Cuenta();
        cuenta.setClienteId("1");
        cuenta.setNumero(numeroCuenta);

        Movimiento movimiento1 = new Movimiento();
        movimiento1.setCuenta(cuenta);
        Movimiento movimiento2 = new Movimiento();
        movimiento2.setCuenta(cuenta);

        List<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(movimiento1);
        movimientos.add(movimiento2);

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setNombre("Juan Pérez");

        ReporteDTO reporteDTO1 = new ReporteDTO();
        ReporteDTO reporteDTO2 = new ReporteDTO();

        when(movimientoRepository.findByCuentaNumeroAndFechaBetween(numeroCuenta, inicioFecha, finFecha)).thenReturn(movimientos);
        when(clienteClient.getClienteById(anyLong())).thenReturn(clienteDTO);
        when(movimientoMappers.toReporteDTO(movimiento1)).thenReturn(reporteDTO1);
        when(movimientoMappers.toReporteDTO(movimiento2)).thenReturn(reporteDTO2);

        List<ReporteDTO> result = reporteService.findByCuentaNumeroAndFechaBetween(numeroCuenta, fechaInicio, fechaFin);

        assertEquals(2, result.size());
        assertEquals("Juan Pérez", result.get(0).getCliente());
        assertEquals("Juan Pérez", result.get(1).getCliente());

        verify(movimientoRepository, times(1)).findByCuentaNumeroAndFechaBetween(numeroCuenta, inicioFecha, finFecha);
        verify(clienteClient, times(2)).getClienteById(anyLong());
        verify(movimientoMappers, times(2)).toReporteDTO(any(Movimiento.class));
    }

    @Test
    void testFindByCuentaNumeroAndFechaBetweenSinMovimientos() {

        String numeroCuenta = "12345";
        Date inicioFecha = Conversion.convertStringToDate(fechaInicio);
        Date finFecha = Conversion.convertStringToDate(fechaFin);

        when(movimientoRepository.findByCuentaNumeroAndFechaBetween(numeroCuenta, inicioFecha, finFecha)).thenReturn(new ArrayList<>());

        assertThrows(MovimientoNotFoundException.class, () -> {
            reporteService.findByCuentaNumeroAndFechaBetween(numeroCuenta, fechaInicio, fechaFin);
        });

        verify(movimientoRepository, times(1)).findByCuentaNumeroAndFechaBetween(numeroCuenta, inicioFecha, finFecha);
        verify(clienteClient, never()).getClienteById(anyLong());
        verify(movimientoMappers, never()).toReporteDTO(any());
    }

    @Test
    void testFindByNombreAndFechaBetweenConMovimientos() {
        Date inicioFecha = Conversion.convertStringToDate(fechaInicio);
        Date finFecha = Conversion.convertStringToDate(fechaFin);

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre(nombre);

        Cuenta cuenta = new Cuenta();
        cuenta.setClienteId("1");
        cuenta.setNumero("12345");

        Movimiento movimiento1 = new Movimiento();
        movimiento1.setCuenta(cuenta);
        movimiento1.setTipo("Retiro");
        movimiento1.setValor("-100");

        Movimiento movimiento2 = new Movimiento();
        movimiento2.setCuenta(cuenta);
        movimiento2.setTipo("Depósito");
        movimiento2.setValor("200");

        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(cuenta);

        List<Movimiento> movimientos = new ArrayList<>();
        movimientos.add(movimiento1);
        movimientos.add(movimiento2);

        ReporteDTO reporteDTO1 = new ReporteDTO();
        reporteDTO1.setCliente(nombre);
        reporteDTO1.setMovimiento("-100");

        ReporteDTO reporteDTO2 = new ReporteDTO();
        reporteDTO2.setCliente(nombre);
        reporteDTO2.setMovimiento("200");

        when(clienteClient.getClienteByName(nombre)).thenReturn(clienteDTO);
        when(cuentaRepository.findByClienteId(clienteDTO.getId().toString())).thenReturn(cuentas);
        when(movimientoRepository.findByCuentaNumeroAndFechaBetween(cuenta.getNumero(), inicioFecha, finFecha)).thenReturn(movimientos);
        when(clienteClient.getClienteById(anyLong())).thenReturn(clienteDTO);
        when(movimientoMappers.toReporteDTO(movimiento1)).thenReturn(reporteDTO1);
        when(movimientoMappers.toReporteDTO(movimiento2)).thenReturn(reporteDTO2);

        List<ReporteDTO> result = reporteService.findByNombreAndFechaBetween(nombre, fechaInicio, fechaFin);

        assertEquals(2, result.size());
        assertEquals("-100", result.get(0).getMovimiento());
        assertEquals("200", result.get(1).getMovimiento());

        verify(clienteClient, times(1)).getClienteByName(nombre);
        verify(cuentaRepository, times(1)).findByClienteId(clienteDTO.getId().toString());
        verify(movimientoRepository, times(1)).findByCuentaNumeroAndFechaBetween(cuenta.getNumero(), inicioFecha, finFecha);
        verify(movimientoMappers, times(2)).toReporteDTO(any(Movimiento.class));
    }

    @Test
    void testFindByNombreAndFechaBetweenSinMovimientos() {
        Date inicioFecha = Conversion.convertStringToDate(fechaInicio);
        Date finFecha = Conversion.convertStringToDate(fechaFin);

        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(1L);
        clienteDTO.setNombre(nombre);

        List<Cuenta> cuentas = new ArrayList<>();
        Cuenta cuenta = new Cuenta();
        cuenta.setNumero("12345");
        cuentas.add(cuenta);

        when(clienteClient.getClienteByName(nombre)).thenReturn(clienteDTO);
        when(cuentaRepository.findByClienteId(clienteDTO.getId().toString())).thenReturn(cuentas);
        when(movimientoRepository.findByCuentaNumeroAndFechaBetween(cuenta.getNumero(), inicioFecha, finFecha)).thenReturn(new ArrayList<>());

        List<ReporteDTO> result = reporteService.findByNombreAndFechaBetween(nombre, fechaInicio, fechaFin);

        assertTrue(result.isEmpty());

        verify(clienteClient, times(1)).getClienteByName(nombre);
        verify(cuentaRepository, times(1)).findByClienteId(clienteDTO.getId().toString());
        verify(movimientoRepository, times(1)).findByCuentaNumeroAndFechaBetween(cuenta.getNumero(), inicioFecha, finFecha);
        verify(movimientoMappers, never()).toReporteDTO(any(Movimiento.class));
    }

    @Test
    void testFindByNombreAndFechaBetweenClienteNoEncontrado() {
        when(clienteClient.getClienteByName(nombre)).thenReturn(null);

        List<ReporteDTO> result = reporteService.findByNombreAndFechaBetween(nombre, fechaInicio, fechaFin);

        assertTrue(result.isEmpty());

        verify(clienteClient, times(1)).getClienteByName(nombre);
        verify(cuentaRepository, never()).findByClienteId(anyString());
        verify(movimientoRepository, never()).findByCuentaNumeroAndFechaBetween(anyString(), any(Date.class), any(Date.class));
        verify(movimientoMappers, never()).toReporteDTO(any(Movimiento.class));
    }

}
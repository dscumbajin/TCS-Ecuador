package com.example.cuentas.service;

import com.example.cuentas.client.ClienteClient;
import com.example.cuentas.dto.ClienteDTO;
import com.example.cuentas.dto.ReporteDTO;
import com.example.cuentas.entity.Cuenta;
import com.example.cuentas.entity.Movimiento;
import com.example.cuentas.exception.ClienteServiceUnavailableException;
import com.example.cuentas.exception.CuentaNotFoundException;
import com.example.cuentas.exception.MovimientoNotFoundException;
import com.example.cuentas.mapper.MovimientoMappers;
import com.example.cuentas.repository.CuentaRepository;
import com.example.cuentas.repository.MovimientoRepository;
import com.example.cuentas.util.Conversion;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReporteServiceImpl implements IReporteServiceImpl {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private MovimientoMappers movimientoMappers;
    /**
     * @param numero
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    @Override
    public List<ReporteDTO> findByCuentaNumeroAndFechaBetween(String numero, String fechaInicio, String fechaFin) {
        try{
            Date inicioFecha = Conversion.convertStringToDate(fechaInicio);
            Date finFecha = Conversion.convertStringToDate(fechaFin);
            List<ReporteDTO> reporteDTOS = new ArrayList<>();
            List<Movimiento> movimientos = movimientoRepository.findByCuentaNumeroAndFechaBetween(numero, inicioFecha, finFecha);
            if(!movimientos.isEmpty()){
                for (Movimiento movimiento : movimientos) {
                    ClienteDTO cliente = clienteClient.getClienteById(Long.parseLong(movimiento.getCuenta().getClienteId()));
                    ReporteDTO reporteDTO = movimientoMappers.toReporteDTO(movimiento);
                    reporteDTO.setCliente(cliente.getNombre());
                    reporteDTOS.add(reporteDTO);
                }
            }else {
                throw new MovimientoNotFoundException("No se encontro el movimientos con el numero de cuenta : "
                        + numero +" en el rango de fechas: " + fechaInicio + " - " + fechaFin );
            }
            return reporteDTOS;
        } catch (FeignException.NotFound e) {
            throw new CuentaNotFoundException("Cliente no encontrado");
        } catch (FeignException e) {
            throw new ClienteServiceUnavailableException("No se pudo conectar al servicio de clientes");
        }
    }

    /**
     * @param nombre
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    @Override
    @Retryable(value = ClienteServiceUnavailableException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<ReporteDTO> findByNombreAndFechaBetween(String nombre, String fechaInicio, String fechaFin) {
        try{
            Date inicioFecha = Conversion.convertStringToDate(fechaInicio);
            Date finFecha = Conversion.convertStringToDate(fechaFin);
            List<ReporteDTO> reporteDTOS = new ArrayList<>();
            ClienteDTO cliente = clienteClient.getClienteByName(nombre);
            if(cliente != null){
                List<Cuenta> cuentas = cuentaRepository.findByClienteId(cliente.getId().toString());
                if(!cuentas.isEmpty()){
                    for (Cuenta cuenta : cuentas) {
                        List<Movimiento> movimientos = movimientoRepository.findByCuentaNumeroAndFechaBetween(cuenta.getNumero(), inicioFecha, finFecha);
                        if(!movimientos.isEmpty()){
                            for (Movimiento movimiento : movimientos) {
                                ClienteDTO cliente1 = clienteClient.getClienteById(Long.parseLong(movimiento.getCuenta().getClienteId()));
                                ReporteDTO reporteDTO = movimientoMappers.toReporteDTO(movimiento);
                                reporteDTO.setCliente(cliente1.getNombre());
                                if("Retiro".equals(movimiento.getTipo()) && !movimiento.getValor().contains("-")){
                                    reporteDTO.setMovimiento("-"+movimiento.getValor());
                                }
                                reporteDTOS.add(reporteDTO);
                            }
                        }
                    }
                }
            }
            return reporteDTOS;
        } catch (FeignException.NotFound e) {
            throw new CuentaNotFoundException("Cliente no encontrado");
        } catch (FeignException e) {
            throw new ClienteServiceUnavailableException("No se pudo conectar al servicio de clientes");
        }
    }
}

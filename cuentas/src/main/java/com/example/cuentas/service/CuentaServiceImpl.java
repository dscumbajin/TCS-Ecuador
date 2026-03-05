package com.example.cuentas.service;

import com.example.cuentas.client.ClienteClient;
import com.example.cuentas.dto.ClienteDTO;
import com.example.cuentas.dto.CuentaDTO;
import com.example.cuentas.dto.CuentaRequestDTO;
import com.example.cuentas.entity.Cuenta;
import com.example.cuentas.exception.CuentaNotFoundException;
import com.example.cuentas.exception.CuentaYaExisteException;
import com.example.cuentas.exception.ClienteServiceUnavailableException;
import com.example.cuentas.mapper.CuentaMappers;
import com.example.cuentas.repository.CuentaRepository;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaServiceImpl implements ICunetaServiceImpl {

    @Autowired
    private CuentaRepository cuentaRepository;

    @Autowired
    private ClienteClient clienteClient;

    @Autowired
    private CuentaMappers cuentaMappers;

    @Override
    @Retryable(value = ClienteServiceUnavailableException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public boolean save(CuentaRequestDTO cuentaRequestDTO) {
        try {
            if (cuentaRequestDTO.getNumero() == null || cuentaRequestDTO.getIdentificacion() == null) {
                throw new CuentaNotFoundException("Número de cuenta e identificación son requeridos");
            }
            if (cuentaRepository.existsByNumero(cuentaRequestDTO.getNumero())) {
                throw new CuentaYaExisteException("El número de cuenta debe ser único");
            }
            ClienteDTO clienteDTO = clienteClient.getCliente(cuentaRequestDTO.getIdentificacion());
            if (clienteDTO == null) {
                throw new CuentaNotFoundException("Cliente no encontrado");
            }
            Cuenta cuenta = cuentaMappers.toCuenta(cuentaRequestDTO);
            cuenta.setClienteId(clienteDTO.getId().toString());
            cuentaRepository.save(cuenta);
            return true;

        } catch (FeignException.NotFound e) {
            throw new CuentaNotFoundException("Cliente no encontrado");
        } catch (FeignException e) {
            throw new ClienteServiceUnavailableException("No se pudo conectar al servicio de clientes");
        }
    }

    @Override
    public boolean update(Long id, CuentaDTO cuentaDTO) {
        Cuenta cuenta = cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("No existe la cuenta con el ID: " + id));
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setEstado(cuentaDTO.isEstado());
        cuentaRepository.save(cuenta);
        return true;
    }

    @Override
    public boolean delete(Long id) {
        cuentaRepository.findById(id)
                .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));
        cuentaRepository.deleteById(id);
        return true;
    }

    @Override
    @Retryable(value = ClienteServiceUnavailableException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public List<CuentaDTO> cuentaDtos() {
        try {
        return cuentaRepository.findAll().stream()
                .peek(cuenta -> {
                    ClienteDTO client = clienteClient.getClienteById(Long.parseLong(cuenta.getClienteId()));
                    cuenta.setClienteId(client.getNombre());
                })
                .map(cuentaMappers::toCuentaDTO)
                .collect(Collectors.toList());
        } catch (FeignException.NotFound e) {
            throw new CuentaNotFoundException("Cliente no encontrado");
        } catch (FeignException e) {
            throw new ClienteServiceUnavailableException("No se pudo conectar al servicio de clientes");
        }
    }

    @Override
    @Retryable(value = ClienteServiceUnavailableException.class, maxAttempts = 3, backoff = @Backoff(delay = 2000))
    public CuentaDTO findById(Long id) {
        try {
            Cuenta cuenta = cuentaRepository.findById(id)
                    .orElseThrow(() -> new CuentaNotFoundException("Cuenta no encontrada con ID: " + id));
            ClienteDTO clienteDTO = clienteClient.getClienteById(Long.parseLong(cuenta.getClienteId()));
            cuenta.setClienteId(clienteDTO.getNombre());
            return cuentaMappers.toCuentaDTO(cuenta);
        } catch (FeignException.NotFound e) {
            throw new CuentaNotFoundException("Cliente no encontrado");
        } catch (FeignException e) {
            throw new ClienteServiceUnavailableException("No se pudo conectar al servicio de clientes");
        }
    }

    @Override
    public CuentaDTO findByNumero(String numero) {
        try {

            if (!cuentaRepository.existsByNumero(numero)) {
                throw new CuentaYaExisteException("Cuenta no encontrada");
            }
            Cuenta cuenta = cuentaRepository.findByNumero(numero);
            ClienteDTO clienteDTO = clienteClient.getClienteById(Long.parseLong(cuenta.getClienteId()));
            cuenta.setClienteId(clienteDTO.getNombre());
            return cuentaMappers.toCuentaDTO(cuenta);
        } catch (FeignException.NotFound e) {
            throw new CuentaNotFoundException("Cliente no encontrado");
        } catch (FeignException e) {
            throw new ClienteServiceUnavailableException("No se pudo conectar al servicio de clientes");
        }
    }
}

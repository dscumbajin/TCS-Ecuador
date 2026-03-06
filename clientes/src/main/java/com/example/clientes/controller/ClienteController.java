package com.example.clientes.controller;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;
import com.example.clientes.exception.ClienteNotFoundException;
import com.example.clientes.mapper.ClientMapper;
import com.example.clientes.service.IClienteServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/clientes")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:80"})
public class ClienteController {

    @Autowired
    private IClienteServiceImpl clienteService;

    @Autowired
    private ClientMapper clientMapper;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> getAllClientes() {
        List<ClienteResponseDTO> clienteResponseDTOS = clienteService.clienteDTOs();
        return ResponseEntity.ok(clienteResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getClienteById(@PathVariable Long id) {
        try {
            ClienteResponseDTO clienteResponseDTO = clienteService.findById(id);
            return new ResponseEntity<>(clienteResponseDTO, HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/cliente")
    public ResponseEntity<?> getClienteByIdentificacion(@RequestParam String identificacion) {
        try {
            ClienteRequestDTO clienteRequestDTO = clienteService.findByIdentificacion(identificacion);
            return new ResponseEntity<>(clienteRequestDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/nombre")
    public ResponseEntity<?> getClienteByName(@RequestParam String nombre) {
        try {
            ClienteRequestDTO clienteRequestDTO = clienteService.findByNombre(nombre);
            return new ResponseEntity<>(clienteRequestDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCliente(@Valid @RequestBody ClienteRequestDTO ClienteRequestDTO) {
        try {
            return new ResponseEntity<>(clienteService.save(ClienteRequestDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            // No manejar errores aquí, dejar que el GlobalExceptionHandler los procese
            throw e;
        }
    }
    @Transactional
    @PutMapping
    public ResponseEntity<?> updateCliente(@RequestBody ClienteRequestDTO ClienteRequestDTO) {
        try {
            ClienteResponseDTO clienteActualizado = clienteService.update(ClienteRequestDTO);
            return new ResponseEntity<>(clienteActualizado, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    @DeleteMapping("/{identificacion}")
    public ResponseEntity<?> deleteCliente(@PathVariable String identificacion) {
        try {
            clienteService.deleteByIdentificacion(identificacion);
            return new ResponseEntity<>(Map.of("message", "Cliente eliminado correctamente"), HttpStatus.OK);
        } catch (ClienteNotFoundException e) {
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Error al eliminar cliente: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

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

@RestController
@RequestMapping("api/clientes")
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
    public ResponseEntity<?> createCliente( @Valid @RequestBody ClienteRequestDTO ClienteRequestDTO) {
        try {
            return new ResponseEntity<>(clienteService.save(ClienteRequestDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @RequestBody ClienteRequestDTO ClienteRequestDTO) {
        try {
            boolean resp = clienteService.update(id, ClienteRequestDTO);
            return new ResponseEntity<>("Cliente actualizado", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCliente(@PathVariable Long id) {
        try {
            clienteService.delete(id);
            return new ResponseEntity<>("Cliente eliminado correctamente", HttpStatus.OK);
        } catch (ClienteNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}

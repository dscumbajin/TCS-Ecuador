package com.example.cuentas.controller;

import com.example.cuentas.dto.CuentaDTO;
import com.example.cuentas.dto.CuentaRequestDTO;
import com.example.cuentas.exception.CuentaNotFoundException;
import com.example.cuentas.exception.CuentaYaExisteException;
import com.example.cuentas.service.ICunetaServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/cuentas")
public class CuentaController {

    @Autowired
    private ICunetaServiceImpl cuentaService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCuentas() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<CuentaDTO> cuentas = cuentaService.cuentaDtos();
            response.put("data", cuentas);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error al obtener las cuentas: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>getCuentaById(@PathVariable Long id) {
        try {
            CuentaDTO cuentaDTO = cuentaService.findById(id);
            return ResponseEntity.ok(Collections.singletonMap("data", cuentaDTO));
        } catch (CuentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Collections.singletonMap("error", "Error procesando la solicitud: " + e.getMessage()));
        }
    }

    @Transactional
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createCuenta(@Valid @RequestBody CuentaRequestDTO cuentaDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            cuentaService.save(cuentaDTO);
            response.put("message", "Se creó con éxito la cuenta: " + cuentaDTO.getNumero());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (CuentaYaExisteException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } catch (CuentaNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("error", "Error procesando la solicitud: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCuenta(@PathVariable Long id, @Valid @RequestBody CuentaDTO cuentaDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean updated = cuentaService.update(id, cuentaDTO);
            if (updated) {
                response.put("message", "Cuenta actualizada con éxito");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("error", "No se pudo actualizar la cuenta");
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (CuentaNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("error", "Error procesando la solicitud: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCuenta(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            cuentaService.delete(id);
            response.put("message", "Cuenta eliminada correctamente");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (CuentaNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            response.put("error", "Error procesando la solicitud: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/cuenta")
    public ResponseEntity<?>findByNumero(@RequestParam String numero) {
        try {
            CuentaDTO cuentaDTO = cuentaService.findByNumero(numero);
            return ResponseEntity.ok(Collections.singletonMap("data", cuentaDTO));
        } catch (CuentaNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                    .body(Collections.singletonMap("error", "Error procesando la solicitud: " + e.getMessage()));
        }
    }
}

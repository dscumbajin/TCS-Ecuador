package com.example.cuentas.controller;

import com.example.cuentas.dto.MovimientoDTO;
import com.example.cuentas.entity.Movimiento;
import com.example.cuentas.exception.MovimientoNotFoundException;
import com.example.cuentas.mapper.MovimientoMappers;
import com.example.cuentas.service.IMoviminetoServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/movimientos")
public class MovimientoController {

    @Autowired
    private IMoviminetoServiceImpl movimientoService;

    @Autowired
    private MovimientoMappers movimientoMappers;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMovimientos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MovimientoDTO> movimientos = movimientoService.movimientoDtos();
            response.put("data", movimientos);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error al obtener los movimientos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getMovimientoById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            MovimientoDTO movimientoDTO = movimientoService.findById(id);
            response.put("data", movimientoDTO);
            return ResponseEntity.ok(response);
        } catch (MovimientoNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Error al obtener el movimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createMovimiento(@Valid @RequestBody MovimientoDTO movimientoDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            MovimientoDTO savedMovimientoDTO = movimientoMappers.toMovimientoDTO(movimientoService.save(movimientoDTO));
            response.put("data", savedMovimientoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (MovimientoNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Error al crear el movimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateMovimiento(@PathVariable Long id, @Valid @RequestBody MovimientoDTO movimientoDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            Movimiento updatedMovimientoDTO = movimientoService.update(id, movimientoDTO);
            response.put("data", movimientoMappers.toMovimientoDTO(updatedMovimientoDTO));
            return ResponseEntity.ok(response);
        } catch (MovimientoNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Error al actualizar el movimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteMovimiento(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            movimientoService.delete(id);
            response.put("message", "Movimiento eliminado correctamente");
            return ResponseEntity.ok(response);
        } catch (MovimientoNotFoundException e) {
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.put("error", "Error al eliminar el movimiento: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/cuenta")
    public ResponseEntity<Map<String, Object>> getMovimientoByNumberCuenta(@RequestParam String numero) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<MovimientoDTO> movimientosDTO = movimientoService.findByCuentaNumero(numero);
            if (movimientosDTO.isEmpty()) {
                response.put("message", "No se encontraron movimientos para la cuenta con número: " + numero);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            response.put("data", movimientosDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Error al obtener los movimientos: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

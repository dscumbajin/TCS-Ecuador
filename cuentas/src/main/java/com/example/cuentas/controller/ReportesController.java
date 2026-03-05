package com.example.cuentas.controller;

import com.example.cuentas.dto.ReporteDTO;
import com.example.cuentas.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/reportes")
public class ReportesController {

    @Autowired
    private IReporteServiceImpl reporteService;

    @Autowired
    private ICunetaServiceImpl cuentaService;

    @GetMapping("/cliente")
    public ResponseEntity<Map<String, Object>> findByCuentaNumeroAndFechaBetween(
            @RequestParam String numero,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ReporteDTO> reporteDTOS = reporteService.findByCuentaNumeroAndFechaBetween(numero, fechaInicio, fechaFin);
            if (reporteDTOS.isEmpty()) {
                response.put("message", "No se encontraron reportes para la cuenta número " + numero +
                        " entre " + fechaInicio + " y " + fechaFin);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            response.put("data", reporteDTOS);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            response.put("error", "Formato de fecha inválido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("error", "Error al obtener los reportes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> findByNombreAndFechaBetween(
            @RequestParam String nombre,
            @RequestParam String fechaInicio,
            @RequestParam String fechaFin) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ReporteDTO> reporteDTOS = reporteService.findByNombreAndFechaBetween(nombre, fechaInicio, fechaFin);
            if (reporteDTOS.isEmpty()) {
                response.put("message", "No existen movimientos del cliente " + nombre +
                        " en el rango de fechas: " + fechaInicio + " - " + fechaFin);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            response.put("data", reporteDTOS);
            return ResponseEntity.ok(response);
        } catch (DateTimeParseException e) {
            response.put("error", "Formato de fecha inválido: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("error", "Error al obtener los reportes: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

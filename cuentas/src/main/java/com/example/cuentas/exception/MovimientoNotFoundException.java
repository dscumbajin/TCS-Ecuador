package com.example.cuentas.exception;

public class MovimientoNotFoundException extends RuntimeException {
    public MovimientoNotFoundException(String mensaje) {
        super(mensaje);
    }
}
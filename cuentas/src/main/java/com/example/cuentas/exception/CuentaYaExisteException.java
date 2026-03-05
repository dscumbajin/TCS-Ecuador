package com.example.cuentas.exception;

public class CuentaYaExisteException extends RuntimeException {
    public CuentaYaExisteException(String mensaje) {
        super(mensaje);
    }
}
package com.example.cuentas.exception;

public class ClienteServiceUnavailableException extends RuntimeException {
    public ClienteServiceUnavailableException(String mensaje) {
        super(mensaje);
    }
}
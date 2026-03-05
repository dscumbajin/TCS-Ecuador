package com.example.cuentas.exception;

public class ApiUnprocessableEntity extends RuntimeException {
    public ApiUnprocessableEntity(String message) {
        super(message);
    }
}

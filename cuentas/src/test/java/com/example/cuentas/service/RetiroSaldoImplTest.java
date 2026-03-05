package com.example.cuentas.service;

import com.example.cuentas.exception.MovimientoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RetiroSaldoImplTest {

    private RetiroSaldoImpl retiroSaldo;

    @BeforeEach
    public void setUp() {
        retiroSaldo = new RetiroSaldoImpl();
    }

    @Test
    public void testAjustarSaldoConValorMenorQueSaldo() {
        String valor = "100.0";
        double saldoInicial = 500.0;
        double saldoFinal = retiroSaldo.ajustarSaldo(valor, saldoInicial);
        assertEquals(400.0, saldoFinal, 0.001);
    }

    @Test
    public void testAjustarSaldoConValorIgualASaldo() {
        String valor = "500.0";
        double saldoInicial = 500.0;
        double saldoFinal = retiroSaldo.ajustarSaldo(valor, saldoInicial);
        assertEquals(0.0, saldoFinal, 0.001);
    }

    @Test
    public void testAjustarSaldoConValorMayorQueSaldo() {
        String valor = "600.0";
        double saldoInicial = 500.0;
        assertThrows(MovimientoNotFoundException.class, () -> {
            retiroSaldo.ajustarSaldo(valor, saldoInicial);
        });
    }

    @Test
    public void testAjustarSaldoConValorNegativo() {
        String valor = "-100.0";
        double saldoInicial = 500.0;
        double saldoFinal = retiroSaldo.ajustarSaldo(valor, saldoInicial);
        assertEquals(400.0, saldoFinal, 0.001);
    }

    @Test
    public void testAjustarSaldoConValorMayorSaldoNegativo() {
        String valor = "-600.0";
        double saldoInicial = 500.0;
        assertThrows(MovimientoNotFoundException.class, () -> {
            retiroSaldo.ajustarSaldo(valor, saldoInicial);
        });
    }

}
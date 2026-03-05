package com.example.cuentas.service;

import com.example.cuentas.exception.MovimientoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DepositoSaldoImplTest {

    private DepositoSaldoImpl depositoSaldo;

    @BeforeEach
    public void setUp() {
        depositoSaldo = new DepositoSaldoImpl();
    }

    @Test
    public void testAjustarSaldoConValorPositivo() {
        String valor = "200.0";
        double saldoInicial = 500.0;
        double saldoFinal = depositoSaldo.ajustarSaldo(valor, saldoInicial);
        assertEquals(700.0, saldoFinal, 0.001);
    }

    @Test
    public void testAjustarSaldoConValorNegativo() {
        String valor = "-100.0";
        double saldoInicial = 500.0;
        assertThrows(MovimientoNotFoundException.class, () -> {
            depositoSaldo.ajustarSaldo(valor, saldoInicial);
        });
    }

    @Test
    public void testAjustarSaldoConValorCero() {
        String valor = "0.0";
        double saldoInicial = 500.0;
        assertThrows(MovimientoNotFoundException.class, () -> {
            depositoSaldo.ajustarSaldo(valor, saldoInicial);
        });
    }

    @Test
    public void testAjustarSaldoConValorDecimalPositivo() {
        String valor = "50.75";
        double saldoInicial = 1000.0;
        double saldoFinal = depositoSaldo.ajustarSaldo(valor, saldoInicial);
        assertEquals(1050.75, saldoFinal, 0.001);
    }

}
package com.example.cuentas.service;

import com.example.cuentas.exception.MovimientoNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DepositoSaldoImpl implements IAjusteSaldoImpl{

    @Override
    public double ajustarSaldo(String valor, double saldo) {
        if (Double.parseDouble(valor) > 0) {
            saldo += Double.parseDouble(valor);
            return saldo;
        } else {
            throw new MovimientoNotFoundException("El valor tiene que ser positivo para depositar");
        }
    }
}

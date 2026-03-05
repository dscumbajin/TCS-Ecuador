package com.example.cuentas.service;

import com.example.cuentas.exception.MovimientoNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class RetiroSaldoImpl implements IAjusteSaldoImpl{

    @Override
    public double ajustarSaldo(String valor, double saldo) {
        if (valor.contains("-")){
            valor = valor.replace("-","");
        }
        if (Double.parseDouble(valor) > saldo) {
            throw new MovimientoNotFoundException("Saldo no disponible");
        } else {
            saldo -= Double.parseDouble(valor);
            return saldo;
        }
    }
}

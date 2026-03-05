package com.example.cuentas.repository;

import com.example.cuentas.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Serializable> {

    Cuenta findByNumero(String numero);
    List<Cuenta> findByClienteId(String clienteId);
    boolean existsByNumero(String numero);
}

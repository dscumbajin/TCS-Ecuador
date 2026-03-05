package com.example.cuentas.repository;

import com.example.cuentas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Serializable> {
    public List<Movimiento> findByCuentaNumeroOrderByFechaDesc(String numero);
    public List<Movimiento> findByCuentaNumero(String numero);
    public List<Movimiento> findByCuentaNumeroAndFechaBetween(String numero, Date fechaInicio, Date fechaFin);

}

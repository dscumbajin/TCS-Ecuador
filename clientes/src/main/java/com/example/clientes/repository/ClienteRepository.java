package com.example.clientes.repository;

import com.example.clientes.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Serializable> {

    Cliente findByIdentificacion(String identificacion);
    Cliente findByNombre(String nombre);
}

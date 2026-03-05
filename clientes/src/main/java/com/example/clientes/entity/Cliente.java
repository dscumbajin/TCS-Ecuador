package com.example.clientes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "clientes")
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name ="clienteId")
public class Cliente extends Persona{

    @Column(name ="contrasena", nullable = false)
    private String contrasena;

    @Column(name ="estado", columnDefinition = "boolean default true")
    private boolean estado;

}

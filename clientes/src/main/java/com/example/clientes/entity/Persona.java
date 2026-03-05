package com.example.clientes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "personas")
@Table(name = "personas", uniqueConstraints = {@UniqueConstraint(columnNames = {"identificacion"})})
@Inheritance( strategy = InheritanceType.JOINED )
public class Persona {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="nombre", nullable = false)
    private String nombre;

    @Column(name ="genero")
    private String genero;

    @Column(name ="edad")
    private int edad;

    @Column(name ="identificacion", nullable = false, length = 10)
    private String identificacion;

    @Column(name ="direccion", nullable = false)
    private String direccion;

    @Column(name ="telefono", nullable = false, length = 10)
    private String telefono;
}

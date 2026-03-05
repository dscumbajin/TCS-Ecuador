package com.example.cuentas.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name= "cuentas")
@Table(name = "cuentas", uniqueConstraints = {@UniqueConstraint(columnNames = {"numero"})})
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuentaId")
    private Long cuentaId;

    @Column(name = "numero", nullable = false)
    private String numero;

    @Column(name = "tipo", nullable = false)
    private String tipoCuenta;

    @Column(name = "saldoInicial", nullable = false)
    private Double saldoInicial;

    @Column(name = "estado", columnDefinition = "boolean default true")
    private boolean estado;

    @Column(name = "clienteId", nullable = false)
    private String clienteId;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    List<Movimiento> movimientos;
}

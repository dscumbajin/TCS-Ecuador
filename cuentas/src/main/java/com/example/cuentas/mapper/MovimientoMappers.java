package com.example.cuentas.mapper;

import com.example.cuentas.dto.MovimientoDTO;
import com.example.cuentas.dto.ReporteDTO;
import com.example.cuentas.entity.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MovimientoMappers {

    @Mappings({
            @Mapping(source = "movimiento.cuenta.numero", target = "numero"),
            @Mapping(source = "movimiento.cuenta.estado", target = "estado")
    })
    MovimientoDTO toMovimientoDTO(Movimiento movimiento);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping( target = "fecha", ignore = true),
            @Mapping(target = "cuenta", ignore = true)
    })
    Movimiento toMovimiento(MovimientoDTO movimientoDTO);

    @Mappings({
            @Mapping(source = "movimiento.cuenta.numero", target = "numeroCuenta"),
            @Mapping(source = "movimiento.cuenta.saldoInicial", target = "saldoInicial"),
            @Mapping(source = "movimiento.saldo", target = "saldoDisponible"),
            @Mapping(source = "movimiento.cuenta.estado", target = "estado"),
            @Mapping(source = "movimiento.valor", target = "movimiento"),
            @Mapping(target = "cliente",ignore = true)
    })
    ReporteDTO toReporteDTO(Movimiento movimiento);
}

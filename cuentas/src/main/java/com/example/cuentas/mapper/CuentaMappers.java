package com.example.cuentas.mapper;

import com.example.cuentas.dto.CuentaDTO;
import com.example.cuentas.dto.CuentaRequestDTO;
import com.example.cuentas.entity.Cuenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CuentaMappers {
    @Mappings({
            @Mapping(source = "clienteId", target = "nombre")
    })
    CuentaDTO toCuentaDTO(Cuenta cuenta);
    @Mappings({
            @Mapping(target = "cuentaId", ignore = true),
            @Mapping(target = "clienteId", ignore = true),
            @Mapping(target = "movimientos", ignore = true)
    })
    Cuenta toCuenta(CuentaRequestDTO cuentaDTO);
}

package com.example.clientes.mapper;

import com.example.clientes.dto.ClienteRequestDTO;
import com.example.clientes.dto.ClienteResponseDTO;
import com.example.clientes.entity.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClienteResponseDTO toClienteDTO(Cliente cliente);

    @Mappings({
            @Mapping(target = "genero", ignore = true),
            @Mapping(target = "edad", ignore = true),
            @Mapping(target = "identificacion", ignore = true),
    })
    ClienteRequestDTO toClienteIdDTO(Cliente cliente);

    Cliente toEntity (ClienteRequestDTO clienteRequestDTO);

}

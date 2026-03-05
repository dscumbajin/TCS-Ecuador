package com.example.cuentas.client;

import com.example.cuentas.dto.ClienteDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="clientes-microservice" , url = "${clientes.service.url}")
public interface ClienteClient {
    @GetMapping(value = "/api/clientes/cliente")
    ClienteDTO getCliente(@RequestParam("identificacion") String identificacion);

    @GetMapping(value = "/api/clientes/{id}")
    ClienteDTO getClienteById(@PathVariable Long id);

    @GetMapping(value = "/api/clientes/nombre")
    ClienteDTO getClienteByName(@RequestParam("nombre") String nombre);
}

package com.gaei.api.clientes.srvapiclientes.service;

import java.util.Optional;

import com.gaei.api.clientes.srvapiclientes.dto.ClienteDTO;
import com.gaei.api.clientes.srvapiclientes.model.Cliente;

public interface IClienteService {
    Cliente crearCliente(ClienteDTO clienteDTO);
    Optional<Cliente> obtenerCliente(String idTx);
    ClienteDTO toDTO(Cliente cliente);
    boolean existeClientePorNumeroDocumento(String numeroDocumento); 
    Cliente actualizarCliente(ClienteDTO clienteDTO);
    Optional<ClienteDTO> consultarCliente(String tipoDocumento, String numeroDocumento);

}

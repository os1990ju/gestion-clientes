package com.gaei.api.clientes.srvapiclientes.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaei.api.clientes.srvapiclientes.dto.ClienteDTO;
import com.gaei.api.clientes.srvapiclientes.model.Cliente;
import com.gaei.api.clientes.srvapiclientes.repository.ClienteRepository;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public Cliente crearCliente(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        mapToEntity(clienteDTO, cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public Optional<Cliente> obtenerCliente(String idTx) {
        return clienteRepository.findById(idTx);
    }

    @Override
    public boolean existeClientePorNumeroDocumento(String numeroDocumento) {
        return clienteRepository.findByNumeroDocumento(numeroDocumento).isPresent();
    }

    @Override
    public Cliente actualizarCliente(ClienteDTO clienteDTO) {
  
        Cliente clienteExistente = clienteRepository.findByNumeroDocumento(clienteDTO.getNumeroDocumento())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado")); 

        mapToEntity(clienteDTO, clienteExistente, clienteExistente.getIdTx());


        return clienteRepository.save(clienteExistente);
    }

    @Override
    public Optional<ClienteDTO> consultarCliente(String tipoDocumento, String numeroDocumento) {

        Optional<Cliente> clienteOpt = clienteRepository.findByNumeroDocumento(numeroDocumento);

        if (clienteOpt.isPresent() && clienteOpt.get().getTipoDocumento().equals(tipoDocumento)) {
            return Optional.of(toDTO(clienteOpt.get()));
        }

        return Optional.empty();
    }

    private void mapToEntity(ClienteDTO clienteDTO, Cliente cliente, String idTx) {
        cliente.setIdTx(idTx); // se usa par no alterar el id de la tx
        cliente.setTipoDocumento(clienteDTO.getTipoDocumento());
        cliente.setNumeroDocumento(clienteDTO.getNumeroDocumento());
        cliente.setPrimerNombre(clienteDTO.getPrimerNombre());
        cliente.setSegundoNombre(clienteDTO.getSegundoNombre());
        cliente.setPrimerApellido(clienteDTO.getPrimerApellido());
        cliente.setSegundoApellido(clienteDTO.getSegundoApellido());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());
    }

    private void mapToEntity(ClienteDTO clienteDTO, Cliente cliente) {
        cliente.setIdTx(clienteDTO.getIdTx());
        cliente.setTipoDocumento(clienteDTO.getTipoDocumento());
        cliente.setNumeroDocumento(clienteDTO.getNumeroDocumento());
        cliente.setPrimerNombre(clienteDTO.getPrimerNombre());
        cliente.setSegundoNombre(clienteDTO.getSegundoNombre());
        cliente.setPrimerApellido(clienteDTO.getPrimerApellido());
        cliente.setSegundoApellido(clienteDTO.getSegundoApellido());
        cliente.setTelefono(clienteDTO.getTelefono());
        cliente.setCorreoElectronico(clienteDTO.getCorreoElectronico());
    }

    public ClienteDTO toDTO(Cliente cliente) {
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setIdTx(cliente.getIdTx());
        clienteDTO.setTipoDocumento(cliente.getTipoDocumento());
        clienteDTO.setNumeroDocumento(cliente.getNumeroDocumento());
        clienteDTO.setPrimerNombre(cliente.getPrimerNombre());
        clienteDTO.setSegundoNombre(cliente.getSegundoNombre());
        clienteDTO.setPrimerApellido(cliente.getPrimerApellido());
        clienteDTO.setSegundoApellido(cliente.getSegundoApellido());
        clienteDTO.setTelefono(cliente.getTelefono());
        clienteDTO.setCorreoElectronico(cliente.getCorreoElectronico());
        return clienteDTO;
    }
}

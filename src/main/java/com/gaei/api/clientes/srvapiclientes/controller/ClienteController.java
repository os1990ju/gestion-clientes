package com.gaei.api.clientes.srvapiclientes.controller;

import javax.validation.Valid;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gaei.api.clientes.srvapiclientes.dto.ClienteDTO;
import com.gaei.api.clientes.srvapiclientes.exception.ClienteConflictException;
import com.gaei.api.clientes.srvapiclientes.exception.ClienteNotFoundException;
import com.gaei.api.clientes.srvapiclientes.model.Cliente;
import com.gaei.api.clientes.srvapiclientes.service.IClienteService;

@RestController
@RequestMapping("/api/clientes")
@Validated
public class ClienteController {
    private static final Logger logger = LogManager.getLogger(ClienteController.class);

    @Autowired
    private IClienteService clienteService;

    @PostMapping("/guardarCliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        
        logger.info("################# Evento: Inicia guardar cliente\nDetalles de la Solicitud: {}", clienteDTO);

        if (clienteService.existeClientePorNumeroDocumento(clienteDTO.getNumeroDocumento())) {
            throw new ClienteConflictException(String.format("El cliente %s %s ya está registrado.",
                    clienteDTO.getTipoDocumento(), clienteDTO.getNumeroDocumento()));
        }

        Cliente nuevoCliente = clienteService.crearCliente(clienteDTO);
        String mensajeExito = String.format("Cliente %s almacenado de forma exitosa.",
                nuevoCliente.getNumeroDocumento());
        logger.info("################# Evento: Finaliza guardar cliente\nEstado de la Solicitud: Éxito {}", nuevoCliente.toString());

        return construirRespuestaOk(nuevoCliente.getIdTx(), mensajeExito);
    }

    @PostMapping("/actualizarCliente")
    public ResponseEntity<?> actualizarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        
        logger.info("################## Evento: Inicia actualización de cliente\nDetalles de la Solicitud: {}", clienteDTO);

        if (!clienteService.existeClientePorNumeroDocumento(clienteDTO.getNumeroDocumento())) {
            throw new ClienteNotFoundException(String.format("El cliente %s %s no se encuentra registrado.",
                    clienteDTO.getTipoDocumento(), clienteDTO.getNumeroDocumento()));
        }

        Cliente clienteActualizado = clienteService.actualizarCliente(clienteDTO);
        String mensajeExito = String.format("Cliente %s actualizado de forma exitosa.",
                clienteActualizado.getNumeroDocumento());
        logger.info("################   Evento: Finaliza actualización de cliente\nEstado de la Solicitud: Éxito {}",clienteActualizado.toString());

        return construirRespuestaOk(clienteActualizado.getIdTx(), mensajeExito);
    }

    @GetMapping("/consultarCliente/{tipoDocumento}_{numeroDocumento}")
    public ResponseEntity<?> consultarCliente(@PathVariable String tipoDocumento,
            @PathVariable String numeroDocumento) {
        logger.info("################# Evento: Inicia consulta de cliente\nTipo de Documento: {}, Número de Documento: {}", tipoDocumento,
                numeroDocumento);

        Optional<ClienteDTO> clienteDTOOpt = clienteService.consultarCliente(tipoDocumento, numeroDocumento);

        if (clienteDTOOpt.isPresent()) {
            logger.info("################# Evento: Finaliza consulta de cliente\nEstado de la Solicitud: Cliente encontrado {}",clienteDTOOpt.get());
         
            return ResponseEntity.ok(clienteDTOOpt.get());
        } else {
            throw new ClienteNotFoundException(String.format("Cliente %s %s no se encuentra registrado.", tipoDocumento,
                    numeroDocumento));
        }
    }

    private ResponseEntity<String> construirRespuestaOk(String idTx, String mensaje) {
        return ResponseEntity.ok().body(
                String.format("{\"idTx\":\"%s\", \"mensaje\": \"%s\"}", idTx, mensaje));
    }

}
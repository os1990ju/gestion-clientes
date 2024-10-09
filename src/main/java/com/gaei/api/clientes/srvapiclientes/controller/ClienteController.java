package com.gaei.api.clientes.srvapiclientes.controller;

import javax.validation.Valid;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gaei.api.clientes.srvapiclientes.dto.ClienteDTO;

import com.gaei.api.clientes.srvapiclientes.model.Cliente;
import com.gaei.api.clientes.srvapiclientes.service.IClienteService;

@RestController
@RequestMapping("/api/clientes")
@Validated
public class ClienteController {
    private static final Logger logger = LogManager.getLogger(ClienteController.class);

    @Autowired
    private IClienteService clienteService;

    // Método para crear un cliente
    @PostMapping("/guardarCliente")
    public ResponseEntity<?> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        String idTx = clienteDTO.getIdTx();
        logger.info("Evento: Inicia guardar cliente\nDetalles de la Solicitud: {}", clienteDTO);

        // Verificar si el cliente ya está registrado por número de documento
        if (clienteService.existeClientePorNumeroDocumento(clienteDTO.getNumeroDocumento())) {
            String mensaje = String.format("El cliente %s %s ya está registrado.",
                    clienteDTO.getTipoDocumento(), clienteDTO.getNumeroDocumento());
            logger.info("Evento: Finaliza guardar cliente\nEstado de la Solicitud: Ya registrado");
            return construirRespuestaConflict(idTx, mensaje);
        }

        Cliente nuevoCliente = clienteService.crearCliente(clienteDTO);
        String mensajeExito = String.format("Cliente %s almacenado de forma exitosa.",
                nuevoCliente.getNumeroDocumento());
        logger.info("Evento: Finaliza guardar cliente\nEstado de la Solicitud: Éxito");

        return construirRespuestaOk(nuevoCliente.getIdTx(), mensajeExito);
    }

    private ResponseEntity<String> construirRespuestaConflict(String idTx, String mensaje) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                String.format("{\"idTx\":\"%s\", \"mensaje\": \"%s\"}", idTx, mensaje));
    }


    @PostMapping("/actualizarCliente")
    public ResponseEntity<?> actualizarCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        String idTx = clienteDTO.getIdTx();
        logger.info("Evento: Inicia actualización de cliente\nDetalles de la Solicitud: {}", clienteDTO);

        // Verificar si el cliente ya está registrado por número de documento
        if (clienteService.existeClientePorNumeroDocumento(clienteDTO.getNumeroDocumento())) {
            // Actualizar el cliente existente
            Cliente clienteActualizado = clienteService.actualizarCliente(clienteDTO);
            String mensajeExito = String.format("Cliente %s actualizado de forma exitosa.",
                    clienteActualizado.getNumeroDocumento());
            logger.info("Evento: Finaliza actualización de cliente\nEstado de la Solicitud: Éxito");
            return construirRespuestaOk(clienteActualizado.getIdTx(), mensajeExito);
        } else {
            String mensajeError = String.format("El cliente %s %s no se encuentra registrado.",
                    clienteDTO.getTipoDocumento(), clienteDTO.getNumeroDocumento());
            logger.info("Evento: Finaliza actualización de cliente\nEstado de la Solicitud: No registrado");
            return construirRespuestaBadRequest(idTx, mensajeError);
        }
    }

    private ResponseEntity<String> construirRespuestaOk(String idTx, String mensaje) {
        return ResponseEntity.ok().body(
                String.format("{\"idTx\":\"%s\", \"mensaje\": \"%s\"}", idTx, mensaje));
    }

    private ResponseEntity<String> construirRespuestaBadRequest(String idTx, String mensaje) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                String.format("{\"idTx\":\"%s\", \"mensaje\": \"%s\"}", idTx, mensaje));
    }

    @GetMapping("/consultarCliente/{tipoDocumento}_{numeroDocumento}")
    public ResponseEntity<?> consultarCliente(@PathVariable String tipoDocumento,
            @PathVariable String numeroDocumento) {
        logger.info("Evento: Inicia consulta de cliente\nTipo de Documento: {}, Número de Documento: {}", tipoDocumento,
                numeroDocumento);

        Optional<ClienteDTO> clienteDTOOpt = clienteService.consultarCliente(tipoDocumento, numeroDocumento);

        if (clienteDTOOpt.isPresent()) {
            logger.info("Evento: Finaliza consulta de cliente\nEstado de la Solicitud: Cliente encontrado");
            return ResponseEntity.ok(clienteDTOOpt.get());
        } else {
            String mensajeError = String.format("Cliente %s %s no se encuentra registrado.", tipoDocumento,
                    numeroDocumento);
            logger.info("Evento: Finaliza consulta de cliente\nEstado de la Solicitud: No registrado");
            return construirRespuestaBadRequest(mensajeError);
        }
    }

    private ResponseEntity<String> construirRespuestaBadRequest(String mensaje) {
        return ResponseEntity.badRequest().body(
                String.format("{\"error\": \"%s\"}", mensaje));
    }

}
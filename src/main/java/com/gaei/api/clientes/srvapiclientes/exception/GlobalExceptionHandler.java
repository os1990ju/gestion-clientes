package com.gaei.api.clientes.srvapiclientes.exception;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        List<String> fieldErrors = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();

        ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.add(error.getField()));

        if (!fieldErrors.isEmpty()) {

            boolean hasEmailError = fieldErrors.contains("correoElectronico");

            List<String> requiredFields = new ArrayList<>(fieldErrors);
            if (hasEmailError) {
                requiredFields.remove("correoElectronico"); 
            }
            String concatenatedFields = String.join(", ", requiredFields);

            String requiredFieldsMessage = "Campos " + concatenatedFields + ". Son obligatorios.";
            errorMessage.append(requiredFieldsMessage);

            if (hasEmailError) {
                errorMessage.append(
                        " Campo correoElectronico: no cumple con la estructura de un correo electrónico válido.");
            }

            errores.put("error", errorMessage.toString().trim());
            errores.put("idTx", null); 
        }
        logger.error("################   Errores en los campos enviados: {}", errorMessage);

        return ResponseEntity.badRequest().body(errores);
    }

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<String> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        logger.error("################   Cliente no encontrado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                String.format("{\"error\": \"%s\"}", ex.getMessage()));
    }

    @ExceptionHandler(ClienteConflictException.class)
    public ResponseEntity<String> handleClienteConflict(ClienteConflictException ex) {
        logger.warn("################    Conflicto: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                String.format("{\"mensaje\": \"%s\"}", ex.getMessage()));
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<String> handleClienteNotFound(ClienteNotFoundException ex) {
        logger.warn("################    No encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                String.format("{\"mensaje\": \"%s\"}", ex.getMessage()));
    }
}

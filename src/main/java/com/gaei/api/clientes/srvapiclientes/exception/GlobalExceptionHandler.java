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

    // Recopilar errores de campos
    ex.getBindingResult().getFieldErrors().forEach(error -> fieldErrors.add(error.getField()));

    // Verificar si hay errores
    if (!fieldErrors.isEmpty()) {
        // Verificar si el campo correoElectronico está en la lista de errores
        boolean hasEmailError = fieldErrors.contains("correoElectronico");

        // Construir el mensaje de error para los campos obligatorios
        List<String> requiredFields = new ArrayList<>(fieldErrors);
        if (hasEmailError) {
            requiredFields.remove("correoElectronico"); // No incluir correoElectronico en el mensaje de obligatorios
        }
        String concatenatedFields = String.join(", ", requiredFields);
        
        // Mensaje para campos obligatorios
        String requiredFieldsMessage = "Campos " + concatenatedFields + ". Son obligatorios.";
        errorMessage.append(requiredFieldsMessage);

        // Mensaje para correoElectronico si hay error
        if (hasEmailError) {
            errorMessage.append(" Campo correoElectronico: no cumple con la estructura de un correo electrónico válido.");
        }

        errores.put("error", errorMessage.toString().trim());
        errores.put("idTx", null); // Aquí puedes establecer el ID si lo tienes
    }
    logger.warn("Errores de validación: {}", errores.get("error"));

    return ResponseEntity.badRequest().body(errores);
}
    

    @ExceptionHandler(ClienteNoEncontradoException.class)
    public ResponseEntity<String> manejarClienteNoEncontrado(ClienteNoEncontradoException ex) {
        logger.error("Cliente no encontrado: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            String.format("{\"error\": \"%s\"}", ex.getMessage()));
    }
}

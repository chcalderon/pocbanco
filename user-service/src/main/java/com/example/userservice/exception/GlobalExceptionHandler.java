package com.example.userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Extraer todos los mensajes de error de validación
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> Map.<String, Object>of(
                        "timestamp", LocalDateTime.now(),
                        "codigo", HttpStatus.BAD_REQUEST.value(),
                        "detail", String.format("Field '%s': %s", error.getField(), error.getDefaultMessage())
                ))
                .collect(Collectors.toList());

        // Crear el cuerpo de la respuesta
        Map<String, Object> response = Map.of("error", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        // Crear el cuerpo del error específico para argumentos inválidos
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "codigo", HttpStatus.BAD_REQUEST.value(),
                "detail", ex.getMessage()
        );

        // Crear el cuerpo de la respuesta
        Map<String, Object> response = Map.of("error", List.of(error));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericExceptions(Exception ex) {
        // Crear el cuerpo del error genérico
        Map<String, Object> error = Map.of(
                "timestamp", LocalDateTime.now(),
                "codigo", HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "detail", "Ocurrió un error inesperado"
        );

        // Crear el cuerpo de la respuesta
        Map<String, Object> response = Map.of("error", List.of(error));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

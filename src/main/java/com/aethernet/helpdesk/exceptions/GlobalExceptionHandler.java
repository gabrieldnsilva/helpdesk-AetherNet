package com.aethernet.helpdesk.exceptions;

import com.aethernet.helpdesk.domain.dto.response.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Componente de aconselhamento global para lidar com exceções lançadas
 * pelos controladores REST da aplicação.
 *
 * Garante que todas as exceções sejam capturadas e transformadas em
 * respostas HTTP padronizadas e amigáveis (usando {@code ErrorResponseDTO}).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções do tipo {@code EntityNotFoundException}.
     *
     * Retorna o status HTTP 404 (Not Found).
     *
     * @param ex A exceção {@code EntityNotFoundException} capturada.
     * @param request A requisição HTTP atual.
     * @return {@code ResponseEntity} contendo o {@code ErrorResponseDTO} e o status 404.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFound(
            EntityNotFoundException ex,
            HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Trata exceções do tipo {@code DomainRuleException} (Violação de Regra de Negócio).
     *
     * Retorna o status HTTP 400 (Bad Request).
     *
     * @param ex A exceção {@code DomainRuleException} capturada.
     * @param request A requisição HTTP atual.
     * @return {@code ResponseEntity} contendo o {@code ErrorResponseDTO} e o status 400.
     */
    @ExceptionHandler(DomainRuleException.class)
    public ResponseEntity<ErrorResponseDTO> handleDomainRule(
            DomainRuleException ex,
            HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Business Rule Violation",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);

    }

    /**
     * Trata exceções do tipo {@code DuplicateEntityException} (Entidade Duplicada).
     *
     * Retorna o status HTTP 409 (Conflict).
     *
     * @param ex A exceção {@code DuplicateEntityException} capturada.
     * @param request A requisição HTTP atual.
     * @return {@code ResponseEntity} contendo o {@code ErrorResponseDTO} e o status 409.
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ErrorResponseDTO> handleDuplicateEntity(
            DuplicateEntityException ex,
            HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Trata exceções de validação de argumentos de método (DTOs com {@code @Valid} falhando).
     *
     * Captura os erros de campo específicos e os retorna em uma estrutura {@code Map}.
     * Retorna o status HTTP 400 (Bad Request).
     *
     * @param ex A exceção {@code MethodArgumentNotValidException} capturada.
     * @param request A requisição HTTP atual.
     * @return {@code ResponseEntity} contendo um mapa com os detalhes de validação e o status 400.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("errors", errors);
        response.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Trata exceções genéricas não capturadas anteriormente.
     *
     * Retorna uma mensagem de erro genérica e o status HTTP 500 (Internal Server Error).
     *
     * @param ex A exceção {@code Exception} genérica capturada.
     * @param request A requisição HTTP atual.
     * @return {@code ResponseEntity} contendo o {@code ErrorResponseDTO} e o status 500.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            HttpServletRequest request) {

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ocorreu um erro inesperado no servidor",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    /**
     * Trata exceções de violação de integridade de dados do Spring Data JPA.
     *
     * Especificamente, tenta identificar se a violação é devido a CPF ou EMAIL duplicados.
     * Retorna o status HTTP 409 (Conflict).
     *
     * @param ex A exceção {@code DataIntegrityViolationException} capturada.
     * @param request A requisição HTTP atual.
     * @return {@code ResponseEntity} contendo o {@code ErrorResponseDTO} e o status 409.
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrity(
            org.springframework.dao.DataIntegrityViolationException ex,
            HttpServletRequest request) {

        String message = "Violação de integridade dos dados";
        if (ex.getMessage() != null && ex.getMessage().contains("CPF")) {
            message = "CPF já cadastrado no sistema";
        } else if (ex.getMessage() != null && ex.getMessage().contains("EMAIL")) {
            message = "E-mail já cadastrado no sistema";
        }

        ErrorResponseDTO error = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Conflict",
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}
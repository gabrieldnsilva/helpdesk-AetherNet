package com.aethernet.helpdesk.domain.dto.response;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) de resposta utilizado para padronizar o formato
 * de erros retornados pela API (ex: validações, recursos não encontrados).
 *
 * <p>Facilita a compreensão e tratamento de erros pelo cliente da API, fornecendo
 * detalhes sobre o ocorrido, o momento e o endpoint afetado.</p>
 *
 * @param timestamp O momento exato (data e hora) em que o erro ocorreu.
 * @param status O código de status HTTP do erro (ex: 404, 500).
 * @param error O nome ou tipo de erro (ex: Not Found, Internal Server Error).
 * @param message A mensagem detalhada do erro, explicando o motivo da falha.
 * @param path O caminho (URI) da requisição que causou o erro.
 */
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path
) {}
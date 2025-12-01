package com.aethernet.helpdesk.domain.dto.response;

import com.aethernet.helpdesk.domain.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) de resposta utilizado para retornar os dados de
 * identificação e perfil de um Técnico ao cliente da API.
 *
 * <p>Exclui informações sensíveis, como a senha, e inclui o conjunto de perfis
 * e a data de criação do registro do Técnico.</p>
 *
 * @param id O identificador único do Técnico.
 * @param nome O nome completo do Técnico.
 * @param cpf O Cadastro de Pessoa Física (CPF) do Técnico.
 * @param email O endereço de e-mail do Técnico.
 * @param perfis O conjunto de Perfis (Roles) associados ao Técnico (ex: TECNICO, ADMIN).
 * @param dataCriacao A data e hora em que o registro do Técnico foi criado.
 */
public record TecnicoResponseDTO(
        @Schema(description = "ID único do técnico", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "Nome completo do técnico", example = "João da Silva")
        String nome,

        @Schema(description = "CPF do técnico", example = "12345678901")
        String cpf,

        @Schema(description = "Endereço de email do técnico", example = "teste@teste.com")
        String email,

        @Schema(description = "Conjunto de perfis do técnico", example = "[\"TECNICO\", \"ADMIN\"]")
        Set<Perfil> perfis,

        @Schema(description = "Data de criação do técnico", example = "2024-01-15T10:15:30")
        LocalDateTime dataCriacao
) {}
package com.aethernet.helpdesk.domain.dto.response;

import com.aethernet.helpdesk.domain.enums.Perfil;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) de resposta utilizado para retornar os dados de
 * identificação e perfil de um Cliente ao cliente da API.
 *
 * <p>Exclui informações sensíveis, como a senha, e inclui o conjunto de perfis
 * e a data de criação do registro.</p>
 *
 * @param id O identificador único do Cliente.
 * @param nome O nome completo do Cliente.
 * @param cpf O Cadastro de Pessoa Física (CPF) do Cliente.
 * @param email O endereço de e-mail do Cliente.
 * @param perfis O conjunto de Perfis (Roles) associados ao Cliente (ex: CLIENTE).
 * @param dataCriacao A data e hora em que o registro do Cliente foi criado.
 */
public record ClienteResponseDTO(
        UUID id,
        String nome,
        String cpf,
        String email,
        Set<Perfil> perfis,
        LocalDateTime dataCriacao
) {}
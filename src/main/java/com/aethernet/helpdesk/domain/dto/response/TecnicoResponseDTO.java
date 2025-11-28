package com.aethernet.helpdesk.domain.dto.response;

import com.aethernet.helpdesk.domain.enums.Perfil;

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
        UUID id,
        String nome,
        String cpf,
        String email,
        Set<Perfil> perfis,
        LocalDateTime dataCriacao
) {}
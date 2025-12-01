package com.aethernet.helpdesk.domain.dto.request;

import com.aethernet.helpdesk.domain.enums.Perfil;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * DTO (Data Transfer Object) de requisição utilizado para receber e validar os dados
 * necessários para a criação ou atualização de um Técnico.
 *
 * <p>Esta classe record inclui restrições de validação do Jakarta Bean Validation
 * para garantir que os dados de identificação e acesso do Técnico estejam corretos.</p>
 *
 * @param nome O nome completo do Técnico. É obrigatório e deve ter entre 3 e 100 caracteres.
 * @param cpf O Cadastro de Pessoa Física (CPF) do Técnico. É obrigatório e deve ter entre 11 e 14 caracteres.
 * @param email O endereço de e-mail do Técnico. É obrigatório e deve ser um formato de e-mail válido.
 * @param senha A senha de acesso do Técnico. É obrigatória e deve ter no mínimo 6 caracteres.
 * @param perfis Opcional. Conjunto de perfis (roles) do Técnico, sendo o perfil TECNICO o mais comum.
 */
public record TecnicoRequestDTO(
        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        @Schema(description = "Nome completo do técnico", example = "João da Silva")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 14, message = "CPF inválido")
        @Schema(description = "CPF do técnico", example = "123.456.789-00")
        String cpf,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        @Schema(description = "Endereço de email do técnico", example = "joao.silva@example.com")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
        @Schema(description = "Senha de acesso do técnico", example = "senhaSegura123")
        String senha,

        @Schema(description = "Conjunto de perfis do técnico", example = "[\"TECNICO\", \"ADMIN\"]", nullable = true)
        Set<Perfil> perfis
) {}
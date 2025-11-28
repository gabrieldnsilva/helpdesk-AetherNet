package com.aethernet.helpdesk.domain.dto.request;

import com.aethernet.helpdesk.domain.enums.Perfil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * DTO (Data Transfer Object) de requisição utilizado para receber e validar os dados
 * necessários para a criação ou atualização de um Cliente.
 *
 * <p>Esta classe record inclui restrições de validação do Jakarta Bean Validation
 * para garantir que os dados de identificação e acesso do Cliente estejam corretos.</p>
 *
 * @param nome O nome completo do Cliente. É obrigatório e deve ter entre 3 e 100 caracteres.
 * @param cpf O Cadastro de Pessoa Física do Cliente. É obrigatório e deve ter entre 11 e 14 caracteres (para incluir formatação).
 * @param email O endereço de e-mail do Cliente. É obrigatório e deve ser um formato de e-mail válido.
 * @param senha A senha de acesso do Cliente. É obrigatória e deve ter entre 6 e 100 caracteres.
 * @param perfis Opcional. Conjunto de perfis (roles) do Cliente, embora o perfil CLIENTE seja o padrão.
 */
public record ClienteRequestDTO (

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Size(min = 11, max = 14, message = "CPF inválido")
        String cpf,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, max = 100, message = "Senha deve ter entre 6 e 100 caracteres")
        String senha,

        Set<Perfil> perfis
) {}
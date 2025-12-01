package com.aethernet.helpdesk.domain.dto.request;

import com.aethernet.helpdesk.domain.enums.Prioridade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) de requisição utilizado para receber e validar os dados
 * necessários para a abertura de um novo Chamado no sistema.
 *
 * <p>Esta classe record inclui as restrições de validação do Jakarta Bean Validation
 * para garantir a integridade dos dados na entrada.</p>
 *
 * @param prioridade O nível de prioridade do chamado (ex: BAIXA, MEDIA, ALTA). É obrigatório.
 * @param titulo O título do chamado. É obrigatório e deve ter entre 5 e 100 caracteres.
 * @param observacoes Descrição detalhada do problema ou solicitação. Opcional, mas limitada a 500 caracteres.
 * @param clienteId O UUID do Cliente que está abrindo o chamado. É obrigatório.
 * @param tecnicoId O UUID do Técnico que será atribuído inicialmente ao chamado (opcional na abertura).
 */
@Schema(description = "Dados para a criação ou atualização de chamado")
public record ChamadoRequestDTO(

        @NotNull(message = "Prioridade é obrigatória")
        @Schema(description = "Prioridade do chamado", example = "Alta")
        Prioridade prioridade,

        @NotBlank(message = "Título é obrigatório")
        @Size(min = 5, max = 100, message = "Título deve ter entre 5 e 100 caracteres")
        @Schema(description = "Título do chamado", example = "Problema de conexão VPN")
        String titulo,

        @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
        @Schema(description = "Observações detalhadas do problema", example = "Cliente não consegue conectar à VPN corporativa.")
        String observacoes,

        @NotNull(message = "Cliente é obrigatório")
        @Schema(description = "ID do cliente que abriu o chamado", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
        UUID clienteId,

        @Schema(description = "ID do técnico atribuído ao chamado", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID tecnicoId
) {}
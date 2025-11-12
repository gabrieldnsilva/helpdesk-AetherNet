package com.aethernet.helpdesk.domain.dto.request;

import com.aethernet.helpdesk.domain.enums.Prioridade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChamadoRequestDTO(
        @NotNull(message = "Prioridade é obrigatória")
        Prioridade prioridade,

        @NotBlank(message = "Título é obrigatório")
        @Size(min = 5, max = 100, message = "Título deve ter entre 5 e 100 caracteres")
        String titulo,

        @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
        String observacoes,

        @NotNull(message = "Cliente é obrigatório")
        UUID clienteId,

        UUID tecnicoId
) {}

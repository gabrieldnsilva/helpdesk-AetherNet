package com.aethernet.helpdesk.domain.dto.response;

import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChamadoResponseDTO(
        UUID id,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        Prioridade prioridade,
        Status status,
        String titulo,
        String observacoes,
        String nomeCliente,
        String nomeTecnico
) {}

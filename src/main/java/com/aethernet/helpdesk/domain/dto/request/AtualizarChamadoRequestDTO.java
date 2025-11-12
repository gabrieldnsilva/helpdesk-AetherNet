package com.aethernet.helpdesk.domain.dto.request;

import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;

import java.util.UUID;

public record AtualizarChamadoRequestDTO(
        Status status,
        Prioridade prioridade,
        String observacoes,
        UUID tecnicoId
) {}

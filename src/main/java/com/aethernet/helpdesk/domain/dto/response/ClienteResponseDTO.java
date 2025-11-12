package com.aethernet.helpdesk.domain.dto.response;

import com.aethernet.helpdesk.domain.enums.Perfil;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ClienteResponseDTO(
        UUID id,
        String nome,
        String cpf,
        String email,
        Set<Perfil> perfis,
        LocalDateTime dataCriacao
) {}

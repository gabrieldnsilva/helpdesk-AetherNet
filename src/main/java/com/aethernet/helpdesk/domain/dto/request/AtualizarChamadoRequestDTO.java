package com.aethernet.helpdesk.domain.dto.request;

import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) de requisição utilizado para receber dados
 * ao atualizar um Chamado (Ticket) existente.
 *
 * <p>Esta classe record define os campos que podem ser modificados por uma requisição de atualização.</p>
 *
 * @param status Novo status do chamado (ex: EM_ANDAMENTO, PAUSADO).
 * @param prioridade Nova prioridade do chamado (ex: ALTA, MEDIA).
 * @param observacoes Novas observações ou notas a serem adicionadas ou substituídas.
 * @param tecnicoId O UUID do Técnico a ser atribuído (ou reatribuído) ao chamado.
 */
public record AtualizarChamadoRequestDTO(
        Status status,
        Prioridade prioridade,
        String observacoes,
        UUID tecnicoId
) {}
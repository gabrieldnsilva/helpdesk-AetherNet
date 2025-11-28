package com.aethernet.helpdesk.domain.dto.response;

import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) de resposta utilizado para retornar informações detalhadas
 * de um Chamado (Ticket) ao cliente da API.
 *
 * <p>Contém todos os atributos do Chamado, incluindo o status e prioridade, e resume
 * as entidades relacionadas (Cliente e Técnico) apenas aos seus nomes, facilitando
 * o consumo de dados.</p>
 *
 * @param id O identificador único do Chamado.
 * @param dataAbertura A data e hora em que o Chamado foi criado.
 * @param dataFechamento A data e hora em que o Chamado foi encerrado (pode ser nulo).
 * @param prioridade A prioridade atual do Chamado.
 * @param status O status atual do Chamado.
 * @param titulo O título do Chamado.
 * @param observacoes As observações e descrição detalhada do Chamado.
 * @param nomeCliente O nome do Cliente que abriu o Chamado.
 * @param nomeTecnico O nome do Técnico atualmente atribuído ao Chamado (pode ser nulo).
 */
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
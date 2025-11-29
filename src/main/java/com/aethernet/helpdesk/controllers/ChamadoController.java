package com.aethernet.helpdesk.controllers;

import com.aethernet.helpdesk.domain.dto.request.ChamadoRequestDTO;
import com.aethernet.helpdesk.domain.dto.request.AtualizarChamadoRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ChamadoResponseDTO;
import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;
import com.aethernet.helpdesk.exceptions.EntityNotFoundException;
import com.aethernet.helpdesk.services.ChamadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar operações relacionadas a Chamados no sistema Helpdesk.
 *
 * Expõe endpoints para criação, leitura, listagem, atribuição e atualização de chamados.
 * A URL base para todos os endpoints deste controlador é "/api/chamados".
 */
@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    /**
     * Construtor para injeção de dependência do serviço de Chamado.
     * @param chamadoService Serviço de domínio responsável pela lógica de negócios dos Chamados.
     */
    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

     // === ENDPOINTS PÚBLICOS ===

    /**
     * Busca um Chamado específico pelo seu identificador único.
     *
     * @param id O UUID do Chamado a ser buscado.
     * @return {@code ResponseEntity} contendo o DTO de resposta do chamado encontrado e o status HTTP 200 (OK).
     * @throws EntityNotFoundException Se o Chamado com o ID fornecido não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    /**
     * Lista todos os Chamados registrados no sistema.
     *
     * @return Uma lista de {@code ChamadoResponseDTO} representando todos os chamados.
     */
    @GetMapping
    public ResponseEntity<List<ChamadoResponseDTO>> listarChamados(
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) Prioridade prioridade
    ) {
        return ResponseEntity.ok(chamadoService.listarTodos(status, prioridade));
    }

    /**
     * Cria e armazena um novo Chamado no sistema.
     *
     * @param dto O DTO {@code ChamadoRequestDTO} contendo os dados para abertura do chamado.
     * O objeto é validado automaticamente pelo {@code @Valid}.
     * @return {@code ResponseEntity} contendo o DTO de resposta do chamado criado e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> criar(@RequestBody @Valid ChamadoRequestDTO dto) {
        ChamadoResponseDTO response = chamadoService.abrir(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    /**
     * Atualiza os dados de um Chamado existente.
     *
     * @param id O UUID do Chamado a ser atualizado.
     * @param dto O DTO {@code AtualizarChamadoRequestDTO} contendo os dados atualizados do chamado.
     * O objeto é validado automaticamente pelo {@code @Valid}.
     * @return {@code ResponseEntity} contendo o DTO de resposta do chamado atualizado e o status HTTP 200 (OK).
     * @throws EntityNotFoundException Se o Chamado com o ID fornecido não for encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> atualizar(@PathVariable UUID id,
                                                        @Valid @RequestBody ChamadoRequestDTO dto) {
        return ResponseEntity.ok(chamadoService.atualizar(id, dto));
    }

    /**
     * Altera o Status (ex: ABERTO para EM_ANDAMENTO) de um Chamado.
     *
     * @param id O UUID do Chamado cujo status será alterado.
     * @param novoStatus O novo {@code Status} a ser aplicado ao chamado.
     * @return {@code ResponseEntity} contendo o Chamado com o status atualizado e o status HTTP 200 (OK).
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<ChamadoResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody Status novoStatus) {

        ChamadoResponseDTO response = chamadoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(response);
    }

    /**
     * Altera o Status de um Chamado para FECHADO.
     * @param id O UUID do Chamado a ser fechado.
     * @return {@code ResponseEntity} contendo o Chamado com o status atualizado e o status HTTP 200 (OK).
     */
    @PatchMapping("/{id}/fechar")
    public ResponseEntity<ChamadoResponseDTO> fechar(@PathVariable UUID id) {
        return ResponseEntity.ok(chamadoService.fechar(id));
    }

}
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

    /**
     * Cria e armazena um novo Chamado no sistema.
     *
     * @param dto O DTO {@code ChamadoRequestDTO} contendo os dados para abertura do chamado.
     * O objeto é validado automaticamente pelo {@code @Valid}.
     * @return {@code ResponseEntity} contendo o DTO de resposta do chamado criado e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> abrirChamado(@RequestBody @Valid ChamadoRequestDTO dto) {
        ChamadoResponseDTO response = chamadoService.abrir(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Busca um Chamado específico pelo seu identificador único.
     *
     * @param id O UUID do Chamado a ser buscado.
     * @return {@code ResponseEntity} contendo o DTO de resposta do chamado encontrado e o status HTTP 200 (OK).
     * @throws EntityNotFoundException Se o Chamado com o ID fornecido não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable UUID id) {
        ChamadoResponseDTO response = chamadoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os Chamados registrados no sistema.
     *
     * @return Uma lista de {@code ChamadoResponseDTO} representando todos os chamados.
     */
    @GetMapping
    public List<ChamadoResponseDTO> listarTodos() {
        return chamadoService.listarTodos();
    }

    /**
     * Lista os Chamados filtrados por um determinado Status.
     *
     * @param status O {@code Status} (ex: ABERTO, EM_ANDAMENTO, FECHADO) pelo qual os chamados serão filtrados.
     * @return Uma lista de {@code ChamadoResponseDTO} que correspondem ao Status fornecido.
     */
    @GetMapping("/status/{status}")
    public List<ChamadoResponseDTO> listarPorStatus(@PathVariable Status status) {
        return chamadoService.listarPorStatus(status);
    }

    /**
     * Atribui um Técnico a um Chamado específico.
     *
     * @param chamadoId O UUID do Chamado que será atribuído.
     * @param tecnicoId O UUID do Técnico que será responsável pelo chamado.
     * @return {@code ResponseEntity} contendo o Chamado atualizado com o Técnico atribuído e o status HTTP 200 (OK).
     */
    @PutMapping("/{chamadoId}/tecnico/{tecnicoId}")
    public ResponseEntity<ChamadoResponseDTO> atribuirTecnico(
            @PathVariable UUID chamadoId,
            @PathVariable UUID tecnicoId) {

        ChamadoResponseDTO response = chamadoService.atribuirTecnico(chamadoId, tecnicoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza o campo de observações de um Chamado específico.
     *
     * @param id O UUID do Chamado a ser atualizado.
     * @param novasObservacoes A nova string de observações para o chamado.
     * @return {@code ResponseEntity} contendo o Chamado atualizado e o status HTTP 200 (OK).
     */
    @PutMapping("/{id}/observacoes")
    public ResponseEntity<ChamadoResponseDTO> atualizarObservacoes(
            @PathVariable UUID id,
            @RequestBody String novasObservacoes) {

        ChamadoResponseDTO response = chamadoService.atualizarObservacoes(id, novasObservacoes);
        return ResponseEntity.ok(response);
    }

    /**
     * Altera o Status (ex: ABERTO para EM_ANDAMENTO) de um Chamado.
     *
     * @param id O UUID do Chamado cujo status será alterado.
     * @param novoStatus O novo {@code Status} a ser aplicado ao chamado.
     * @return {@code ResponseEntity} contendo o Chamado com o status atualizado e o status HTTP 200 (OK).
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ChamadoResponseDTO> atualizarStatus(
            @PathVariable UUID id,
            @RequestBody Status novoStatus) {

        ChamadoResponseDTO response = chamadoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(response);
    }

}
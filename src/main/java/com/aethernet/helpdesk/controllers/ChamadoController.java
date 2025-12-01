package com.aethernet.helpdesk.controllers;

import com.aethernet.helpdesk.domain.dto.request.ChamadoRequestDTO;
import com.aethernet.helpdesk.domain.dto.request.TecnicoRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ChamadoResponseDTO;
import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;
import com.aethernet.helpdesk.exceptions.EntityNotFoundException;
import com.aethernet.helpdesk.services.ChamadoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
@Tag(name = "Chamados", description = "Gerenciamento de chamados de suporte técnico")
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
    @Operation(summary = "Buscar chamado por ID")
    @ApiResponses(value =  {
            @ApiResponse(responseCode = "200", description = "Chamado encontrado"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable @Parameter(description = "ID do Chamado") UUID id) {
        return ResponseEntity.ok(chamadoService.buscarPorId(id));
    }

    /**
     * Lista todos os Chamados registrados no sistema.
     *
     * @return Uma lista de {@code ChamadoResponseDTO} representando todos os chamados.
     */
    @GetMapping
    @Operation(
            summary = "Listar chamados com filtros opcionais",
            description = "Retorna lista de chamados. Pode filtrar por status e/ou prioridade"
    )
    @ApiResponse(responseCode = "200", description = "Lista de chamados retornada")
    public ResponseEntity<List<ChamadoResponseDTO>> listarChamados(
            @RequestParam(required = false) @Parameter(description = "Filtrar por status (ABERTO, EM_ANDAMENTO, PAUSADO, ENCERRADO)") Status status,
            @RequestParam(required = false) @Parameter(description = "Filtrar por prioridade(BAIXA, MEDIA ALTA)") Prioridade prioridade
    ) {
        return ResponseEntity.ok(chamadoService.listarTodos(status, prioridade));
    }

    /**
     * Cria e armazena um novo Chamado no sistema.
     *
     * @param requestDTO O DTO {@code ChamadoRequestDTO} contendo os dados para abertura do chamado.
     * O objeto é validado automaticamente pelo {@code @Valid}.
     * @return {@code ResponseEntity} contendo o DTO de resposta do chamado criado e o status HTTP 201 (Created).
     */
    @PostMapping
    @Operation(
            summary = "Abrir novo chamado",
            description = "Cria um novo chamado de suporte. Se técnico for atribuído, status será EM_ANDAMENTO automaticamente. Caso contrário status será ABERTO."
    )
    @ApiResponses(value =  {
            @ApiResponse(responseCode = "201", description = "Chamado criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente ou técnico não encontrado")
    })
    public ResponseEntity<ChamadoResponseDTO> abrir(@RequestBody @Valid @Parameter(description = "Dados do novo chamado") ChamadoRequestDTO requestDTO) {
        ChamadoResponseDTO response = chamadoService.abrir(requestDTO);
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
     * @param requestDTO O DTO {@code AtualizarChamadoRequestDTO} contendo os dados atualizados do chamado.
     * O objeto é validado automaticamente pelo {@code @Valid}.
     * @return {@code ResponseEntity} contendo o DTO de resposta do chamado atualizado e o status HTTP 200 (OK).
     * @throws EntityNotFoundException Se o Chamado com o ID fornecido não for encontrado.
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar chamado completo",
            description = "Atualiza todos os dados de um chamado. Não permite chamados ENCERRADOS."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamado atualizado"),
            @ApiResponse(responseCode = "400", description = "Violação de regra de negócio (ex: Chamado encerrado)"),
            @ApiResponse(responseCode = "404", description = "Chanado, cliente ou técnico não encontrado")
    })
    public ResponseEntity<ChamadoResponseDTO> atualizar(@PathVariable @Parameter(description = "ID do Chamado: ") UUID id,
                                                        @Valid @RequestBody @Parameter(description = "Dados do chamado atualizado: ") ChamadoRequestDTO requestDTO) {
        return ResponseEntity.ok(chamadoService.atualizar(id, requestDTO));
    }

    /**
     * Altera o Status (ex: ABERTO para EM_ANDAMENTO) de um Chamado.
     *
     * @param id O UUID do Chamado cujo status será alterado.
     * @param novoStatus O novo {@code Status} a ser aplicado ao chamado.
     * @return {@code ResponseEntity} contendo o Chamado com o status atualizado e o status HTTP 200 (OK).
     */
    @PatchMapping("/{id}/status")
    @Operation(
            summary = "Alterar status do chamado",
            description = "Valida transições: PAUSADO só pode ir para EM_ANDAMENTO, ENCERRADO não pode ser alterado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida")
    })
    public ResponseEntity<ChamadoResponseDTO> atualizarStatus(
            @PathVariable @Parameter(description = "ID do chamado: ") UUID id,
            @RequestBody @Parameter(description = "Novo status: ") Status novoStatus) {

        ChamadoResponseDTO response = chamadoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(response);
    }


    /**
     * Atribui um técnico a um Chamado específico.
     * @param id O UUID do Chamado ao qual o técnico será atribuído.
     * @param requestDTO O DTO {@code ChamadoRequestDTO} contendo o ID do técnico a ser atribuído.
     * @return {@code ResponseEntity} contendo o Chamado com o técnico atribuído e o status HTTP 200 (OK).
     */
    @PatchMapping("/{id}/atribuir")
    @Operation(
            summary = "Atribuir técnico ao chamado",
            description = "Atribui técnico e auto-transiciona status ABERTO para EM_ANDAMENTO."
    )
    public ResponseEntity<ChamadoResponseDTO> atribuirTecnico(
            @PathVariable UUID id,
            @RequestBody @Parameter(description = "ID do técnico") ChamadoRequestDTO requestDTO) {
        return ResponseEntity.ok(chamadoService.atribuirTecnico(id, requestDTO.tecnicoId()));
    }


    /**
     * Altera o Status de um Chamado para FECHADO.
     * @param id O UUID do Chamado a ser fechado.
     * @return {@code ResponseEntity} contendo o Chamado com o status atualizado e o status HTTP 200 (OK).
     */
    @PatchMapping("/{id}/fechar")
    @Operation(
            summary = "Fechar chamado",
            description = "Fedine status como ENCERRADO e registra data de fechamento. Não permite fechar chamados ABERTOS diretamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Chamado fechado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Violação de regra de negócio (ex: Chamado ABERTO não pode ser fechado diretamente)"),
            @ApiResponse(responseCode = "404", description = "Chamado não encontrado")
    })
    public ResponseEntity<ChamadoResponseDTO> fechar(@PathVariable @Parameter(description = "ID do Chamado: ") UUID id) {
        return ResponseEntity.ok(chamadoService.fechar(id));
    }

}
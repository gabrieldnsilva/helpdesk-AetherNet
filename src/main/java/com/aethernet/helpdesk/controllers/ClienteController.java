package com.aethernet.helpdesk.controllers;

import com.aethernet.helpdesk.domain.dto.request.ClienteRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ClienteResponseDTO;
import com.aethernet.helpdesk.services.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
 * Controlador REST para gerenciar operações CRUD (Criação, Leitura, Atualização e Deleção)
 * relacionadas à entidade Cliente no sistema Helpdesk.
 *
 * A URL base para todos os endpoints deste controlador é "/api/clientes".
 */
@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de clientes do sistema Helpdesk")
public class ClienteController {

    private final ClienteService clienteService;

    /**
     * Construtor para injeção de dependência do serviço de Cliente.
     * @param clienteService O serviço de domínio responsável pela lógica de negócios dos Clientes.
     */
    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    /**
     * Cria um novo Cliente no sistema.
     *
     * Após a criação, retorna a URL do novo recurso no cabeçalho "Location" (HTTP 201 Created).
     *
     * @param requestDTO O DTO {@code ClienteRequestDTO} contendo os dados para o novo Cliente.
     * O objeto é validado automaticamente pelo {@code @Valid}.
     * @return {@code ResponseEntity} contendo o DTO de resposta do cliente criado e o status HTTP 201 (Created).
     */
    @PostMapping
    @Operation(
            summary = "Criar um novo cliente",
            description = "Cria um novo cliente no sistema com os dados fornecidos. CPF e Email devem ser únicos."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
            @ApiResponse(responseCode = "409", description = "CPF ou email já cadastrado")
    })
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody @Parameter(description = "Dados do novo cliente") ClienteRequestDTO requestDTO) {
        ClienteResponseDTO cliente = clienteService.criar(requestDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.id())
                .toUri();

        return ResponseEntity.created(location).body(cliente);
    }

    /**
     * Busca um Cliente específico pelo seu identificador único.
     *
     * @param id O UUID do Cliente a ser buscado.
     * @return {@code ResponseEntity} contendo o DTO de resposta do cliente encontrado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Cliente com o ID fornecido não for encontrado.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados de um cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                content = @Content(schema = @Schema(implementation = ClienteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable @Parameter(description = "ID do cliente (UUID)") UUID id) {
        ClienteResponseDTO response = clienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os Clientes registrados no sistema.
     *
     * @return {@code ResponseEntity} contendo uma lista de {@code ClienteResponseDTO} e o status HTTP 200 (OK).
     */
    @GetMapping
    @Operation(summary = "Listar todos os clientes", description = "Retorna a lista de clientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso")
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Atualiza os dados de um Cliente existente.
     *
     * @param id O UUID do Cliente a ser atualizado.
     * @param requestDTO O DTO {@code ClienteRequestDTO} contendo os novos dados do Cliente.
     * @return {@code ResponseEntity} contendo o DTO de resposta do cliente atualizado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Cliente com o ID fornecido não for encontrado.
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar cliente",
            description = "Atualiza todos os dados de um cliente existente. CPF e email não podem duplicar com outros clientes."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualziado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos são inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
            @ApiResponse(responseCode = "409", description = "CPF ou email já está em uso")
    })
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable @Parameter(description = "ID do cliente: ") UUID id,
            @Valid @RequestBody @Parameter(description = "Novos dados do cliente") ClienteRequestDTO requestDTO) {
        ClienteResponseDTO response = clienteService.atualizar(id, requestDTO);
        return ResponseEntity.ok(response);
    }

    /**
     * Deleta um Cliente específico do sistema.
     *
     * Retorna uma resposta sem conteúdo, indicando sucesso na exclusão.
     *
     * @param id O UUID do Cliente a ser deletado.
     * @return {@code ResponseEntity} com status HTTP 204 (No Content).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Cliente com o ID fornecido não for encontrado.
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar Cliente", description = "Remove um cliente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable @Parameter(description = "ID do cliente") UUID id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
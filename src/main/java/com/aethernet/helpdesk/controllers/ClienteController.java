package com.aethernet.helpdesk.controllers;

import com.aethernet.helpdesk.domain.dto.request.ClienteRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ClienteResponseDTO;
import com.aethernet.helpdesk.services.ClienteService;
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
     * @param dto O DTO {@code ClienteRequestDTO} contendo os dados para o novo Cliente.
     * O objeto é validado automaticamente pelo {@code @Valid}.
     * @return {@code ResponseEntity} contendo o DTO de resposta do cliente criado e o status HTTP 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO response = clienteService.criar(dto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Busca um Cliente específico pelo seu identificador único.
     *
     * @param id O UUID do Cliente a ser buscado.
     * @return {@code ResponseEntity} contendo o DTO de resposta do cliente encontrado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Cliente com o ID fornecido não for encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(@PathVariable UUID id) {
        ClienteResponseDTO response = clienteService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os Clientes registrados no sistema.
     *
     * @return {@code ResponseEntity} contendo uma lista de {@code ClienteResponseDTO} e o status HTTP 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listarTodos() {
        List<ClienteResponseDTO> clientes = clienteService.listarTodos();
        return ResponseEntity.ok(clientes);
    }

    /**
     * Atualiza os dados de um Cliente existente.
     *
     * @param id O UUID do Cliente a ser atualizado.
     * @param dto O DTO {@code ClienteRequestDTO} contendo os novos dados do Cliente.
     * @return {@code ResponseEntity} contendo o DTO de resposta do cliente atualizado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Cliente com o ID fornecido não for encontrado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        ClienteResponseDTO response = clienteService.atualizar(id, dto);
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
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
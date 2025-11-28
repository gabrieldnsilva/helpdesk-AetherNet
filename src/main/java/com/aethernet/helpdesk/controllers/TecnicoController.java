package com.aethernet.helpdesk.controllers;

import com.aethernet.helpdesk.domain.dto.request.TecnicoRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.TecnicoResponseDTO;
import com.aethernet.helpdesk.services.TecnicoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Controlador REST para gerenciar operações CRUD e de busca relacionadas à entidade Técnico.
 *
 * A URL base para todos os endpoints deste controlador é "/api/tecnicos".
 */
@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    /**
     * Construtor para injeção de dependência do serviço de Técnico.
     * @param tecnicoService O serviço de domínio responsável pela lógica de negócios dos Técnicos.
     */
    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    /**
     * Cria e persiste um novo Técnico no sistema.
     *
     * Retorna o URI do recurso recém-criado no cabeçalho 'Location'.
     *
     * @param dto O DTO {@code TecnicoRequestDTO} contendo os dados para o novo Técnico.
     * @return {@code ResponseEntity} contendo o DTO de resposta do Técnico criado e o status HTTP 201 (Created).
     */
    @PostMapping // Criar um novo técnico
    public ResponseEntity<TecnicoResponseDTO> criar(@Valid @RequestBody TecnicoRequestDTO dto) {
        TecnicoResponseDTO tecnicoCriado = tecnicoService.criar(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tecnicoCriado.id())
                .toUri();
        return ResponseEntity.created(location).body(tecnicoCriado);
    }

    /**
     * Busca um Técnico específico pelo seu identificador único.
     *
     * @param id O UUID do Técnico a ser buscado.
     * @return {@code ResponseEntity} contendo o DTO de resposta do Técnico encontrado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Técnico não for encontrado.
     */
    @GetMapping("/{id}") // Buscar técnico por ID
    public ResponseEntity<TecnicoResponseDTO> buscarPorId(@PathVariable UUID id) {
        TecnicoResponseDTO tecnico = tecnicoService.buscarPorId(id);
        return ResponseEntity.ok(tecnico);
    }

    /**
     * Lista todos os Técnicos registrados no sistema.
     *
     * @return {@code ResponseEntity} contendo uma lista de {@code TecnicoResponseDTO} e o status HTTP 200 (OK).
     */
    @GetMapping // Listar todos os técnicos
    public ResponseEntity<List<TecnicoResponseDTO>> listarTodos() {
        List<TecnicoResponseDTO> tecnicos = tecnicoService.listarTodos();
        return ResponseEntity.ok(tecnicos);
    }

    /**
     * Atualiza os dados de um Técnico existente.
     *
     * @param id O UUID do Técnico a ser atualizado.
     * @param dto O DTO {@code TecnicoRequestDTO} contendo os novos dados do Técnico.
     * @return {@code ResponseEntity} contendo o DTO de resposta do Técnico atualizado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Técnico não for encontrado.
     */
    @PutMapping("/{id}") // Atualizar técnico existente
    public ResponseEntity<TecnicoResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody TecnicoRequestDTO dto) {
        TecnicoResponseDTO tecnicoAtualizado = tecnicoService.atualizar(id, dto);
        return ResponseEntity.ok(tecnicoAtualizado);
    }

    /**
     * Deleta um Técnico específico do sistema.
     *
     * @param id O UUID do Técnico a ser deletado.
     * @return {@code ResponseEntity} com status HTTP 204 (No Content), indicando sucesso na exclusão.
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Técnico não for encontrado.
     */
    @DeleteMapping("/{id}") // Deletar técnico por ID
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        tecnicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Busca um Técnico pelo seu número de CPF.
     *
     * @param cpf A string contendo o CPF do Técnico.
     * @return {@code ResponseEntity} contendo o DTO de resposta do Técnico encontrado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Técnico não for encontrado.
     */
    @GetMapping("/cpf/{cpf}") // Buscar técnico por CPF
    public ResponseEntity<TecnicoResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        TecnicoResponseDTO tecnico = tecnicoService.buscarPorCpf(cpf);
        return ResponseEntity.ok(tecnico);
    }

    /**
     * Busca um Técnico pelo seu endereço de Email.
     *
     * @param email A string contendo o Email do Técnico.
     * @return {@code ResponseEntity} contendo o DTO de resposta do Técnico encontrado e o status HTTP 200 (OK).
     * @throws com.aethernet.helpdesk.exceptions.EntityNotFoundException Se o Técnico não for encontrado.
     */
    @GetMapping("/email/{email}") // Buscar técnico por Email
    public ResponseEntity<TecnicoResponseDTO> buscarPorEmail(@PathVariable String email) {
        TecnicoResponseDTO tecnico = tecnicoService.buscarPorEmail(email);
        return ResponseEntity.ok(tecnico);
    }
}
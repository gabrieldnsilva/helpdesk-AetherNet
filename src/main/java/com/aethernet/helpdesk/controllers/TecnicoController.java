package com.aethernet.helpdesk.controllers;

import com.aethernet.helpdesk.domain.dto.request.TecnicoRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.TecnicoResponseDTO;
import com.aethernet.helpdesk.services.TecnicoService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

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

    @GetMapping("/{id}") // Buscar técnico por ID
    public ResponseEntity<TecnicoResponseDTO> buscarPorId(@PathVariable UUID id) {
        TecnicoResponseDTO tecnico = tecnicoService.buscarPorId(id);
        return ResponseEntity.ok(tecnico);
    }

    @GetMapping // Listar todos os técnicos
    public ResponseEntity<List<TecnicoResponseDTO>> listarTodos() {
        List<TecnicoResponseDTO> tecnicos = tecnicoService.listarTodos();
        return ResponseEntity.ok(tecnicos);
    }

    @PutMapping("/{id}") // Atualizar técnico existente
    public ResponseEntity<TecnicoResponseDTO> atualizar(
            @PathVariable UUID id,
            @Valid @RequestBody TecnicoRequestDTO dto) {
        TecnicoResponseDTO tecnicoAtualizado = tecnicoService.atualizar(id, dto);
        return ResponseEntity.ok(tecnicoAtualizado);
    }

    @DeleteMapping("/{id}") // Deletar técnico por ID
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        tecnicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cpf/{cpf}") // Buscar técnico por CPF
    public ResponseEntity<TecnicoResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        TecnicoResponseDTO tecnico = tecnicoService.buscarPorCpf(cpf);
        return ResponseEntity.ok(tecnico);
    }

    @GetMapping("/email/{email}") // Buscar técnico por Email
    public ResponseEntity<TecnicoResponseDTO> buscarPorEmail(@PathVariable String email) {
        TecnicoResponseDTO tecnico = tecnicoService.buscarPorEmail(email);
        return ResponseEntity.ok(tecnico);
    }
}

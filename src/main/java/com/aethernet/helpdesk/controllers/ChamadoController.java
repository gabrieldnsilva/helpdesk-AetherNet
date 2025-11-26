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

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    private final ChamadoService chamadoService;

    public ChamadoController(ChamadoService chamadoService) {
        this.chamadoService = chamadoService;
    }

    @PostMapping
    public ResponseEntity<ChamadoResponseDTO> abrirChamado(@RequestBody @Valid ChamadoRequestDTO dto) {
        ChamadoResponseDTO response = chamadoService.abrir(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChamadoResponseDTO> buscarPorId(@PathVariable UUID id) {
        ChamadoResponseDTO response = chamadoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public List<ChamadoResponseDTO> listarTodos() {
        return chamadoService.listarTodos();
    }

    @GetMapping("/status/{status}")
    public List<ChamadoResponseDTO> listarPorStatus(@PathVariable Status status) {
        return chamadoService.listarPorStatus(status);
    }

    @PutMapping("/{chamadoId}/tecnico/{tecnicoId}")
    public ResponseEntity<ChamadoResponseDTO> atribuirTecnico(
            @PathVariable UUID chamadoId,
            @PathVariable UUID tecnicoId) {

        ChamadoResponseDTO response = chamadoService.atribuirTecnico(chamadoId, tecnicoId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/observacoes")
    public ResponseEntity<ChamadoResponseDTO> atualizarObservacoes(
            @PathVariable UUID id,
            @RequestBody String novasObservacoes) {

        ChamadoResponseDTO response = chamadoService.atualizarObservacoes(id, novasObservacoes);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/fechar")
    public ResponseEntity<ChamadoResponseDTO> fecharChamado(@PathVariable UUID id) {
        ChamadoResponseDTO response = chamadoService.fechar(id);
        return ResponseEntity.ok(response);
    }

}
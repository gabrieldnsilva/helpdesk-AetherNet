package com.aethernet.helpdesk.services;

import com.aethernet.helpdesk.domain.Chamado;
import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.Tecnico;
import com.aethernet.helpdesk.domain.dto.request.AtualizarChamadoRequestDTO;
import com.aethernet.helpdesk.domain.dto.request.ChamadoRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ChamadoResponseDTO;
import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;
import com.aethernet.helpdesk.exceptions.DomainRuleException;
import com.aethernet.helpdesk.exceptions.EntityNotFoundException;
import com.aethernet.helpdesk.repositories.ChamadoRepository;
import com.aethernet.helpdesk.repositories.ClienteRepository;
import com.aethernet.helpdesk.repositories.TecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository,
                          ClienteRepository clienteRepository,
                          TecnicoRepository tecnicoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional
    public ChamadoResponseDTO abrir(ChamadoRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente", dto.clienteId()));

        Tecnico tecnico = null;
        if (dto.tecnicoId() != null) {
            tecnico = tecnicoRepository.findById(dto.tecnicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Técnico", dto.tecnicoId()));
        }

        Chamado chamado = new Chamado();
        chamado.setId(UUID.randomUUID());
        chamado.setTitulo(dto.titulo());
        chamado.setObservacoes(dto.observacoes());
        chamado.setPrioridade(dto.prioridade());
        chamado.setStatus(Status.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());
        chamado.setCliente(cliente);
        chamado.setTecnico(tecnico);

        chamado = chamadoRepository.save(chamado);
        return toResponseDTO(chamado);
    }

    @Transactional
    public ChamadoResponseDTO atualizar(UUID id, AtualizarChamadoRequestDTO dto) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado", id));

        if (dto.status() != null) {
            validarTransicaoStatus(chamado.getStatus(), dto.status());
            chamado.setStatus(dto.status());

            if (dto.status() == Status.ENCERRADO) {
                chamado.setDataFechamento(LocalDateTime.now());
            }
        }

        if (dto.prioridade() != null) {
            chamado.setPrioridade(dto.prioridade());
        }

        if (dto.observacoes() != null) {
            chamado.setObservacoes(dto.observacoes());
        }

        if (dto.tecnicoId() != null) {
            Tecnico tecnico = tecnicoRepository.findById(dto.tecnicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Técnico", dto.tecnicoId()));
            chamado.setTecnico(tecnico);

            if (chamado.getStatus() == Status.ABERTO) {
                chamado.setStatus(Status.EM_ANDAMENTO);
            }
        }

        chamado = chamadoRepository.save(chamado);
        return toResponseDTO(chamado);
    }

    @Transactional(readOnly = true)
    public ChamadoResponseDTO buscarPorId(UUID id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado", id));
        return toResponseDTO(chamado);
    }

    @Transactional(readOnly = true)
    public List<ChamadoResponseDTO> listarTodos() {
        return chamadoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChamadoResponseDTO> listarPorStatus(Status status) {
        return chamadoRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChamadoResponseDTO> listarPorPrioridade(Prioridade prioridade) {
        return chamadoRepository.findByPrioridade(prioridade)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChamadoResponseDTO fechar(UUID id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado: " +  id));

        validarTransicaoStatus(chamado.getStatus(), Status.ENCERRADO);
        chamado.fechar();

        Chamado chamadoAtualizado  = chamadoRepository.save(chamado);
        return toResponseDTO(chamadoAtualizado);
    }

    @Transactional
    public ChamadoResponseDTO atribuirTecnico(UUID chamadoId, UUID tecnicoId) {
        Chamado chamado = chamadoRepository.findById(chamadoId)
                .orElseThrow(() -> new EntityNotFoundException("Chamado não encontrado: " + chamadoId));

        Tecnico tecnico = tecnicoRepository.findById(tecnicoId)
                .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado: " + tecnicoId));

        if (chamado.getStatus() == Status.ENCERRADO) {
            throw new DomainRuleException("Não é possível atribuir um técnico a um chamado encerrado");
        }

        chamado.setTecnico(tecnico);

        // Se o chamado estava ABERTO, muda para EM_ANDAMENTO após atribuir técnico
        if (chamado.getStatus() == Status.ABERTO) {
            chamado.setStatus(Status.EM_ANDAMENTO);
        }

        Chamado chamadoAtualizado = chamadoRepository.save(chamado);
        return toResponseDTO(chamadoAtualizado);
    }


    private void validarTransicaoStatus(Status atual, Status novo) {
        if (atual == Status.ENCERRADO) {
            throw new DomainRuleException("Não é possível alterar um chamado encerrado");
        }

        if (novo == Status.ENCERRADO && atual == Status.ABERTO) {
            throw new DomainRuleException("Chamado deve estar em andamento antes de ser encerrado");
        }
    }

    private ChamadoResponseDTO toResponseDTO(Chamado chamado) {
        return new ChamadoResponseDTO(
                chamado.getId(),
                chamado.getDataAbertura(),
                chamado.getDataFechamento(),
                chamado.getPrioridade(),
                chamado.getStatus(),
                chamado.getTitulo(),
                chamado.getObservacoes(),
                chamado.getCliente() != null ? chamado.getCliente().getNome() : null,
                chamado.getTecnico() != null ? chamado.getTecnico().getNome() : null
        );
    }
}

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

    // === MÉTODOS PÚBLICOS (ENDPOINTS) ===

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
        chamado.setTitulo(dto.titulo());
        chamado.setObservacoes(dto.observacoes());
        chamado.setPrioridade(dto.prioridade());
        chamado.setStatus(Status.ABERTO);
        chamado.setDataAbertura(LocalDateTime.now());
        chamado.setCliente(cliente);
        chamado.setTecnico(tecnico);

        // Transição para EM_ANDAMENTO se técnico for atribuído na abertura
        if (tecnico != null && chamado.getStatus() == Status.ABERTO) {
            chamado.setStatus(Status.EM_ANDAMENTO);
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
    public List<ChamadoResponseDTO> listarTodos(Status status, Prioridade prioridade) {
        List<Chamado> chamados;
        if (status != null && prioridade != null) {
            chamados = chamadoRepository.findByStatusAndPrioridade(status, prioridade);
        } else if (status != null) {
            chamados = chamadoRepository.findByStatus(status);
        } else if (prioridade != null) {
            chamados = chamadoRepository.findByPrioridade(prioridade);
        } else {
            chamados = chamadoRepository.findAll();
        }
        return chamados.stream().map(this::toResponseDTO).toList();
    }

    @Transactional
    public ChamadoResponseDTO atualizar(UUID id, ChamadoRequestDTO dto) {
        Chamado chamado = buscarChamado(id);
        validarNaoEncerrado(chamado);

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        Tecnico tecnico = null;
        if (dto.tecnicoId() != null) {
            tecnico = tecnicoRepository.findById(dto.tecnicoId())
                    .orElseThrow(() -> new EntityNotFoundException("Técnico não encontrado"));
        }

        chamado.setTitulo(dto.titulo());
        chamado.setObservacoes(dto.observacoes());
        chamado.setPrioridade(dto.prioridade());
        chamado.setCliente(cliente);

        // Atribuição de técnico pode mudar status conforme regras
        chamado.setTecnico(tecnico);
        if (tecnico != null && chamado.getStatus() == Status.ABERTO) {
            chamado.setStatus(Status.EM_ANDAMENTO);
        }

        chamado = chamadoRepository.save(chamado);
        return toResponseDTO(chamado);
    }


    // === OPERAÇÕES ESPECÍFICAS ===

    @Transactional
    public ChamadoResponseDTO alterarStatus(UUID id, Status novoStatus) {
        Chamado chamado = buscarChamado(id);
        validarTransicaoStatus(chamado.getStatus(), novoStatus);

        chamado.setStatus(novoStatus);
        if (novoStatus == Status.ENCERRADO) {
            chamado.fechar();
        }

        return toResponseDTO(chamadoRepository.save(chamado));
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

    @Transactional
    public ChamadoResponseDTO fechar(UUID id) {
        return alterarStatus(id, Status.ENCERRADO);
    }

    // === MÉTODOS PRIVADOS ===

    private Chamado buscarChamado(UUID id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado", id));
    }

    private void validarNaoEncerrado(Chamado chamado) {
        if (chamado.getStatus() == Status.ENCERRADO) {
            throw new DomainRuleException("Não é possível alterar um chamado encerrado");
        }
    }

    private void validarTransicaoStatus(Status atual, Status novo) {
        if (atual == Status.ENCERRADO) {
            throw new DomainRuleException("Não é possível alterar um chamado encerrado");
        }
        if (atual == Status.ABERTO && novo == Status.ENCERRADO) {
            throw new DomainRuleException("Chamado ABERTO não pode ser encerrado diretamente");
        }
        if (atual == Status.PAUSADO && novo != Status.EM_ANDAMENTO) {
            throw new DomainRuleException("Chamado PAUSADO só pode voltar para EM_ANDAMENTO");
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

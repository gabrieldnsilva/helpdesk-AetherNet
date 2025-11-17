package com.aethernet.helpdesk.services;

import com.aethernet.helpdesk.domain.Tecnico;
import com.aethernet.helpdesk.domain.dto.request.TecnicoRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.TecnicoResponseDTO;
import com.aethernet.helpdesk.domain.enums.Perfil;
import com.aethernet.helpdesk.exceptions.DuplicateEntityException;
import com.aethernet.helpdesk.exceptions.EntityNotFoundException;
import com.aethernet.helpdesk.repositories.TecnicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    @Transactional
    public TecnicoResponseDTO criar(TecnicoRequestDTO dto) {

        // Validar Unicidade de CPF e Email
        if (tecnicoRepository.existsByCpf(dto.cpf())) {
            throw new DuplicateEntityException("CPF já cadastrado no sistema");
        }
        if (tecnicoRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntityException("E-mail já cadastrado no sistema");
        }

        Tecnico tecnico = new Tecnico();
        tecnico.setId(UUID.randomUUID());
        tecnico.setNome(dto.nome());
        tecnico.setCpf(dto.cpf());
        tecnico.setEmail(dto.email());
        tecnico.setSenha(dto.senha()); // TODO: criptografar com BCrypt
        tecnico.setDataCriacao(LocalDateTime.now());

        Set<Perfil> perfis = dto.perfis() != null && !dto.perfis().isEmpty()
                ? dto.perfis()
                : Set.of(Perfil.TECNICO);
        perfis.forEach(tecnico::addPerfil);

        tecnico = tecnicoRepository.save(tecnico);
        return toResponseDTO(tecnico);
    }

    @Transactional(readOnly = true)
    public TecnicoResponseDTO buscarPorId(UUID id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Técnico", id));
        return toResponseDTO(tecnico);
    }

    @Transactional(readOnly = true)
    public List<TecnicoResponseDTO> listarTodos() {
        return tecnicoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public TecnicoResponseDTO atualizar(UUID id, TecnicoRequestDTO dto) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Técnico", id));

        // Validar Unicidade apenas se CPF ou Email forem alterados
        if (!tecnico.getCpf().equals(dto.cpf()) && tecnicoRepository.existsByCpf(dto.cpf())) {
            throw new DuplicateEntityException("CPF já cadastrado no sistema");
        }
        if (!tecnico.getEmail().equals(dto.email()) && tecnicoRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntityException("E-mail já cadastrado no sistema");
        }

        tecnico.setNome(dto.nome());
        tecnico.setCpf(dto.cpf());
        tecnico.setEmail(dto.email());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            tecnico.setSenha(dto.senha()); // TODO: Criptografar Senha
        }

        tecnico = tecnicoRepository.save(tecnico);
        return toResponseDTO(tecnico);
    }

    @Transactional
    public void deletar(UUID id) {
        if (!tecnicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Técnico", id);
        }
        tecnicoRepository.deleteById(id);
    }

    private TecnicoResponseDTO toResponseDTO(Tecnico tecnico) {
        return new TecnicoResponseDTO(
                tecnico.getId(),
                tecnico.getNome(),
                tecnico.getCpf(),
                tecnico.getEmail(),
                tecnico.getPerfis(),
                tecnico.getDataCriacao()
        );
    }
}

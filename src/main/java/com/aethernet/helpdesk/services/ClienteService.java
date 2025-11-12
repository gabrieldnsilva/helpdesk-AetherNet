package com.aethernet.helpdesk.services;

import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.dto.request.ClienteRequestDTO;
import com.aethernet.helpdesk.domain.dto.response.ClienteResponseDTO;
import com.aethernet.helpdesk.domain.enums.Perfil;
import com.aethernet.helpdesk.exceptions.DuplicateEntityException;
import com.aethernet.helpdesk.exceptions.EntityNotFoundException;
import com.aethernet.helpdesk.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {

        // Validar Unicidade de CPF e Email
        if (clienteRepository.existsByCpf(dto.cpf())) {
            throw new DuplicateEntityException("CPF já cadastrado no sistema");
        }
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntityException("E-mail já cadastrado no sistema");
        }

        Cliente cliente = new Cliente();
        cliente.setId(UUID.randomUUID());
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setSenha(dto.senha()); // TODO: Criptografar senha
        cliente.setDataCriacao(LocalDateTime.now());

        Set<Perfil> perfis = dto.perfis() != null && !dto.perfis().isEmpty()
                ? dto.perfis()
                : Set.of(Perfil.CLIENTE);
        perfis.forEach(cliente::addPerfil);

        cliente = clienteRepository.save(cliente);
        return toResponseDTO(cliente);
    }

    @Transactional
    public ClienteResponseDTO atualizar(UUID id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));

        // Validar Unicidade apenas se CPF ou Email forem alterados
        if (!cliente.getCpf().equals(dto.cpf()) && clienteRepository.existsByCpf(dto.cpf())) {
            throw new DuplicateEntityException("CPF já cadastrado no sistema");
        }
        if (!cliente.getEmail().equals(dto.email()) && clienteRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntityException("E-mail já cadastrado no sistema");
        }

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());

        if (dto.senha() != null && !dto.senha().isBlank()) {
            cliente.setSenha(dto.senha()); // TODO: criptografar
        }

        cliente = clienteRepository.save(cliente);
        return toResponseDTO(cliente);
    }

    @Transactional
    public void deletar(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }

    private void validarCpfUnico(String cpf) {
        clienteRepository.findByCpf(cpf).ifPresent(c -> {
            throw new DuplicateEntityException("CPF", cpf);
        });
    }

    private void validarEmailUnico(String email) {
        clienteRepository.findByEmail(email).ifPresent(c -> {
            throw new DuplicateEntityException("Email", email);
        });
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getPerfis(),
                cliente.getDataCriacao()
        );
    }
}

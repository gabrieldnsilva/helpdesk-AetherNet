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

/**
 * Serviço de domínio responsável pela execução da lógica de negócios e persistência
 * da entidade Técnico.
 *
 * Gerencia operações como criação, busca por diversos critérios, atualização,
 * deleção e validação de unicidade de dados do Técnico.
 */
@Service
public class TecnicoService {

    private final TecnicoRepository tecnicoRepository;

    /**
     * Construtor para injeção de dependência do repositório de Técnico.
     * @param tecnicoRepository Repositório para operações de persistência de Técnico.
     */
    public TecnicoService(TecnicoRepository tecnicoRepository) {
        this.tecnicoRepository = tecnicoRepository;
    }

    /**
     * Cria e persiste um novo Técnico no banco de dados.
     *
     * Executa a validação de unicidade para CPF e Email antes de salvar. O perfil padrão,
     * se não for fornecido, é {@code TECNICO}.
     *
     * @param dto O DTO de requisição contendo os dados do novo Técnico.
     * @return O DTO de resposta do Técnico recém-criado.
     * @throws DuplicateEntityException Se o CPF ou Email já estiverem cadastrados.
     */
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
        tecnico.setNome(dto.nome());
        tecnico.setCpf(dto.cpf());
        tecnico.setEmail(dto.email());
        tecnico.setSenha(dto.senha()); // TODO: criptografar com BCrypt

        Set<Perfil> perfis = dto.perfis() != null && !dto.perfis().isEmpty()
                ? dto.perfis()
                : Set.of(Perfil.TECNICO);
        perfis.forEach(tecnico::addPerfil);

        tecnico = tecnicoRepository.save(tecnico);
        return toResponseDTO(tecnico);
    }

    /**
     * Busca um Técnico pelo seu identificador único.
     *
     * @param id O UUID do Técnico.
     * @return O DTO de resposta do Técnico encontrado.
     * @throws EntityNotFoundException Se o Técnico com o ID fornecido não existir.
     */
    @Transactional(readOnly = true)
    public TecnicoResponseDTO buscarPorId(UUID id) {
        Tecnico tecnico = tecnicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Técnico", id));
        return toResponseDTO(tecnico);
    }

    /**
     * Busca um Técnico pelo seu número de CPF.
     *
     * @param cpf O CPF do Técnico.
     * @return O DTO de resposta do Técnico encontrado.
     * @throws EntityNotFoundException Se o Técnico com o CPF fornecido não existir.
     */
    @Transactional(readOnly = true)
    public TecnicoResponseDTO buscarPorCpf(String cpf) {
        Tecnico tecnico = tecnicoRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException("Técnico", cpf));
        return toResponseDTO(tecnico);
    }

    /**
     * Busca um Técnico pelo seu endereço de Email.
     *
     * @param email O Email do Técnico.
     * @return O DTO de resposta do Técnico encontrado.
     * @throws EntityNotFoundException Se o Técnico com o Email fornecido não existir.
     */
    @Transactional(readOnly = true)
    public TecnicoResponseDTO buscarPorEmail(String email) {
        Tecnico tecnico = tecnicoRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Técnico", email));
        return toResponseDTO(tecnico);
    }

    /**
     * Lista todos os Técnicos registrados no sistema.
     *
     * @return Uma lista de {@code TecnicoResponseDTO}.
     */
    @Transactional(readOnly = true)
    public List<TecnicoResponseDTO> listarTodos() {
        return tecnicoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Atualiza os dados de um Técnico existente.
     *
     * Inclui validação de unicidade para CPF e Email, garantindo que não pertençam a outro Técnico.
     *
     * @param id O UUID do Técnico a ser atualizado.
     * @param dto O DTO de requisição contendo os novos dados.
     * @return O DTO de resposta do Técnico atualizado.
     * @throws EntityNotFoundException Se o Técnico não for encontrado.
     * @throws DuplicateEntityException Se o novo CPF ou Email já pertencer a outro Técnico.
     */
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

        if (dto.perfis() != null && !dto.perfis().isEmpty()) {
            tecnico.getPerfis().clear();
            dto.perfis().forEach(tecnico::addPerfil);
        }

        tecnico = tecnicoRepository.save(tecnico);
        return toResponseDTO(tecnico);
    }

    /**
     * Deleta um Técnico pelo seu identificador único.
     *
     * @param id O UUID do Técnico a ser deletado.
     * @throws EntityNotFoundException Se o Técnico não for encontrado.
     */
    @Transactional
    public void deletar(UUID id) {
        if (!tecnicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Técnico", id);
        }
        tecnicoRepository.deleteById(id);
    }

    /**
     * Converte uma entidade {@code Tecnico} para o seu respectivo DTO de resposta.
     *
     * @param tecnico A entidade Tecnico a ser convertida.
     * @return O {@code TecnicoResponseDTO} resultante.
     */
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
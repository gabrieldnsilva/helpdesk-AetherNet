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
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors; // Importação adicionada para uso em .toList() antes do Java 16

/**
 * Serviço de domínio responsável pela execução da lógica de negócios e persistência
 * da entidade Cliente.
 *
 * Gerencia operações como criação, busca, atualização, deleção e validação de unicidade.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    /**
     * Construtor para injeção de dependência do repositório de Cliente.
     * @param clienteRepository Repositório para operações de persistência de Cliente.
     */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Cria e persiste um novo Cliente no banco de dados.
     *
     * Executa a validação de unicidade para CPF e Email antes de salvar.
     * O perfil padrão, se não for fornecido, é {@code CLIENTE}.
     *
     * @param dto O DTO de requisição contendo os dados do novo Cliente.
     * @return O DTO de resposta do Cliente recém-criado.
     * @throws DuplicateEntityException Se o CPF ou Email já estiverem cadastrados.
     */
    @Transactional
    public ClienteResponseDTO criar(ClienteRequestDTO dto) {

        // Validar Unicidade de CPF e Email
        if (clienteRepository.existsByCpf(dto.cpf())) {
            throw new DuplicateEntityException("CPF já cadastrado" + dto.cpf());
        }
        if (clienteRepository.existsByEmail(dto.email())) {
            throw new DuplicateEntityException("E-mail já cadastrado" + dto.email());
        }

        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());
        cliente.setEmail(dto.email());
        cliente.setSenha(dto.senha()); // TODO: Criptografar senha

        cliente = clienteRepository.save(cliente);
        return toResponseDTO(cliente);
    }

    /**
     * Busca um Cliente pelo seu identificador único.
     *
     * @param id O UUID do Cliente.
     * @return O DTO de resposta do Cliente encontrado.
     * @throws EntityNotFoundException Se o Cliente com o ID fornecido não existir.
     */
    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPorId(UUID id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente", id));
        return toResponseDTO(cliente);
    }

    /**
     * Lista todos os Clientes registrados no sistema.
     *
     * @return Uma lista de {@code ClienteResponseDTO}.
     */
    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    /**
     * Atualiza os dados de um Cliente existente.
     *
     * Inclui validação de unicidade para CPF e Email, ignorando o próprio Cliente.
     *
     * @param id O UUID do Cliente a ser atualizado.
     * @param dto O DTO de requisição contendo os novos dados do Cliente.
     * @return O DTO de resposta do Cliente atualizado.
     * @throws EntityNotFoundException Se o Cliente não for encontrado.
     * @throws DuplicateEntityException Se o novo CPF ou Email já pertencer a outro Cliente.
     */
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

        if (!cliente.getPerfis().contains(Perfil.CLIENTE)) {
            cliente.addPerfil(Perfil.CLIENTE);
        }

        cliente = clienteRepository.save(cliente);
        return toResponseDTO(cliente);
    }

    /**
     * Deleta um Cliente pelo seu identificador único.
     *
     * @param id O UUID do Cliente a ser deletado.
     * @throws EntityNotFoundException Se o Cliente não for encontrado.
     */
    @Transactional
    public void deletar(UUID id) {
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente", id);
        }
        clienteRepository.deleteById(id);
    }

    /**
     * Converte uma entidade {@code Cliente} para o seu respectivo DTO de resposta.
     *
     * @param cliente A entidade Cliente a ser convertida.
     * @return O {@code ClienteResponseDTO} resultante.
     */
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
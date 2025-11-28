package com.aethernet.helpdesk.services;

import com.aethernet.helpdesk.domain.Chamado;
import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.Tecnico;
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

/**
 * Serviço de domínio responsável pela execução da lógica de negócios e persistência
 * dos Chamados da aplicação.
 *
 * Gerencia operações como abertura, busca, listagem, e atualizações de status, prioridade,
 * técnico e observações.
 */
@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    /**
     * Construtor para injeção de dependências dos repositórios.
     *
     * @param chamadoRepository Repositório para operações de Chamado.
     * @param clienteRepository Repositório para operações de Cliente.
     * @param tecnicoRepository Repositório para operações de Técnico.
     */
    public ChamadoService(ChamadoRepository chamadoRepository,
                          ClienteRepository clienteRepository,
                          TecnicoRepository tecnicoRepository) {
        this.chamadoRepository = chamadoRepository;
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    // === MÉTODOS PÚBLICOS (ENDPOINTS) ===

    /**
     * Cria e persiste um novo Chamado no banco de dados.
     *
     * Define o status inicial como {@code ABERTO} e a data de abertura como o momento atual.
     *
     * @param dto O DTO de requisição contendo os dados do novo chamado (título, observações, prioridade, IDs de cliente/técnico).
     * @return O DTO de resposta do Chamado recém-criado.
     * @throws EntityNotFoundException Se o Cliente ou o Técnico (se fornecido) não forem encontrados.
     */
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

        chamado = chamadoRepository.save(chamado);
        return toResponseDTO(chamado);
    }

    /**
     * Busca um Chamado pelo seu identificador único.
     *
     * @param id O UUID do Chamado.
     * @return O DTO de resposta do Chamado encontrado.
     * @throws EntityNotFoundException Se o Chamado com o ID fornecido não existir.
     */
    @Transactional(readOnly = true)
    public ChamadoResponseDTO buscarPorId(UUID id) {
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado", id));
        return toResponseDTO(chamado);
    }

    /**
     * Lista todos os Chamados registrados no sistema.
     *
     * @return Uma lista de {@code ChamadoResponseDTO}.
     */
    @Transactional(readOnly = true)
    public List<ChamadoResponseDTO> listarTodos() {
        return chamadoRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os Chamados que possuem o {@code Status} especificado.
     *
     * @param status O Status para filtragem (ex: ABERTO, EM_ANDAMENTO, ENCERRADO).
     * @return Uma lista de {@code ChamadoResponseDTO} que correspondem ao status.
     */
    @Transactional(readOnly = true)
    public List<ChamadoResponseDTO> listarPorStatus(Status status) {
        return chamadoRepository.findByStatus(status)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os Chamados que possuem a {@code Prioridade} especificada.
     *
     * @param prioridade A Prioridade para filtragem (ex: BAIXA, MEDIA, ALTA).
     * @return Uma lista de {@code ChamadoResponseDTO} que correspondem à prioridade.
     */
    @Transactional(readOnly = true)
    public List<ChamadoResponseDTO> listarPorPrioridade(Prioridade prioridade) {
        return chamadoRepository.findByPrioridade(prioridade)
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }


    // === OPERAÇÕES ESPECÍFICAS ===

    /**
     * Altera o status de um Chamado existente.
     *
     * Realiza a validação da transição de status. Se o novo status for {@code ENCERRADO},
     * a data de fechamento é registrada no chamado.
     *
     * @param id O UUID do Chamado.
     * @param novoStatus O novo Status a ser aplicado.
     * @return O DTO de resposta do Chamado atualizado.
     * @throws EntityNotFoundException Se o Chamado não for encontrado.
     * @throws DomainRuleException Se a transição de status for inválida (ex: ABERTO diretamente para ENCERRADO, ou tentar mudar um Chamado já ENCERRADO).
     */
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

    /**
     * Altera a prioridade de um Chamado existente.
     *
     * @param id O UUID do Chamado.
     * @param novaPrioridade A nova Prioridade a ser aplicada.
     * @return O DTO de resposta do Chamado atualizado.
     * @throws EntityNotFoundException Se o Chamado não for encontrado.
     * @throws DomainRuleException Se for tentado alterar a prioridade de um Chamado que já está {@code ENCERRADO}.
     */
    @Transactional
    public ChamadoResponseDTO alterarPrioridade(UUID id, Prioridade novaPrioridade) {
        Chamado chamado = buscarChamado(id);

        if (chamado.getStatus() == Status.ENCERRADO) {
            throw new DomainRuleException("Não é possível alterar prioridade de chamado encerrado");
        }

        chamado.setPrioridade(novaPrioridade);
        return toResponseDTO(chamadoRepository.save(chamado));
    }

    /**
     * Atualiza o campo de observações de um Chamado.
     *
     * @param id O UUID do Chamado.
     * @param novasObservacoes A nova string de observações.
     * @return O DTO de resposta do Chamado atualizado.
     * @throws EntityNotFoundException Se o Chamado não for encontrado.
     * @throws DomainRuleException Se for tentado alterar observações de um Chamado que já está {@code ENCERRADO}.
     */
    @Transactional
    public ChamadoResponseDTO atualizarObservacoes(UUID id, String novasObservacoes) {
        Chamado chamado = buscarChamado(id);

        if (chamado.getStatus() == Status.ENCERRADO) {
            throw new DomainRuleException("Não é possível alterar observações de chamado encerrado");
        }

        chamado.setObservacoes(novasObservacoes);
        return toResponseDTO(chamadoRepository.save(chamado));
    }

    /**
     * Atribui um Técnico responsável a um Chamado.
     *
     * Se o Chamado estava {@code ABERTO}, seu status é automaticamente alterado para {@code EM_ANDAMENTO}.
     *
     * @param chamadoId O UUID do Chamado.
     * @param tecnicoId O UUID do Técnico a ser atribuído.
     * @return O DTO de resposta do Chamado atualizado.
     * @throws EntityNotFoundException Se o Chamado ou o Técnico não forem encontrados.
     * @throws DomainRuleException Se for tentado atribuir técnico a um Chamado que já está {@code ENCERRADO}.
     */
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

    /**
     * Encerra um Chamado, alterando seu status para {@code ENCERRADO}.
     *
     * É um atalho para o método {@code alterarStatus(id, Status.ENCERRADO)}.
     *
     * @param id O UUID do Chamado a ser encerrado.
     * @return O DTO de resposta do Chamado encerrado.
     * @throws DomainRuleException Se a regra de domínio não permitir o encerramento (vide {@code validarTransicaoStatus}).
     */
    @Transactional
    public ChamadoResponseDTO fechar(UUID id) {
        return alterarStatus(id, Status.ENCERRADO);
    }

    // === MÉTODOS PRIVADOS ===

    /**
     * Método utilitário para buscar um Chamado pelo ID e garantir que ele exista.
     *
     * @param id O UUID do Chamado.
     * @return A entidade {@code Chamado} encontrada.
     * @throws EntityNotFoundException Se o Chamado não for encontrado.
     */
    private Chamado buscarChamado(UUID id) {
        return chamadoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chamado", id));
    }

    /**
     * Valida se a transição de status de um Chamado é permitida pelas regras de negócio.
     *
     * Regras:
     * 1. Não pode mudar de status se o status atual for {@code ENCERRADO}.
     * 2. Não pode ir de {@code ABERTO} diretamente para {@code ENCERRADO} (deve passar por {@code EM_ANDAMENTO}).
     *
     * @param atual O Status atual do Chamado.
     * @param novo O novo Status desejado.
     * @throws DomainRuleException Se a transição for proibida.
     */
    private void validarTransicaoStatus(Status atual, Status novo) {

        if (atual == Status.ABERTO && novo == Status.ENCERRADO) {
            throw new DomainRuleException("Chamado deve estar em andamento antes de ser encerrado");
        }

        if (atual == Status.ENCERRADO) {
            throw new DomainRuleException("Não é possível alterar um chamado encerrado");
        }
    }

    /**
     * Converte uma entidade {@code Chamado} para o seu respectivo DTO de resposta.
     *
     * Este método mapeia os campos da entidade para o DTO, incluindo a transformação
     * das entidades relacionadas (Cliente e Técnico) apenas em seus nomes.
     *
     * @param chamado A entidade Chamado a ser convertida.
     * @return O {@code ChamadoResponseDTO} resultante.
     */
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

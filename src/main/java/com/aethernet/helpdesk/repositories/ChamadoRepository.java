package com.aethernet.helpdesk.repositories;

import com.aethernet.helpdesk.domain.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Interface de repositório para a entidade {@code Chamado}.
 *
 * Estende {@code JpaRepository}, fornecendo automaticamente as operações CRUD
 * básicas (Criar, Ler, Atualizar, Deletar) para Chamados,
 * utilizando {@code Chamado} como tipo de entidade e {@code UUID} como tipo da chave primária.
 */
@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, UUID> {

    /**
     * Busca e retorna uma lista de todos os Chamados que possuem o {@code Status} especificado.
     *
     * Este método é gerado automaticamente pelo Spring Data JPA baseado no nome.
     *
     * @param status O {@code Status} pelo qual os chamados serão filtrados.
     * @return Uma lista de {@code Chamado}s que correspondem ao status fornecido.
     */
    List<Chamado> findByStatus(com.aethernet.helpdesk.domain.enums.Status status);

    /**
     * Busca e retorna uma lista de todos os Chamados que possuem a {@code Prioridade} especificada.
     *
     * @param prioridade A {@code Prioridade} pela qual os chamados serão filtrados.
     * @return Uma lista de {@code Chamado}s que correspondem à prioridade fornecida.
     */
    List<Chamado> findByPrioridade(com.aethernet.helpdesk.domain.enums.Prioridade prioridade);

    /**
     * Busca e retorna uma lista de todos os Chamados abertos por um Cliente específico.
     *
     * @param clienteId O UUID do Cliente.
     * @return Uma lista de {@code Chamado}s associados ao Cliente com o ID fornecido.
     */
    List<Chamado> findByClienteId(UUID clienteId);

    /**
     * Busca e retorna uma lista de todos os Chamados atribuídos a um Técnico específico.
     *
     * @param tecnicoId O UUID do Técnico.
     * @return Uma lista de {@code Chamado}s associados ao Técnico com o ID fornecido.
     */
    List<Chamado> findByTecnicoId(UUID tecnicoId);
}
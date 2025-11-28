package com.aethernet.helpdesk.repositories;

import com.aethernet.helpdesk.domain.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repositório para a entidade {@code Tecnico}.
 *
 * Estende {@code JpaRepository}, fornecendo as operações CRUD básicas
 * e definindo métodos de consulta personalizados focados na busca e
 * validação de unicidade de CPF e Email.
 */
@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, UUID> {

    /**
     * Busca um Técnico pelo seu número de CPF.
     *
     * @param cpf O número de CPF do Técnico.
     * @return Um {@code Optional} contendo o Técnico, se encontrado.
     */
    Optional<Tecnico> findByCpf(String cpf);

    /**
     * Busca um Técnico pelo seu endereço de Email.
     *
     * @param email O endereço de Email do Técnico.
     * @return Um {@code Optional} contendo o Técnico, se encontrado.
     */
    Optional<Tecnico> findByEmail(String email);

    /**
     * Verifica a existência de um Técnico no banco de dados com o CPF fornecido.
     *
     * @param cpf O CPF a ser verificado.
     * @return {@code true} se um Técnico com este CPF existir, {@code false} caso contrário.
     */
    boolean existsByCpf(String cpf);

    /**
     * Verifica a existência de um Técnico no banco de dados com o Email fornecido.
     *
     * @param email O Email a ser verificado.
     * @return {@code true} se um Técnico com este Email existir, {@code false} caso contrário.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se um Técnico com o CPF fornecido já existe, excluindo o Técnico com o ID especificado.
     *
     * Usado para validação de unicidade durante operações de atualização (PUT),
     * garantindo que o CPF não pertença a outro usuário.
     *
     * @param cpf O CPF a ser verificado.
     * @param id O ID do Técnico a ser ignorado na busca.
     * @return {@code true} se outro Técnico (com ID diferente de {@code id}) possuir o CPF.
     */
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    /**
     * Verifica se um Técnico com o Email fornecido já existe, excluindo o Técnico com o ID especificado.
     *
     * Usado para validação de unicidade durante operações de atualização (PUT),
     * garantindo que o Email não pertença a outro usuário.
     *
     * @param email O Email a ser verificado.
     * @param id O ID do Técnico a ser ignorado na busca.
     * @return {@code true} se outro Técnico (com ID diferente de {@code id}) possuir o Email.
     */
    boolean existsByEmailAndIdNot(String email, UUID id);
}
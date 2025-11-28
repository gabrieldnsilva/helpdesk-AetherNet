package com.aethernet.helpdesk.repositories;

import com.aethernet.helpdesk.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface de repositório para a entidade {@code Cliente}.
 *
 * Estende {@code JpaRepository}, fornecendo automaticamente as operações CRUD básicas
 * para Clientes, e define métodos de consulta personalizados focados na unicidade de
 * CPF e Email.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

    /**
     * Busca um Cliente pelo seu número de CPF.
     *
     * @param cpf O número de CPF do Cliente.
     * @return Um {@code Optional} contendo o Cliente, se encontrado.
     */
    Optional<Cliente> findByCpf(String cpf);

    /**
     * Busca um Cliente pelo seu endereço de Email.
     *
     * @param email O endereço de Email do Cliente.
     * @return Um {@code Optional} contendo o Cliente, se encontrado.
     */
    Optional<Cliente> findByEmail(String email);

    /**
     * Verifica a existência de um Cliente no banco de dados com o CPF fornecido.
     *
     * @param cpf O CPF a ser verificado.
     * @return {@code true} se um Cliente com este CPF existir, {@code false} caso contrário.
     */
    boolean existsByCpf(String cpf);

    /**
     * Verifica a existência de um Cliente no banco de dados com o Email fornecido.
     *
     * @param email O Email a ser verificado.
     * @return {@code true} se um Cliente com este Email existir, {@code false} caso contrário.
     */
    boolean existsByEmail(String email);

    /**
     * Verifica se um Cliente com o CPF fornecido já existe, excluindo o Cliente com o ID especificado.
     *
     * Usado para validação de unicidade durante operações de atualização (PUT),
     * garantindo que o CPF não pertença a outro usuário.
     *
     * @param cpf O CPF a ser verificado.
     * @param id O ID do Cliente a ser ignorado na busca.
     * @return {@code true} se outro Cliente (com ID diferente de {@code id}) possuir o CPF.
     */
    boolean existsByCpfAndIdNot(String cpf, UUID id);

    /**
     * Verifica se um Cliente com o Email fornecido já existe, excluindo o Cliente com o ID especificado.
     *
     * Usado para validação de unicidade durante operações de atualização (PUT),
     * garantindo que o Email não pertença a outro usuário.
     *
     * @param email O Email a ser verificado.
     * @param id O ID do Cliente a ser ignorado na busca.
     * @return {@code true} se outro Cliente (com ID diferente de {@code id}) possuir o Email.
     */
    boolean existsByEmailAndIdNot(String email, UUID id);
}
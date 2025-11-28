package com.aethernet.helpdesk.domain;

import com.aethernet.helpdesk.domain.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Classe base abstrata para todas as entidades de usuário (Cliente e Técnico) do sistema.
 *
 * Implementa atributos e métodos comuns, além de configurar a estratégia de herança
 * usando JOINED, onde cada subclasse é mapeada para sua própria tabela,
 * e a tabela da superclasse armazena apenas os atributos comuns.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {

    /**
     * Identificador único (Primary Key) da Pessoa.
     * Gerado automaticamente utilizando o tipo UUID.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * Nome completo da Pessoa.
     */
    private String nome;

    /**
     * Cadastro de Pessoa Física (CPF).
     * Deve ser único e não pode ser nulo. O tamanho é restrito a 11 caracteres.
     */
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    /**
     * Endereço de e-mail da Pessoa.
     * Deve ser único, não pode ser nulo e é validado como um formato de e-mail.
     */
    @Column(unique = true, nullable = false)
    @Email
    private String email;

    /**
     * Senha de acesso da Pessoa.
     */
    private String senha;

    /**
     * Conjunto de Perfis (Roles) associados à Pessoa (ex: CLIENTE, TECNICO, ADMIN).
     * Carregamento EAGER garante que os perfis sejam carregados junto com a Pessoa.
     * Mapeado para uma tabela auxiliar chamada 'pessoa_perfis'.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pessoa_perfis", joinColumns = @JoinColumn(name = "pessoa_id"))
    @Enumerated(EnumType.STRING)
    private Set<Perfil> perfis = new HashSet<>();

    /**
     * Data e hora em que o registro da Pessoa foi criado.
     * Inicializada automaticamente.
     */
    private LocalDateTime dataCriacao = LocalDateTime.now();

    /**
     * Construtor padrão protegido.
     * Garante que toda Pessoa criada, por padrão, tenha o perfil de {@code CLIENTE}.
     */
    protected Pessoa() {
        addPerfil(Perfil.CLIENTE);
    }

    /**
     * Adiciona um novo Perfil (Role) ao conjunto de perfis da Pessoa.
     *
     * @param perfil O {@code Perfil} a ser adicionado.
     */
    public void addPerfil(Perfil perfil) {
        perfis.add(perfil);
    }

    // --- Getters e Setters ---

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Set<Perfil> getPerfis() {
        return perfis;
    }

    public void setPerfis(Set<Perfil> perfis) {
        this.perfis = perfis;
    }
}
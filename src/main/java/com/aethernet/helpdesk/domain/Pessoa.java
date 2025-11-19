package com.aethernet.helpdesk.domain;

import com.aethernet.helpdesk.domain.enums.Perfil;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String nome;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    private String senha;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pessoa_perfis", joinColumns = @JoinColumn(name = "pessoa_id"))
    @Enumerated(EnumType.STRING)
    private Set<Perfil> perfis = new HashSet<>();

    private LocalDateTime dataCriacao = LocalDateTime.now();

    protected Pessoa() {
        addPerfil(Perfil.CLIENTE);
    }

    public void addPerfil(Perfil perfil) {
        perfis.add(perfil);
    }

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

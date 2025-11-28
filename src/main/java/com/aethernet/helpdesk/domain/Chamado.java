package com.aethernet.helpdesk.domain;

import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Representa a entidade de domínio "Chamado" (Ticket de Suporte).
 *
 * Mapeia para uma tabela no banco de dados e contém todos os atributos
 * e relacionamentos necessários para gerenciar um ticket.
 */
@Entity
public class Chamado {

    /**
     * Identificador único (Primary Key) do Chamado.
     * Gerado automaticamente pelo sistema.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Data e hora exata em que o Chamado foi aberto.
     * É inicializada automaticamente no momento da criação da entidade.
     */
    private LocalDateTime dataAbertura = LocalDateTime.now();

    /**
     * Data e hora em que o Chamado foi encerrado.
     * Este campo é preenchido quando o status é alterado para {@code ENCERRADO}.
     */
    private LocalDateTime dataFechamento;

    /**
     * Nível de Prioridade do Chamado (ex: BAIXA, MEDIA, ALTA).
     * O mapeamento é feito por nome (String) para maior legibilidade no banco de dados.
     */
    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    /**
     * Status atual do Chamado (ex: ABERTO, EM_ANDAMENTO, ENCERRADO).
     * O mapeamento é feito por nome (String).
     */
    @Enumerated(EnumType.STRING)
    private Status status;

    /**
     * Título breve do Chamado.
     */
    private String titulo;

    /**
     * Descrição detalhada do problema ou solicitação.
     * O comprimento máximo é estendido para 2000 caracteres para acomodar descrições longas.
     */
    @Column(length = 2000)
    private String observacoes;

    /**
     * O Cliente que abriu o Chamado.
     * Relacionamento Many-to-One: Muitos Chamados para um Cliente.
     * A coluna {@code cliente_id} não pode ser nula ({@code nullable = false}).
     */
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    /**
     * O Técnico responsável por resolver o Chamado.
     * Relacionamento Many-to-One: Muitos Chamados para um Técnico.
     * A coluna {@code tecnico_id} pode ser nula, pois a atribuição pode ser posterior à abertura.
     */
    @ManyToOne
    @JoinColumn(name = "tecnico_id")
    private Tecnico tecnico;

    /**
     * Método utilitário para fechar o Chamado.
     *
     * Define o status como {@code ENCERRADO} e registra a data e hora atual
     * como a data de fechamento.
     */
    public void fechar() {
        this.status = Status.ENCERRADO;
        this.dataFechamento = LocalDateTime.now();
    }

    // Getters e Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Tecnico getTecnico() {
        return tecnico;
    }

    public void setTecnico(Tecnico tecnico) {
        this.tecnico = tecnico;
    }
}
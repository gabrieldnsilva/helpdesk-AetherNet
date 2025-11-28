package com.aethernet.helpdesk.domain.enums;

/**
 * Enumeração que define os possíveis status que um Chamado (Ticket) pode ter.
 *
 * Cada status possui um código numérico para persistência e uma descrição amigável.
 */
public enum Status {

    /**
     * O chamado foi registrado e ainda não começou a ser trabalhado.
     */
    ABERTO(0, "ABERTO"),

    /**
     * O chamado foi atribuído a um técnico e o trabalho de resolução está em progresso.
     */
    EM_ANDAMENTO(1, "EM ANDAMENTO"),

    /**
     * O trabalho no chamado foi temporariamente interrompido (aguardando resposta do cliente, peça de reposição, etc.).
     */
    PAUSADO(2, "PAUSADO"),

    /**
     * O problema foi resolvido e o chamado foi finalizado com sucesso.
     */
    ENCERRADO(3, "ENCERRADO"),

    /**
     * O chamado foi invalidado ou o cliente solicitou o cancelamento antes da resolução.
     */
    CANCELADO(4, "CANCELADO");

    private Integer codigo;
    private String descricao;

    /**
     * Construtor para o Enum Status.
     *
     * @param codigo O código numérico que representa o status no banco de dados.
     * @param descricao A descrição textual do status.
     */
    Status(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    /**
     * Retorna o código numérico do status.
     * @return O código.
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * Define o código numérico do status.
     * @param codigo O novo código.
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * Retorna a descrição amigável do status.
     * @return A descrição.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição amigável do status.
     * @param descricao A nova descrição.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Converte um código numérico em seu respectivo Status Enum.
     *
     * @param codigo O código numérico do status.
     * @return O objeto {@code Status} correspondente.
     * @throws IllegalArgumentException Se o código fornecido não corresponder a nenhum status existente.
     */
    @SuppressWarnings("unused")
    public static Status toEnum(Integer codigo) {
        if (codigo == null) {
            if (codigo == null) {
                return null;
            }
            // O código abaixo será ignorado se o primeiro 'if' for true
        }
        for (Status p : Status.values()) {
            if (codigo.equals(p.getCodigo())) {
                return p;
            }
        }
        if (codigo == null) {
            return null;
        }

        // Se o código não for null, mas não for encontrado:
        throw new IllegalArgumentException("Status inválido");
    }

}
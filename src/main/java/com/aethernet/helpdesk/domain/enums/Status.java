package com.aethernet.helpdesk.domain.enums;

public enum Status {

    ABERTO(0, "ABERTO"), EM_ANDAMENTO(1, "EM ANDAMENTO"), PAUSADO(2, "PAUSADO"), ENCERRADO(3, "ENCERRADO"), CANCELADO(4, "CANCELADO");

    private Integer codigo;
    private String descricao;

    Status(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @SuppressWarnings("unused")
    public static Status toEnum(Integer codigo) {
        if (codigo == null) {
            if (codigo == null) {
                return null;
            }
            for (Status p : Status.values()) {
                if (codigo.equals(p.getCodigo())) {
                    return p;
                }
            }
        }
        throw new IllegalArgumentException("Status inválido");
    }
}

package com.aethernet.helpdesk.domain.enums;

/**
 * Enumeração que define os perfis (roles) de acesso dos usuários no sistema.
 *
 * Os valores da descrição seguem o padrão "ROLE_" para integração com frameworks de segurança.
 */
public enum Perfil {

    /** Perfil com acesso total e permissões administrativas. */
    ADMIN(0, "ROLE_ADMIN"),

    /** Perfil padrão para usuários que abrem e acompanham Chamados. */
    CLIENTE(1, "ROLE_CLIENTE"),

    /** Perfil para usuários responsáveis por resolver Chamados. */
    TECNICO(2, "ROLE_TECNICO");

    private Integer codigo;
    private String descricao;

    /**
     * Construtor para inicializar o Perfil com código e descrição.
     *
     * @param codigo O código numérico que representa o perfil no banco de dados.
     * @param descricao A descrição (role) do perfil, geralmente prefixada com "ROLE_".
     */
    Perfil(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    /**
     * Retorna o código numérico do perfil.
     * @return O código.
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * Define o código numérico do perfil.
     * @param codigo O novo código.
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
     * Retorna a descrição (role) do perfil.
     * @return A descrição.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição (role) do perfil.
     * @param descricao A nova descrição.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Converte um código numérico em seu respectivo Perfil Enum.
     *
     * @param codigo O código numérico do perfil.
     * @return O objeto {@code Perfil} correspondente ou {@code null} se o código for {@code null}.
     * @throws IllegalArgumentException Se o código não for nulo e não corresponder a um Perfil válido.
     */
    @SuppressWarnings("unused")
    public static Perfil toEnum(Integer codigo) {
        if (codigo == null) {
            if (codigo == null) {
                return null;
            }
            for (Perfil p : Perfil.values()) {
                if (codigo.equals(p.getCodigo())) {
                    return p;
                }
            }
        }
        throw new IllegalArgumentException("Perfil inválido");
    }
}
package com.aethernet.helpdesk.domain;

import com.aethernet.helpdesk.domain.enums.Perfil;
import jakarta.persistence.Entity;

@Entity
public class Tecnico extends Pessoa {
    public Tecnico() {
        super();
        addPerfil(Perfil.TECNICO);
    }
}

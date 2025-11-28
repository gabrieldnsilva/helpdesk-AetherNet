package com.aethernet.helpdesk.domain;

import com.aethernet.helpdesk.domain.enums.Perfil;
import jakarta.persistence.Entity;

@Entity
public class Cliente extends Pessoa {
    public Cliente() {
        super();
        addPerfil(Perfil.CLIENTE);
    }
}

package com.aethernet.helpdesk.repositories;

import com.aethernet.helpdesk.domain.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TecnicoRepository extends JpaRepository<Tecnico, UUID> {

}

package com.aethernet.helpdesk.repositories;

import com.aethernet.helpdesk.domain.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChamadoRepository extends JpaRepository<Chamado, UUID> {
    List<Chamado> findByStatus(com.aethernet.helpdesk.domain.enums.Status status);
    List<Chamado> findByPrioridade(com.aethernet.helpdesk.domain.enums.Prioridade prioridade);
}

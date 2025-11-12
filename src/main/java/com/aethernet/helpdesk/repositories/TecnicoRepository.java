package com.aethernet.helpdesk.repositories;

import com.aethernet.helpdesk.domain.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TecnicoRepository extends JpaRepository<Tecnico, UUID> {
    Optional<Tecnico> findByCpf(String cpf);
    Optional<Tecnico> findByEmail(String email);

    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
    boolean existsByCpfAndIdNot(String cpf, UUID id);
    boolean existsByEmailAndIdNot(String email, UUID id);
}

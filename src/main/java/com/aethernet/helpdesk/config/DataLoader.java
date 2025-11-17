package com.aethernet.helpdesk.config;

import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.Tecnico;
import com.aethernet.helpdesk.domain.enums.Perfil;
import com.aethernet.helpdesk.repositories.ClienteRepository;
import com.aethernet.helpdesk.repositories.TecnicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    public DataLoader(ClienteRepository clienteRepository, TecnicoRepository tecnicoRepository) {
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Cliente cliente1 = new Cliente();
        cliente1.setNome("João Silva");
        cliente1.setEmail("joao@email.com");
        cliente1.setCpf("12345678901");
        cliente1.setSenha("senha123");
        clienteRepository.save(cliente1);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Maria Santos");
        cliente2.setEmail("maria@email.com");
        cliente2.setCpf("98765432100");
        cliente2.setSenha("senha456");
        clienteRepository.save(cliente2);

        Tecnico tecnico1 = new Tecnico();
        tecnico1.setNome("Carlos Técnico");
        tecnico1.setEmail("carlos@aethernet.com");
        tecnico1.setCpf("11122233344");
        tecnico1.setSenha("tecnico123");
        tecnico1.getPerfis().add(Perfil.TECNICO);
        tecnicoRepository.save(tecnico1);

        Tecnico tecnico2 = new Tecnico();
        tecnico2.setNome("Ana Suporte");
        tecnico2.setEmail("ana@aethernet.com");
        tecnico2.setCpf("55566677788");
        tecnico2.setSenha("suporte456");
        tecnico2.getPerfis().add(Perfil.TECNICO);
        tecnicoRepository.save(tecnico2);
    }
}

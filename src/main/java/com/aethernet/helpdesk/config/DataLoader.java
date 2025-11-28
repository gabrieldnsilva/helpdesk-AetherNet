package com.aethernet.helpdesk.config;

import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.Tecnico;
import com.aethernet.helpdesk.domain.enums.Perfil;
import com.aethernet.helpdesk.repositories.ClienteRepository;
import com.aethernet.helpdesk.repositories.TecnicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Componente de configuração responsável por carregar dados iniciais no banco de dados
 * (seed data) assim que a aplicação Spring Boot é iniciada.
 *
 * Implementa a interface {@code CommandLineRunner} para execução no startup.
 */
@Component
public class DataLoader implements CommandLineRunner {

    private final ClienteRepository clienteRepository;
    private final TecnicoRepository tecnicoRepository;

    /**
     * Construtor para injeção de dependência dos repositórios necessários.
     *
     * @param clienteRepository Repositório para persistência de Clientes.
     * @param tecnicoRepository Repositório para persistência de Técnicos.
     */
    public DataLoader(ClienteRepository clienteRepository, TecnicoRepository tecnicoRepository) {
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
    }

    /**
     * Método executado automaticamente pelo Spring Boot na inicialização.
     *
     * Cria e persiste dois Clientes e dois Técnicos de exemplo no banco de dados.
     *
     * @param args Argumentos de linha de comando (não utilizados neste contexto).
     */
    @Override
    @Transactional
    public void run(String... args) {
        // Inicialização dos Clientes
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

        // Inicialização dos Técnicos
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
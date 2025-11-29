package com.aethernet.helpdesk.config;

import com.aethernet.helpdesk.domain.Chamado;
import com.aethernet.helpdesk.domain.Cliente;
import com.aethernet.helpdesk.domain.Tecnico;
import com.aethernet.helpdesk.domain.enums.Perfil;
import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;
import com.aethernet.helpdesk.repositories.ChamadoRepository;
import com.aethernet.helpdesk.repositories.ClienteRepository;
import com.aethernet.helpdesk.repositories.TecnicoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private final ChamadoRepository chamadoRepository;

    /**
     * Construtor para injeção de dependência dos repositórios necessários.
     *
     * @param clienteRepository Repositório para persistência de Clientes.
     * @param tecnicoRepository Repositório para persistência de Técnicos.
     */
    public DataLoader(ClienteRepository clienteRepository, TecnicoRepository tecnicoRepository, ChamadoRepository chamadoRepository) {
        this.clienteRepository = clienteRepository;
        this.tecnicoRepository = tecnicoRepository;
        this.chamadoRepository = chamadoRepository;
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
    public void run(String... args) throws  Exception {
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


        // NOVOS: Chamados de teste
        criarChamadoAberto(cliente1); // UUID fixo para testes
        criarChamadoEmAndamento(cliente2, tecnico1);
        criarChamadoPausado(cliente1, tecnico2);
        criarChamadoEncerrado(cliente2, tecnico1);

    }

    private Chamado criarChamadoAberto(Cliente cliente) {
        Chamado chamado = new Chamado();
        chamado.setPrioridade(Prioridade.ALTA);
        chamado.setStatus(Status.ABERTO);
        chamado.setTitulo("Problema de conexão - TESTE ABERTO");
        chamado.setObservacoes("Chamado sem técnico atribuído");
        chamado.setCliente(cliente);
        chamado.setDataAbertura(LocalDateTime.now());
        return chamadoRepository.save(chamado);
    }

    private Chamado criarChamadoEmAndamento(Cliente cliente, Tecnico tecnico) {
        Chamado chamado = new Chamado();
        chamado.setPrioridade(Prioridade.MEDIA);
        chamado.setStatus(Status.EM_ANDAMENTO);
        chamado.setTitulo("Erro no sistema - TESTE ANDAMENTO");
        chamado.setObservacoes("Chamado em andamento com técnico atribuído");
        chamado.setCliente(cliente);
        chamado.setTecnico(tecnico);
        chamado.setDataAbertura(LocalDateTime.now().minusDays(1));
        return chamadoRepository.save(chamado);
    }

    private Chamado criarChamadoPausado(Cliente cliente, Tecnico tecnico) {
        Chamado chamado = new Chamado();
        chamado.setPrioridade(Prioridade.BAIXA);
        chamado.setStatus(Status.PAUSADO);
        chamado.setTitulo("Solicitação de melhoria - TESTE PAUSADO");
        chamado.setObservacoes("Chamado pausado aguardando informações do cliente");
        chamado.setCliente(cliente);
        chamado.setTecnico(tecnico);
        chamado.setDataAbertura(LocalDateTime.now().minusDays(2));
        return chamadoRepository.save(chamado);
    }

    private Chamado criarChamadoEncerrado(Cliente cliente, Tecnico tecnico) {
        Chamado chamado = new Chamado();
        chamado.setPrioridade(Prioridade.ALTA);
        chamado.setStatus(Status.ENCERRADO);
        chamado.setTitulo("Falha de hardware - TESTE ENCERRADO");
        chamado.setObservacoes("Chamado encerrado após resolução do problema");
        chamado.setCliente(cliente);
        chamado.setTecnico(tecnico);
        chamado.setDataAbertura(LocalDateTime.now().minusDays(5));
        chamado.setDataFechamento(LocalDateTime.now().minusDays(1));
        return chamadoRepository.save(chamado);
    }


}
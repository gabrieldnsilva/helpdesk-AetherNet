## Revisão Geral da Estrutura

### 1. Checklist Arquitetural (Planejado vs. Implementado)
\- MVC separado (domain/entities, repositories, services, controllers)  
\- Entidades principais: `Pessoa` (abstrata), `Cliente`, `Tecnico`, `Chamado`  
\- Enums: `Perfil`, `Status`, `Prioridade`  
\- Identificadores `UUID` (verificar uso de `@GeneratedValue` e `@Type` se necessário)  
\- Persistência: Spring Data JPA (H2 dev / PostgreSQL prod)  
\- Relacionamentos esperados em `Chamado`: referência a `Cliente` e `Tecnico` (verificar `@ManyToOne`)  
\- Controle de perfis: armazenar `perfis` como `Set<Perfil>` (evitar duplicidade)  
\- Datas: usar `LocalDateTime` com controle de criação e fechamento  
\- Senha: armazenar já criptografada (BCrypt) mesmo antes da segurança completa (boa prática)

### 2. Entidades (Pontos a Validar)
\- `Pessoa`: `@MappedSuperclass` ou `@Entity` com estratégia `JOINED` ou `SINGLE_TABLE` (definir).  
\- `Cliente` e `Tecnico`: especializações corretas com construtores.  
\- `Chamado`: garantir `dataAbertura` default no construtor, `dataFechamento` somente quando encerrado.  
\- Enums: usar `@Enumerated(EnumType.STRING)` para evitar acoplamento ordinal.  
\- Campos sensíveis: `senha` nunca exposta em DTOs de resposta.  
\- Coleção `perfis`: se `Set<Perfil>` + `@ElementCollection(fetch = FetchType.EAGER)` (ou tabela dedicada).

### 3. Repositórios
\- Existência de: `PessoaRepository` (se concreta), ou repositórios separados (`ClienteRepository`, `TecnicoRepository`).  
\- `ChamadoRepository`: métodos de filtro ok (`findByStatus`, `findByPrioridade`). Pode adicionar paginação futura (`Page<Chamado>`).  
\- Avaliar necessidade de métodos: `findByClienteId`, `findByTecnicoId`, combinação por prioridade/status.

### 4. Antes dos Endpoints REST
\- Definir DTOs (entrada/saída) para evitar exposição direta das entidades.  
\- Criar camada `Service` para regras (ex.: abrir chamado, transições de status).  
\- Definir exceções customizadas (`EntityNotFoundException`, `DomainRuleException`).  
\- Padronizar respostas de erro (objeto com timestamp, status, message, path).  
\- Adicionar `CommandLineRunner` para seed inicial (clientes, técnicos, chamados).

### 5. Plano Inicial de Testes (Cobertura Essencial)
\- Testes de entidades: construção, regras simples (ex.: adicionar perfil default).  
\- Testes de repositório: persistência básica e filtros (`Status`, `Prioridade`).  
\- Testes de serviço: abrir chamado, alterar status, validar fluxo (não fechar sem técnico, etc.).  
\- Testes de exceções: garantir lançamento correto.  
\- Futuro: testes de controller usando `@WebMvcTest` ou `@SpringBootTest` + MockMvc.

### 6. Escopo Mínimo de Testes Agora (Recomendado)
\- `ChamadoRepositoryTest`  
\- `ClienteRepositoryTest` / `TecnicoRepositoryTest` (persistência simples)  
\- `ChamadoServiceTest` (se já existir serviço)

### 7. Exemplos de Scaffold de Testes

Arquivo: `src/test/java/com/aethernet/helpdesk/repositories/ChamadoRepositoryTest.java`
```java
package com.aethernet.helpdesk.repositories;

import com.aethernet.helpdesk.domain.Chamado;
import com.aethernet.helpdesk.domain.enums.Prioridade;
import com.aethernet.helpdesk.domain.enums.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChamadoRepositoryTest {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Test
    @DisplayName("Deve filtrar chamados por status")
    void deveFiltrarPorStatus() {
        Chamado c1 = new Chamado();
        c1.setTitulo("Falha no acesso");
        c1.setStatus(Status.ABERTO);
        c1.setPrioridade(Prioridade.ALTA);
        c1.setDataAbertura(LocalDateTime.now());

        Chamado c2 = new Chamado();
        c2.setTitulo("Erro de impressão");
        c2.setStatus(Status.ANDAMENTO);
        c2.setPrioridade(Prioridade.BAIXA);
        c2.setDataAbertura(LocalDateTime.now());

        chamadoRepository.save(c1);
        chamadoRepository.save(c2);

        List<Chamado> abertos = chamadoRepository.findByStatus(Status.ABERTO);

        assertThat(abertos).hasSize(1);
        assertThat(abertos.get(0).getTitulo()).isEqualTo("Falha no acesso");
    }

    @Test
    @DisplayName("Deve filtrar chamados por prioridade")
    void deveFiltrarPorPrioridade() {
        Chamado c1 = new Chamado();
        c1.setTitulo("Servidor indisponível");
        c1.setStatus(Status.ABERTO);
        c1.setPrioridade(Prioridade.ALTA);
        c1.setDataAbertura(LocalDateTime.now());

        Chamado c2 = new Chamado();
        c2.setTitulo("Solicitação mudança");
        c2.setStatus(Status.ABERTO);
        c2.setPrioridade(Prioridade.BAIXA);
        c2.setDataAbertura(LocalDateTime.now());

        chamadoRepository.save(c1);
        chamadoRepository.save(c2);

        List<Chamado> alta = chamadoRepository.findByPrioridade(Prioridade.ALTA);

        assertThat(alta).hasSize(1);
        assertThat(alta.get(0).getTitulo()).isEqualTo("Servidor indisponível");
    }
}
```

Arquivo: `src/test/java/com/aethernet/helpdesk/domain/PessoaEntityTest.java`
```java
package com.aethernet.helpdesk.domain;

import com.aethernet.helpdesk.domain.enums.Perfil;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PessoaEntityTest {

    @Test
    void deveAdicionarPerfisSemDuplicar() {
        Cliente cliente = new Cliente();
        cliente.adicionarPerfil(Perfil.CLIENTE);
        cliente.adicionarPerfil(Perfil.CLIENTE);

        assertThat(cliente.getPerfis()).hasSize(1);
    }
}
```

(Adaptar conforme implementação real das entidades.)

### 8. Próximo Passo
\- Ajustar entidades conforme checklist, depois ampliar testes para regras de negócio (serviço).  
\- Só então iniciar controllers REST (CRUD básico).

Se algo acima não estiver aderente ao seu código atual, ajustar agora antes de avançar nos endpoints.
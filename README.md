# 🚀 Helpdesk AetherNet

O objetivo principal do Helpdesk AetherNet é gerenciar o ciclo de vida de um chamado de suporte, desde a abertura até o fechamento.

## Conceitos Aplicados 
* **Persistência de Dados:** Modelagem e mapeamento de entidades para um banco de dados relacional.

* __CRUD:__ Implementação de operações Criar, Read, Update e Delete para a entidade Chamado.

* __Consultas Personalizadas:__ Uso do Spring Data JPA para buscar chamados por Status e Prioridade.

* __Arquitetura:__ Uso da estrutura em camadas (Controller, Service, Repository).

## 🛠️ Tecnologias Utilizadas

O projeto é construído sobre o ecossistema Spring Boot, garantindo uma aplicação robusta e escalável.

* **Linguagem:** Java 17+
* **Framework:** Spring Boot 3+
* **Persistência:** Spring Data JPA e Hibernate
* **Banco de Dados (Dev):** H2 Database (em memória, padrão para desenvolvimento)
* **Documentação da API:** Springdoc OpenAPI / Swagger UI
* **Build Tool:** Maven

## 🧱 Arquitetura e Conceitos

A arquitetura do projeto segue o padrão em **camadas** e aplica rigorosamente os princípios de POO:

### 1. Separação de Responsabilidades

| Camada | Responsabilidade | Tecnologias Chave |
| :--- | :--- | :--- |
| **`Controller`** | Mapear endpoints, receber requisições HTTP e retornar respostas. | `@RestController`, `@RequestMapping` |
| **`Service`** | Contém a lógica de negócio (validações, regras de status, atribuição de técnico). | `@Service`, Transações (`@Transactional`) |
| **`Repository`** | Comunicação direta com o banco de dados (CRUD e consultas personalizadas). | `JpaRepository` (Spring Data JPA) |
| **`Domain`** | Entidades de persistência (`@Entity`), DTOs (Data Transfer Objects) e Enums. | `@Entity`, `@Data` |

### 2. Persistência de Dados

* **Mapeamento Objeto-Relacional:** Utiliza JPA para mapear entidades Java (e.g., `Chamado`, `Tecnico`, `Cliente`) para tabelas no banco de dados.
* **Consultas Personalizadas:** Uso do poder do Spring Data JPA para realizar buscas eficientes, como filtrar chamados por `Status` ou `Prioridade`.

---

## ⚙️ Estrutura do Projeto e Instalação

### Pré-requisitos

Para executar este projeto, você precisará ter instalado:

* __JDK (Java Development Kit):__ Versão 17 ou superior.

* __Maven:__ Gerenciador de dependências (já incluso na estrutura do projeto).

* __IDE (Opcional):__ IntelliJ IDEA ou VS Code (com suporte a Java/Spring).

* __Postman/Insomnia:__ Para testar os endpoints da API REST.


## 📥 Instalação

1. Clonando o Repositório
   ```
   git clone https://github.com/gabrieldnsilva/helpdesk-AetherNet.git
   ```
   Navegue até o diretório do projeto:
   ```
   cd helpdesk-AetherNet
   ```
2. Executando a Aplicação
Este projeto utiliza o H2 Database em memória para desenvolvimento, o que significa que ele não requer configuração externa de banco de dados e os dados serão perdidos ao encerrar a aplicação.

A aplicação será iniciada na porta padrão: ```http://localhost:8080```

3. Acesso ao H2 Console (Opcional)
Durante o desenvolvimento, o banco de dados H2 fica acessível para inspeção das tabelas e dados:
   * URL: ```http://localhost:8080/h2-console```
   * JDBC URL: ```jdbc:h2:mem:helpdeskdb```

---

## 📖 Documentação da API (Swagger UI)

Para facilitar a exploração de todos os endpoints da API (incluindo `Chamados`, `Técnicos` e `Clientes`), o projeto utiliza o **Swagger UI**.

Após executar a aplicação, você pode acessar a documentação interativa pelo seu navegador:

**URL do Swagger UI:**
`http://localhost:8080/swagger-ui/index.html`

Você encontrará:
* A lista completa de rotas.
* Detalhes de DTOs (Request e Response).
* A capacidade de testar os endpoints diretamente.

---

## 🌐 Endpoints da API REST
Método | Rota | Descrição |
| :--- | :--- | :--- |
| `GET` | `/api/chamados/{id}` | Buscar chamado por ID |
| `GET` | `/api/chamados` | Listar chamados com filtros opcionais |
| `POST` | `/api/chamados` | Abrir novo chamado |
| `PUT` | `/api/chamados/{id}` | Atualizar chamado completo |
| `PATCH` | `/api/chamados/{id}/status` | Alterar status do chamado |
| `PATCH` | `/api/chamados/{id}/fechar` | Fechar chamado |
| `PATCH` | `/api/chamados/{id}/atribuir` | Atribuir técnico ao chamado |

### Exemplo: Criar um Chamado (POST /api/chamados)
Antes de criar um chamado, certifique-se de que a aplicação carregou os dados iniciais (Clientes e Técnicos).

* Método: POST

* URL: ```http://localhost:8080/api/chamados```

* Header: ```Content-Type: application/json```

* __Body (raw, JSON):__

 ```JSON
  {
    "prioridade": "ALTA",
    "titulo": "Problema de login na ferramenta X",
    "observacoes": "O cliente não consegue acessar o sistema desde ontem à noite.",
    "clienteId": "UUID_DO_CLIENTE_AQUI", 
    "tecnicoId": null 
}
 ```

__Dica__: Você pode obter um ```UUID``` de cliente ou técnico do H2 Console ```(http://localhost:8080/h2-console)``` inspecionando as tabelas ```CLIENTE``` e ```TECNICO```.

---

## 🧪 Testes e Coleções (Opcional)

Para testar e explorar a API de forma completa, há duas opções principais:

### 1. Documentação Interativa (Recomendado)

Utilize o **Swagger UI** para inspecionar e executar todos os endpoints diretamente no navegador, sem a necessidade de ferramentas externas:

* **URL:** `http://localhost:8080/swagger-ui.html`

### 2. Coleções de Requisições

Se preferir usar clients de API (como Postman ou Insomnia), você pode importar as coleções prontas disponíveis na pasta:

* **Local:** `api-requests/`

Essa pasta contém coleções que já possuem os URLs e corpos de requisição predefinidos para facilitar o início dos testes.

---

## 👥 Colaboradores

O desenvolvimento e a manutenção inicial deste projeto, realizado como parte de um trabalho acadêmico da disciplina de Programação Orientada a Objetos (POO), contaram com a participação de:

* **Gabriel Danilo**
* **Kaique Santos de Carvalho**
* **Rogério de Lima Rodrigues**



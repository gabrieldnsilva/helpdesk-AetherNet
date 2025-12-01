# 🚀 Helpdesk AetherNet

O objetivo principal do Helpdesk AetherNet é gerenciar o ciclo de vida de um chamado de suporte, desde a abertura até o fechamento.

## Conceitos Aplicados 
* **Persistência de Dados:** Modelagem e mapeamento de entidades para um banco de dados relacional.

* __CRUD:__ Implementação de operações Criar, Read, Update e Delete para a entidade Chamado.

* __Consultas Personalizadas:__ Uso do Spring Data JPA para buscar chamados por Status e Prioridade.

* __Arquitetura:__ Uso da estrutura em camadas (Controller, Service, Repository).

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
`http://localhost:8080/swagger-ui.html`

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



## 🧪 Testando os Endpoints com Postman

A seguir, apresentamos exemplos de como realizar as principais operações na coleção do Postman.


### 1. Criar um Chamado (POST /api/chamados)
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


### 2. Atribuir Técnico (PATCH /api/chamados/{chamadoId}/tecnico/{tecnicoId})
Este endpoint demonstra uma regra de negócio: a atribuição de um técnico.

* Método: PATCH

* URL:  ```http://localhost:8080/api/chamados/{UUID_DO_CHAMADO}/{UUID_DO_TECNICO} ```

* Exemplo:  ```http://localhost:8080/api/chamados/6b68b8e0-2f9b-4e8c-8f2e-0a0b1c2d3e4f/tecnico/a1b2c3d4-e5f6-7890-1234-567890abcdef ```

* __Body: Nenhum__

### 3. Alterar Status (PATCH /api/chamados/{id}/status)
Altera o status do chamado. Isso ativa as regras de negócio de transição e fechamento (se o status for ENCERRADO).

* Método: PATCH

* URL: ```http://localhost:8080/api/chamados/{UUID_DO_CHAMADO}/status```

* Header: ```Content-Type: application/json```

* __Body (raw, JSON):__ (Para mudar para EM_ANDAMENTO)

  ```JSON
   "EM_ANDAMENTO"
  ```

__Atenção:__ O corpo da requisição é apenas a string do ```Enum```, conforme definido no seu Controller.


### 4. Criar um Novo Técnico (POST /api/tecnicos)
Cria um registro de usuário que pode resolver chamados.

* Método: POST

* URL: ```http://localhost:8080/api/tecnicos```

* Header: ```Content-Type: application/json```

* __Body (raw, JSON)__ - conforme TecnicoRequestDTO:

```JSON
{
    "nome": "Novo Técnico API",
    "cpf": "55544433322",
    "email": "novo.tecnico@api.com",
    "senha": "senhadotecnico",
    "perfis": ["TECNICO"]
}
```


## 📦 Estrutura de Código
A arquitetura do projeto segue o padrão em camadas, focando na separação de responsabilidades:

* __controller:__ Responsável por mapear os endpoints da API ```(@RestController)``` e processar requisições HTTP.

* __service:__ Contém a lógica de negócio (ex: validações, regras de atualização de status).

* __repository:__ Interfaces que estendem ```JpaRepository```, responsáveis pela comunicação direta com o banco de dados via Spring Data JPA.

* __domain:__ Contém as entidades de persistência ```(Chamado.java)```, DTOs e Enums ```(Status, Prioridade)```.




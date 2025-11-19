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



## 📦 Estrutura de Código
A arquitetura do projeto segue o padrão em camadas, focando na separação de responsabilidades:

* __controller:__ Responsável por mapear os endpoints da API ```(@RestController)``` e processar requisições HTTP.

* __service:__ Contém a lógica de negócio (ex: validações, regras de atualização de status).

* __repository:__ Interfaces que estendem ```JpaRepository```, responsáveis pela comunicação direta com o banco de dados via Spring Data JPA.

* __domain:__ Contém as entidades de persistência ```(Chamado.java)```, DTOs e Enums ```(Status, Prioridade)```.

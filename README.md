# API de Autenticação

Este projeto é uma **API de autenticação** desenvolvida em **Java 21** com **Spring Boot** e **Maven**.
Permite a **criação e autenticação de usuários**, utilizando **JWT** para geração de tokens de acesso.

## Funcionalidades

* Criação de usuários
* Autenticação de usuários
* Geração e validação de tokens JWT
* Documentação interativa com **Swagger**

## Tecnologias

* Java 21
* Spring Boot
* Maven
* PostgreSQL (em container)
* Docker
* Lombok

## Testes

* JUnit 5
* Mockito
* JavaFaker
* Testes de integração cobrindo fluxos principais da API

## Arquitetura

A aplicação está containerizada em **Docker** e se comunica com um banco **PostgreSQL** também em container.
O uso do **Lombok** reduz a verbosidade do código, facilitando a manutenção.

A documentação da API pode ser acessada via **Swagger** em:

```
http://localhost:8082/swagger-ui.html
```

## Como clonar o projeto

```bash
git clone https://github.com/a-devrepo/autenticacaoApi.git
cd autenticacaoApi
```

## Executando o projeto

Com o Docker e Docker Compose instalados, basta subir os containers:

```bash
docker-compose up -d --build
```

A API estará disponível em:

```
http://localhost:8082/api/v1/usuarios
```

## Stack de Services (Docker Compose)

O projeto define dois serviços principais:

* **projeto-autenticacaoapi-springboot**

  * Container da aplicação Spring Boot
  * Porta exposta: `8082`
  * Depende do banco `autenticacaoapi-db`
  * Conectado à rede `autenticacaoapi-network`

* **autenticacaoapi-db**

  * Banco de dados PostgreSQL (imagem oficial `postgres:16`)
  * Porta exposta: `5438` (mapeada para `5432` no container)
  * Volume persistente: `autenticacaoapidb-data`
  * Configurado com usuário, senha e nome de banco específicos

## Observações

* Certifique-se de que as portas configuradas no `docker-compose.yml` não estejam em uso.

